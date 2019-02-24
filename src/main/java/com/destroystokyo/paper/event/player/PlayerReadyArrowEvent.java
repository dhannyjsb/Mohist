package com.destroystokyo.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a player is firing a bow and the server is choosing an arrow to use.
 */
public class PlayerReadyArrowEvent extends PlayerEvent implements Cancellable {
    private final ItemStack bow;
    private final ItemStack arrow;

    public PlayerReadyArrowEvent(Player player, ItemStack bow, ItemStack arrow) {
        super(player);
        this.bow = bow;
        this.arrow = arrow;
    }

    /**
     * @return the player is using to fire the arrow
     */
    public ItemStack getBow() {
        return bow;
    }

    /**
     * @return the arrow that is attempting to be used
     */
    public ItemStack getArrow() {
        return arrow;
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
     * Whether or not use of this arrow is cancelled. On cancel, the server will try the next arrow available and fire another event.
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Cancel use of this arrow. On cancel, the server will try the next arrow available and fire another event.
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
