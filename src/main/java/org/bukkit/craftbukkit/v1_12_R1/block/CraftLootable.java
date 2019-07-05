package org.bukkit.craftbukkit.v1_12_R1.block;

import net.minecraft.tileentity.TileEntityLockableLoot;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.Block;

public abstract class CraftLootable<T extends TileEntityLockableLoot> extends CraftContainer<T> implements Nameable {

    public CraftLootable(Block block, Class<T> tileEntityClass) {
        super(block, tileEntityClass);
    }

    public CraftLootable(Material material, T tileEntity) {
        super(material, tileEntity);
    }

    public CraftLootable(Block block, T tileEntity) {
        super(block, tileEntity);
    }

    @Override
    public String getCustomName() {
        T lootable = this.getSnapshot();
        return lootable.hasCustomName() ? lootable.getName() : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(name);
    }

    @Override
    public void applyTo(T lootable) {
        super.applyTo(lootable);

        if (!this.getSnapshot().hasCustomName()) {
            lootable.setCustomName(null);
        }
    }
}
