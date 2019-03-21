package cn.pfcraft.command.defaultcomamnd;

import cn.pfcraft.MohistConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Commandmohist extends Command {

    public Commandmohist(String name) {
        super(name);
        this.description = "Mohist related commands";
        this.usageMessage = "/mohist [reload]";
        this.setPermission("bukkit.command.mohist");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        if (args.length <= 1)
            return CommandBase.getListMatchingLast(args, "reload");

        return Collections.emptyList();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        switch (args[0].toLowerCase(Locale.ENGLISH))  {
            case "reload":
                doReload(sender);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
        }

        return true;
    }

    private void doReload(CommandSender sender) {
        Command.broadcastCommandMessage(sender, ChatColor.RED + "Please note that this command is not supported and may cause issues.");
        Command.broadcastCommandMessage(sender, ChatColor.RED + "If you encounter any issues please use the /stop command to restart your server.");

        MinecraftServer console = MinecraftServer.getServerInst();
        MohistConfig.init((File) console.options.valueOf("mohist-settings"));
        console.server.reloadCount++;

        Command.broadcastCommandMessage(sender, ChatColor.GREEN + "mohist config reload complete.");
    }
}
