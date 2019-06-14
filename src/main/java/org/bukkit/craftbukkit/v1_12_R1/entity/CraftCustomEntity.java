package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.EntityType;

public class CraftCustomEntity extends CraftEntity {

    public Class<? extends Entity> entityClass;
    private String entityName;

    public CraftCustomEntity(CraftServer server, net.minecraft.entity.Entity entity) {
        super(server, entity);
        this.entityClass = entity.getClass();
        this.entityName = EntityRegistry.entityTypeMap.get(entity.getClass());
        if (entityName == null) {
            entityName = entity.getName();
        }
    }

    @Override
    public Entity getHandle() {
        return (Entity) entity;
    }

    @Override
    public String toString() {
        return this.entityName;
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null)
            return type;
        else return EntityType.FORGE_MOD;
    }

    @Override
    public String getCustomName() {
        String name = getHandle().getCustomNameTag();

        if (name == null || name.length() == 0) {
            return this.entity.getName();
        }

        return name;
    }
}