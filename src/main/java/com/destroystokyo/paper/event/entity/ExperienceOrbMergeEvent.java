package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

/**
  * Fired anytime the server is about to merge 2 experience orbs into one
  */
public class ExperienceOrbMergeEvent extends EntityEvent implements Cancellable {
    private final ExperienceOrb mergeTarget;
    private final ExperienceOrb mergeSource;

    public ExperienceOrbMergeEvent(ExperienceOrb mergeTarget, ExperienceOrb mergeSource) {
        super(mergeTarget);
        this.mergeTarget = mergeTarget;
        this.mergeSource = mergeSource;
    }

    /**
      * @return The orb that will absorb the other experience orb
      */
    public ExperienceOrb getMergeTarget() {
        return mergeTarget;
    }

    /**
      * @return The orb that is subject to being removed and merged into the target orb
      */
    public ExperienceOrb getMergeSource() {
        return mergeSource;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
      * @param cancel true if you wish to cancel this event, and prevent the orbs from merging
      */
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
