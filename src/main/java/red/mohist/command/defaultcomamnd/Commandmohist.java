package red.mohist.command.defaultcomamnd;

import net.minecraft.command.CommandBase;
import net.minecraft.server.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import red.mohist.MohistConfig;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Commandmohist extends Command {

    public Commandmohist(String name) {
        super(name);
        this.description = "Mohist related commands";
        this.usageMessage = "/mohist [potions|enchants|materials]";
        this.setPermission("bukkit.command.mohist");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        if (args.length <= 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "potions", "enchants", "materials");
        }

        return Collections.emptyList();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        switch (args[0].toLowerCase(Locale.ENGLISH))  {
            case "potions":
                // Not recommended for use in games, only test output
                getPotions(sender);
                break;
            case "enchants":
                // Not recommended for use in games, only test output
                getEnchant(sender);
                break;
            case "materials":
                // Not recommended for use in games, only test output
                getMaterials(sender);
                break;
            case "entitytypes":
                // Not recommended for use in games, only test output
                getEntityTypes(sender);
                break;
            case "commands":
                // Not recommended for use in games, only test output
                getCommands(sender);
                break;
            case "biomes":
                // Not recommended for use in games, only test output
                getBiomes(sender);
                break;
            case "reload":
                MohistConfig.reload();
                sender.sendMessage(ChatColor.GREEN + "Config reload success");
            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
        }

        return true;
    }

    private void getPotions(CommandSender sender) {
        for(PotionEffectType pet : PotionEffectType.values()){
            if(pet != null) {
                sender.sendMessage(pet.toString());
            }
        }
    }

    private void getEnchant(CommandSender sender) {
        for(Enchantment ench : Enchantment.values()){
            sender.sendMessage(ench.toString());
        }
    }

    private void getMaterials(CommandSender sender) {
        for(Material mat : Material.values()){
            sender.sendMessage(mat.toString());
        }
    }

    private void getEntityTypes(CommandSender sender) {
        for(EntityType ent : EntityType.values()){
            sender.sendMessage(ent.toString());
        }
    }

    private void getCommands(CommandSender sender){
        for (Command per : MinecraftServer.getServerInst().server.getCommandMap().getCommands()) {
            sender.sendMessage(per.getName());
        }
    }

    private void getBiomes(CommandSender sender){
        for (Biome biome : Biome.values()) {
            sender.sendMessage(biome.name());
        }
    }
}
