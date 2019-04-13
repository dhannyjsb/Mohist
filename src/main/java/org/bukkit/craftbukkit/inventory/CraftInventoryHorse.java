package org.bukkit.craftbukkit.inventory;

import net.minecraft.inventory.IInventory;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SaddledHorseInventory;

public class CraftInventoryHorse extends CraftInventoryAbstractHorse implements HorseInventory, SaddledHorseInventory {

    public CraftInventoryHorse(IInventory inventory) {
        super(inventory);
    }

    public ItemStack getArmor() {
       return getItem(1);
    }

    public void setArmor(ItemStack stack) {
        setItem(1, stack);
    }
}
