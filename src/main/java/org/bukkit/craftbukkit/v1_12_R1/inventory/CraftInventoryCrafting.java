package org.bukkit.craftbukkit.v1_12_R1.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Arrays;
import java.util.List;

public class CraftInventoryCrafting extends CraftInventory implements CraftingInventory {
    private final IInventory resultInventory;

    public CraftInventoryCrafting(InventoryCrafting inventory, IInventory resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }

    public IInventory getResultInventory() {
        return resultInventory;
    }

    public IInventory getMatrixInventory() {
        return inventory;
    }

    @Override
    public int getSize() {
        return resultInventory.getSizeInventory() + inventory.getSizeInventory();
    }

    @Override
    public void setContents(ItemStack[] items) {
        if (getSize() > items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getSize() + " or less");
        }
        setContents(items[0], Arrays.copyOfRange(items, 1, items.length));
    }

    @Override
    public ItemStack[] getContents() {
        ItemStack[] items = new ItemStack[getSize()];
        List<net.minecraft.item.ItemStack> mcResultItems = resultInventory.getContents();

        int i = 0;
        for (i = 0; i < mcResultItems.size(); i++ ) {
            items[i] = CraftItemStack.asCraftMirror(mcResultItems.get(i));
        }

        List<net.minecraft.item.ItemStack> mcItems = inventory.getContents();

        for (int j = 0; j < mcItems.size(); j++) {
            items[i + j] = CraftItemStack.asCraftMirror(mcItems.get(j));
        }

        return items;
    }

    public void setContents(ItemStack result, ItemStack[] contents) {
        setResult(result);
        setMatrix(contents);
    }

    @Override
    public CraftItemStack getItem(int index) {
        if (index < resultInventory.getSizeInventory()) {
            net.minecraft.item.ItemStack item = resultInventory.getStackInSlot(index);
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        } else {
            net.minecraft.item.ItemStack item = inventory.getStackInSlot(index - resultInventory.getSizeInventory());
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < resultInventory.getSizeInventory()) {
            resultInventory.setInventorySlotContents(index, CraftItemStack.asNMSCopy(item));
        } else {
            inventory.setInventorySlotContents((index - resultInventory.getSizeInventory()), CraftItemStack.asNMSCopy(item));
        }
    }

    @Override
    public ItemStack[] getMatrix() {
        List<net.minecraft.item.ItemStack> matrix = inventory.getContents();

        return asCraftMirror(matrix);
    }

    @Override
    public ItemStack getResult() {
        net.minecraft.item.ItemStack item = resultInventory.getStackInSlot(0);
        if (!item.isEmpty()) return CraftItemStack.asCraftMirror(item);
        return null;
    }

    @Override
    public void setMatrix(ItemStack[] contents) {
        if (inventory.getSizeInventory() > contents.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + inventory.getSizeInventory() + " or less");
        }

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (i < contents.length) {
                inventory.setInventorySlotContents(i, CraftItemStack.asNMSCopy(contents[i]));
            } else {
                inventory.setInventorySlotContents(i, net.minecraft.item.ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void setResult(ItemStack item) {
        List<net.minecraft.item.ItemStack> contents = resultInventory.getContents();
        contents.set(0, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public Recipe getRecipe() {
        IRecipe recipe = ((InventoryCrafting)getInventory()).currentRecipe;
        // Cauldron start - handle custom recipe classes without Bukkit API equivalents
        try {
            return recipe == null ? null : recipe.toBukkitRecipe();
        } catch (AbstractMethodError ex) {
            return new CraftCustomModRecipe(recipe, recipe.getRegistryName());
        }
        // Cauldron end
    }
}
