
package org.bukkit.craftbukkit.v1_12_R1.command;

import net.minecraft.command.ICommandSender;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class ProxiedNativeCommandSender implements ProxiedCommandSender {

    private final ICommandSender orig;
    private final CommandSender caller;
    private final CommandSender callee;

    public ProxiedNativeCommandSender(ICommandSender orig, CommandSender caller, CommandSender callee) {
        this.orig = orig;
        this.caller = caller;
        this.callee = callee;
    }

    public ICommandSender getHandle() {
        return orig;
    }

    @Override
    public CommandSender getCaller() {
        return caller;
    }

    @Override
    public CommandSender getCallee() {
        return callee;
    }

    @Override
    public void sendMessage(String message) {
        caller.sendMessage(message);
    }

    @Override
    public void sendMessage(String[] messages) {
        caller.sendMessage(messages);
    }

    @Override
    public Server getServer() {
        return callee.getServer();
    }

    @Override
    public String getName() {
        return callee.getName();
    }

    @Override
    public boolean isPermissionSet(String name) {
        return caller.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return caller.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return caller.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return caller.hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return caller.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return caller.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return caller.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return caller.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        caller.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        caller.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return caller.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return caller.isOp();
    }

    @Override
    public void setOp(boolean value) {
        caller.setOp(value);
    }

    // Spigot start
    @Override
    public Spigot spigot()
    {
        return caller.spigot();
    }
    // Spigot end
}
