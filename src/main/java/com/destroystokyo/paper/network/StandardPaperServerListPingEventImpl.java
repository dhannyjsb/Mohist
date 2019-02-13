package com.destroystokyo.paper.network;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.status.server.SPacketServerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public final class StandardPaperServerListPingEventImpl extends PaperServerListPingEventImpl
{
    private static final GameProfile[] EMPTY_PROFILES;
    private static final UUID FAKE_UUID;
    private GameProfile[] originalSample;

    private StandardPaperServerListPingEventImpl(final MinecraftServer server, final NetworkManager networkManager, final ServerStatusResponse ping) {
        super(server, new PaperStatusClient(networkManager), ping.getVersion().getProtocol(), server.server.getServerIcon());
        this.originalSample = (ping.getPlayers() == null) ? null : ping.getPlayers().getPlayers();
    }

    @Nonnull
    @Override
    public List<PlayerProfile> getPlayerSample() {
        final List<PlayerProfile> sample = super.getPlayerSample();
        if (this.originalSample != null) {
            for (final GameProfile profile : this.originalSample) {
                sample.add(CraftPlayerProfile.asBukkitCopy(profile));
            }
            this.originalSample = null;
        }
        return sample;
    }

    private GameProfile[] getPlayerSampleHandle() {
        if (this.originalSample != null) {
            return this.originalSample;
        }
        final List<PlayerProfile> entries = super.getPlayerSample();
        if (entries.isEmpty()) {
            return StandardPaperServerListPingEventImpl.EMPTY_PROFILES;
        }
        final GameProfile[] profiles = new GameProfile[entries.size()];
        for (int i = 0; i < profiles.length; ++i) {
            final PlayerProfile profile = entries.get(i);
            if (profile.getId() != null && profile.getName() != null) {
                profiles[i] = CraftPlayerProfile.asAuthlib(profile);
            }
            else {
                profiles[i] = new GameProfile(MoreObjects.firstNonNull(profile.getId(), StandardPaperServerListPingEventImpl.FAKE_UUID), Strings.nullToEmpty(profile.getName()));
            }
        }
        return profiles;
    }

    public static void processRequest(final MinecraftServer server, final NetworkManager networkManager) {
        final StandardPaperServerListPingEventImpl event = new StandardPaperServerListPingEventImpl(server, networkManager, server.getServerStatusResponse());
        server.server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            networkManager.closeChannel(null);
            return;
        }
        final ServerStatusResponse ping = new ServerStatusResponse();
        ping.setServerDescription(new TextComponentString(event.getMotd()));
        if (!event.shouldHidePlayers()) {
            ping.setPlayers(new ServerStatusResponse.Players(event.getMaxPlayers(), event.getNumPlayers()));
            ping.getPlayers().setPlayers(event.getPlayerSampleHandle());
        }
        ping.setVersion(new ServerStatusResponse.Version(event.getVersion(), event.getProtocolVersion()));
        if (event.getServerIcon() != null) {
            ping.setFavicon(event.getServerIcon().getData());
        }
        networkManager.sendPacket(new SPacketServerInfo(ping));
    }

    static {
        EMPTY_PROFILES = new GameProfile[0];
        FAKE_UUID = new UUID(0L, 0L);
    }
}
