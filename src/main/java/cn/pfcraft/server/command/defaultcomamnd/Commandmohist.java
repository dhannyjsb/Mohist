package cn.pfcraft.server.command.defaultcomamnd;

import cn.pfcraft.server.MohistConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Commandmohist extends Command {

    public Commandmohist(String name) {
        super(name);
        this.description = "Mohist related commands";
        this.usageMessage = "/mohist [heap | reload]";
        this.setPermission("bukkit.command.mohist");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        if (args.length <= 1)
            return CommandBase.getListMatchingLast(args, "heap", "reload");

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
            case "heap":
                dumpHeap(sender);
                break;
            case "reload":
                doReload(sender);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
        }

        return true;
    }

    private void dumpHeap(CommandSender sender) {
        File file = new File(new File(new File("."), "dumps"),
                "heap-dump-" + DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss").format(LocalDateTime.now()) + "-server.hprof");
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW  +"Writing JVM heap data to " + file);
        if (CraftServer.dumpHeap(file)) {
            Command.broadcastCommandMessage(sender, ChatColor.GREEN + "Heap dump complete");
        } else {
            Command.broadcastCommandMessage(sender, ChatColor.RED + "Failed to write heap dump, see sever log for details");
        }
    }

    private void doReload(CommandSender sender) {
        Command.broadcastCommandMessage(sender, ChatColor.RED + "Please note that this command is not supported and may cause issues.");
        Command.broadcastCommandMessage(sender, ChatColor.RED + "If you encounter any issues please use the /stop command to restart your server.");

        MinecraftServer console = MinecraftServer.getServerInst();
       MohistConfig.init((File) console.options.valueOf("mohist-settings"));
        for (WorldServer world : console.worlds) {
            world.pfserverConfig.init();
        }
        console.server.reloadCount++;

        Command.broadcastCommandMessage(sender, ChatColor.GREEN + "mohist config reload complete.");
    }
}
