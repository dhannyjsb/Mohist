package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;

public class CraftSheep extends CraftAnimals implements Sheep {
    public CraftSheep(CraftServer server, EntitySheep entity) {
        super(server, entity);
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData((byte) getHandle().getFleeceColor().getMetadata());
    }

    @Override
    public void setColor(DyeColor color) {
        getHandle().setFleeceColor(EnumDyeColor.byMetadata(color.getWoolData()));
    }

    @Override
    public boolean isSheared() {
        return getHandle().getSheared();
    }

    @Override
    public void setSheared(boolean flag) {
        getHandle().setSheared(flag);
    }

    @Override
    public EntitySheep getHandle() {
        return (EntitySheep) entity;
    }

    @Override
    public String toString() {
        return "CraftSheep";
    }

    @Override
    public EntityType getType() {
        return EntityType.SHEEP;
    }
}
