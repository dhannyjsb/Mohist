package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.EntityHanging;
import net.minecraft.util.EnumFacing;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;

public class CraftHanging extends CraftEntity implements Hanging {
    public CraftHanging(CraftServer server, EntityHanging entity) {
        super(server, entity);
    }

    @Override
    public BlockFace getAttachedFace() {
        return getFacing().getOppositeFace();
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        setFacingDirection(face, false);
    }

    @Override
    public boolean setFacingDirection(BlockFace face, boolean force) {
        EntityHanging hanging = getHandle();
        EnumFacing dir = hanging.facingDirection;
        switch (face) {
            case SOUTH:
            default:
                getHandle().updateFacingWithBoundingBox(EnumFacing.SOUTH);
                break;
            case WEST:
                getHandle().updateFacingWithBoundingBox(EnumFacing.WEST);
                break;
            case NORTH:
                getHandle().updateFacingWithBoundingBox(EnumFacing.NORTH);
                break;
            case EAST:
                getHandle().updateFacingWithBoundingBox(EnumFacing.EAST);
                break;
        }
        if (!force && !hanging.onValidSurface()) {
            // Revert since it doesn't fit
            hanging.updateFacingWithBoundingBox(dir);
            return false;
        }
        return true;
    }

    @Override
    public BlockFace getFacing() {
        EnumFacing direction = this.getHandle().facingDirection;
        if (direction == null) return BlockFace.SELF;
        switch (direction) {
            case SOUTH:
            default:
                return BlockFace.SOUTH;
            case WEST:
                return BlockFace.WEST;
            case NORTH:
                return BlockFace.NORTH;
            case EAST:
                return BlockFace.EAST;
        }
    }

    @Override
    public EntityHanging getHandle() {
        return (EntityHanging) entity;
    }

    @Override
    public String toString() {
        return "CraftHanging";
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
