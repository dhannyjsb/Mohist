package net.minecraftforge.cauldron.entity;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class CraftCustomEntity extends CraftEntity {

    public String entityName;
    public Class<? extends Entity> entityClass;

    public CraftCustomEntity(CraftServer server, net.minecraft.entity.Entity entity) {
        super(server, entity);
        this.entityName = EntityRegistry.getCustomEntityTypeName(entityClass);
        if (entityName == null) {
            entityName = entity.getName();
        }
    }

    @Override
    public net.minecraft.entity.Entity getHandle() {
        return this.entity;
    }

    @Override
    public String toString() {
        return this.entityName;
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null) {
            return type;
        } else {
            return EntityType.UNKNOWN;
        }
    }

    @Override
    public String getCustomName() {
        final String name = this.getHandle().getCustomNameTag();
        if (name == null || name.length() == 0) {
            return this.entity.getName();
        }
        return name;
    }
}