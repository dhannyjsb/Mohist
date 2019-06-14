package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.EnumDyeColor;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;

public class CraftWolf extends CraftTameableAnimal implements Wolf {
    public CraftWolf(CraftServer server, EntityWolf wolf) {
        super(server, wolf);
    }

    @Override
    public boolean isAngry() {
        return getHandle().isAngry();
    }

    @Override
    public void setAngry(boolean angry) {
        getHandle().setAngry(angry);
    }

    @Override
    public EntityWolf getHandle() {
        return (EntityWolf) entity;
    }

    @Override
    public EntityType getType() {
        return EntityType.WOLF;
    }

    @Override
    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte) getHandle().getCollarColor().getMetadata());
    }

    @Override
    public void setCollarColor(DyeColor color) {
        getHandle().setCollarColor(EnumDyeColor.byMetadata(color.getWoolData()));
    }
}
