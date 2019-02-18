package com.destroystokyo.paper.entity;

import net.minecraft.entity.EntityLiving;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public interface CraftSentientNPC extends SentientNPC {
    EntityLiving getHandle();

    default void setTarget(LivingEntity target) {
        EntityLiving entity = this.getHandle();
        if (target == null) {
            entity.setGoalTarget(null, null, false);
        } else if (target instanceof CraftLivingEntity) {
            entity.setGoalTarget(((CraftLivingEntity)target).getHandle(), null, false);
        }

    }

    default LivingEntity getTarget() {
        return this.getHandle().getAttackTarget() == null ? null : (CraftLivingEntity)this.getHandle().getAttackTarget().getBukkitEntity();
    }
}
