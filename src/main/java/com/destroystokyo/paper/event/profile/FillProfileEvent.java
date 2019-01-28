package com.destroystokyo.paper.event.profile;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import java.util.Set;

/**
* Fired once a profiles additional properties (such as textures) has been filled
*/
public class FillProfileEvent extends Event {
    private final PlayerProfile profile;

    public FillProfileEvent(@Nonnull PlayerProfile profile) {
        super(!org.bukkit.Bukkit.isPrimaryThread());
        this.profile = profile;
    }

    /**
    * @return The Profile that had properties filled
    */
    @Nonnull
    public PlayerProfile getPlayerProfile() {
        return profile;
    }

    /**
    * Same as .getPlayerProfile().getProperties()
    *
    * @see PlayerProfile#getProperties()
    * @return The new properties on the profile.r
    */
    public Set<ProfileProperty> getProperties() {
        return profile.getProperties();
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
