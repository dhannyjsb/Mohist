package org.bukkit.craftbukkit.v1_12_R1.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;

/**
 * Represents input from a command block
 */
public class CraftBlockCommandSender extends ServerCommandSender implements BlockCommandSender {
    private final ICommandSender block;

    public CraftBlockCommandSender(ICommandSender commandBlockListenerAbstract) {
        super();
        this.block = commandBlockListenerAbstract;
    }

    @Override
    public Block getBlock() {
        return block.getEntityWorld().getWorld().getBlockAt(block.getPosition().getX(), block.getPosition().getY(), block.getPosition().getZ());
    }

    @Override
    public void sendMessage(String message) {
        for (ITextComponent component : CraftChatMessage.fromString(message)) {
            block.sendMessage(component);
        }
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    public String getName() {
        return block.getName();
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a block");
    }

    public ICommandSender getTileEntity() {
        return block;
    }
}
