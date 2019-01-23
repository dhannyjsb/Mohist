package cn.pfcraft.server.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.*;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nullable;

public class CustomInventory implements InventoryHolder {

    private final IInventory inventory;
    private final CraftInventory container;

    public CustomInventory(IInventory inventory) {
        this.container = new CraftInventory(inventory);
        this.inventory = inventory;
    }

    public CustomInventory(ItemStackHandler handler) {
        this.container = new CraftInventoryCustom(this, handler.getStacksList());
        this.inventory = this.container.getInventory();
    }

    public CustomInventory(InventoryPlayer playerInventory) {
        this.container = new CraftInventoryPlayer(playerInventory);
        this.inventory = playerInventory;
    }

    @Override
    public Inventory getInventory() {
        return this.container;
    }

    @Nullable
    public static InventoryHolder holderFromForge(final IItemHandler handler) {
        if (handler == null) {
            return null;
        }
        if (handler instanceof ItemStackHandler) {
            return new CustomInventory((ItemStackHandler)handler);
        }
        if (handler instanceof SlotItemHandler) {
            return new CustomInventory(((SlotItemHandler)handler).inventory);
        }
        if (handler instanceof InvWrapper) {
            return new CustomInventory(((InvWrapper)handler).getInv());
        }
        if (handler instanceof SidedInvWrapper) {
            return new CustomInventory((InventoryPlayer) ReflectionHelper.getPrivateValue(SidedInvWrapper.class, (SidedInvWrapper)handler, "inv"));
        }
        if (handler instanceof PlayerInvWrapper) {
            return new CustomInventory(getPlayerInv((PlayerInvWrapper)handler));
        }
        return null;
    }

    @Nullable
    public static Inventory inventoryFromForge(IItemHandler handler) {
        InventoryHolder holder = holderFromForge(handler);
        return holder != null ? holder.getInventory() : null;
    }

    public static InventoryPlayer getPlayerInv(final PlayerInvWrapper handler) {
        final IItemHandlerModifiable[] array;
        final IItemHandlerModifiable[] itemHandlers = array = ReflectionHelper.getPrivateValue(CombinedInvWrapper.class, handler, "itemHandler");
        for (final IItemHandlerModifiable itemHandler : array) {
            if (itemHandler instanceof PlayerMainInvWrapper) {
                return ((PlayerMainInvWrapper)itemHandler).getInventoryPlayer();
            }
            if (itemHandler instanceof PlayerArmorInvWrapper) {
                return ((PlayerArmorInvWrapper)itemHandler).getInventoryPlayer();
            }
        }
        return null;
    }
}
