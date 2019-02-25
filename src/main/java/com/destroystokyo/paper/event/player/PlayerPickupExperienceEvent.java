package com.destroystokyo.paper.event.player;

import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

 /**
  * Fired when a player is attempting to pick up an experience orb
  */
 public class PlayerPickupExperienceEvent extends PlayerEvent implements Cancellable {
    private final ExperienceOrb experienceOrb;

     public PlayerPickupExperienceEvent(Player player, ExperienceOrb experienceOrb) {
         super(player);
         this.experienceOrb = experienceOrb;
     }

     /**
      * @return Returns the Orb that the player is picking up
      */
     public ExperienceOrb getExperienceOrb() {
         return experienceOrb;
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
      * If true, Cancels picking up the experience orb, leaving it in the world
      * @param cancel true if you wish to cancel this event
      */
     @Override
     public void setCancelled(boolean cancel) {
         cancelled = cancel;
     }
}
