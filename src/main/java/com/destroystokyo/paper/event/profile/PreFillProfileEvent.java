package com.destroystokyo.paper.event.profile;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Fired when the server is requesting to fill in properties of an incomplete profile, such as textures.
 *
 * Allows plugins to pre populate cached properties and avoid a call to the Mojang API
 */
public class PreFillProfileEvent extends Event {
    private final PlayerProfile profile;

    public PreFillProfileEvent(PlayerProfile profile) {
        super(!org.bukkit.Bukkit.isPrimaryThread());
        this.profile = profile;
    }

    /**
     * @return The profile that needs its properties filled
     */
    public PlayerProfile getPlayerProfile() {
        return profile;
    }

    /**
     * Sets the properties on the profile, avoiding the call to the Mojang API
     * Same as .getPlayerProfile().setProperties(properties);
     * 
     * @see PlayerProfile#setProperties(Collection)
     * @param properties The properties to set/append
     */
    public void setProperties(@Nonnull Collection<ProfileProperty> properties) {
        profile.setProperties(properties);
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
