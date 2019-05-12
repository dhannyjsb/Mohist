package red.mohist.command.defaultcomamnd;

import net.minecraft.command.CommandBase;
import net.minecraft.server.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;
import red.mohist.MohistConfig;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Commandmohist extends Command {

    public Commandmohist(String name) {
        super(name);
        this.description = "Mohist related commands";
        this.usageMessage = "/mohist [reload|potions]";
        this.setPermission("bukkit.command.mohist");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        if (args.length <= 1)
            return CommandBase.getListOfStringsMatchingLastWord(args, "reload", "potions");

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
            case "potions":
                getPotions(sender);
                break;
            case "enchants":
                getEnchant(sender);
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

    private void getPotions(CommandSender sender) {
        for(PotionEffectType pet : PotionEffectType.values()){
            sender.sendMessage(pet.toString());
        }
    }

    private void getEnchant(CommandSender sender) {
        for(Enchantment ench : Enchantment.values()){
            sender.sendMessage(ench.toString());
        }
    }
}
