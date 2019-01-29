package com.destroystokyo.paper.entity;

import org.bukkit.entity.LivingEntity;

/**
 * Used to determine ACTUAL Living NPC's. Spigot mistakenly inversed the conditions for LivingEntity, and
 * used LivingEntity for Insentient Entities, and named the actual EntityLiving class EntityInsentient.
 *
 * This should of all been inversed on the implementation side. To make matters worse, Spigot never
 * exposed the differentiator that there are entities with AI that are not sentient/alive such as
 * Armor stands and Players are the only things that do not implement the REAL EntityLiving class (named Insentient internally)
 *
 * This interface lets you identify NPC entities capable of sentience, and able to move about and react to the world.
 */
public interface SentientNPC extends LivingEntity {

    /**
     * Instructs this Creature to set the specified LivingEntity as its
     * target.
     * <p>
     * Hostile creatures may attack their target, and friendly creatures may
     * follow their target.
     *
     * @param target New LivingEntity to target, or null to clear the target
     */
    public void setTarget(LivingEntity target);

    /**
     * Gets the current target of this Creature
     *
     * @return Current target of this creature, or null if none exists
     */
    public LivingEntity getTarget();
}
