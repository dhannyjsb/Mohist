package com.destroystokyo.paper.entity;

import org.bukkit.entity.LivingEntity;

public interface SentientNPC extends LivingEntity {

    public void setTarget(LivingEntity target);

    public LivingEntity getTarget();
}
