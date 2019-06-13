package org.bukkit.craftbukkit.v1_12_R1.inventory;

import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.IInventory;
import org.bukkit.Location;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryAnvil extends CraftInventory implements AnvilInventory {

    private final Location location;
    private final IInventory resultInventory;
    private final ContainerRepair container;

    public CraftInventoryAnvil(Location location, IInventory inventory, IInventory resultInventory, ContainerRepair container) {
        super(inventory);
        this.location = location;
        this.resultInventory = resultInventory;
        this.container = container;
    }

    public IInventory getResultInventory() {
        return resultInventory;
    }

    public IInventory getIngredientsInventory() {
        return inventory;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < inventory.getSizeInventory()) {
            net.minecraft.item.ItemStack item = inventory.getStackInSlot(slot);
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        } else {
            net.minecraft.item.ItemStack item = resultInventory.getStackInSlot(slot - inventory.getSizeInventory());
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < inventory.getSizeInventory()) {
            inventory.setInventorySlotContents(index, CraftItemStack.asNMSCopy(item));
        } else {
            resultInventory.setInventorySlotContents((index - inventory.getSizeInventory()), CraftItemStack.asNMSCopy(item));
        }
    }

    @Override
    public int getSize() {
        return resultInventory.getSizeInventory() + inventory.getSizeInventory();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public String getRenameText() {
        return container.repairedItemName;
    }

    @Override
    public int getRepairCost() {
        return container.maximumCost;
    }

    @Override
    public void setRepairCost(int i) {
        container.maximumCost = i;
    }
}
