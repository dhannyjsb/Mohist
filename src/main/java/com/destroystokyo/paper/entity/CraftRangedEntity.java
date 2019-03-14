package com.destroystokyo.paper.entity;

import net.minecraft.entity.IRangedAttackMob;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public interface CraftRangedEntity<T extends IRangedAttackMob> extends RangedEntity {
    T getHandle();

    @Override
    default void rangedAttack(LivingEntity target, float charge) {
        getHandle().attackEntityWithRangedAttack(((CraftLivingEntity) target).getHandle(), charge);
    }

    @Override
    default void setChargingAttack(boolean raiseHands) {
        getHandle().setSwingingArms(raiseHands);
    }
}
