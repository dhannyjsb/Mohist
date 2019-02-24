package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

/**
 * Fired when an Enderman determines if it should attack a player or not.
 * Starts off cancelled if the player is wearing a pumpkin head or is not looking
 * at the Enderman, according to Vanilla rules.
 *
 */
public class EndermanAttackPlayerEvent extends EntityEvent implements Cancellable {
    private final Player player;

    public EndermanAttackPlayerEvent(Enderman entity, Player player) {
        super(entity);
        this.player = player;
    }

    /**
     * The enderman considering attacking
     *
     * @return The enderman considering attacking
     */
    @Override public Enderman getEntity() {

        return (Enderman) super.getEntity();
    }

    /**
     * The player the Enderman is considering attacking
     *
     * @return The player the Enderman is considering attacking
     */
    public Player getPlayer() {

        return player;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {

        return handlers;
    }

    public static HandlerList getHandlerList() {

        return handlers;
    }

    private boolean cancelled = false;

    /**
     *
     * @return If cancelled, the enderman will not attack
     */
    @Override
    public boolean isCancelled() {

        return cancelled;
    }

    /**
     * Cancels if the Enderman will attack this player
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {

        cancelled = cancel;
    }
}
