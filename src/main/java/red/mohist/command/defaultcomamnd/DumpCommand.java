package red.mohist.command.defaultcomamnd;

import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import red.mohist.i18n.Message;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DumpCommand extends Command {
    public DumpCommand(String name) {
        super(name);
        this.description = "Universal Dump, which will print the information you need locally!";
        this.usageMessage = "/mohist *";
    }

    private List<String> params = Arrays.asList("potions", "enchants", "commands", "entitytypes", "biomes");

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> list = new ArrayList<>();
        if (args.length == 1 && sender.isOp()) {
            for (String param : params) {
                if (param.toLowerCase().startsWith(args[0].toLowerCase())) {
                    list.add(param);
                }
            }
        }

        return list;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Message.getString("command.nopermission"));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "potions":
                dumpPotions(sender);
                break;
            case "enchants":
                dumpEnchant(sender);
                break;
            case "commands":
                dumpCommands(sender);
                break;
            case "entitytypes":
                dumpEntityTypes(sender);
                break;
            case "biomes":
                dumpBiomes(sender);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
        }
        return false;
    }

    public static void dumpPotions(CommandSender sender){
        StringBuilder sb = new StringBuilder();
        for (PotionEffectType pet : PotionEffectType.values()) {
            if (pet != null) {
                sb.append(pet.toString() + "\n");
            }
        }
        try{
            FileUtils.writeByteArrayToFile(new File("dump", "potions.mo"), sb.toString().getBytes(StandardCharsets.UTF_8));
            sender.sendMessage("Ok");
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void dumpEnchant(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for (Enchantment ench : Enchantment.values()) {
            sb.append(ench.toString() + "\n");
        }
        try {
            FileUtils.writeByteArrayToFile(new File("dump", "enchant.mo"), sb.toString().getBytes(StandardCharsets.UTF_8));
            sender.sendMessage("Ok");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void dumpEntityTypes(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for (EntityType ent : EntityType.values()) {
            sb.append(ent.toString() + "\n");
        }
        try {
            FileUtils.writeByteArrayToFile(new File("dump", "entitytypes.mo"), sb.toString().getBytes(StandardCharsets.UTF_8));
            sender.sendMessage("Ok");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void dumpCommands(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for (Command per : MinecraftServer.getServerInst().server.getCommandMap().getCommands()) {
            sb.append(per.getName() + ": " + per.getPermission() + "\n");
        }
        try {
            FileUtils.writeByteArrayToFile(new File("dump", "commands.mo"), sb.toString().getBytes(StandardCharsets.UTF_8));
            sender.sendMessage("Ok");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void dumpBiomes(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for (Biome biome : Biome.values()) {
            sb.append(biome.toString() + "\n");
        }
        try {
            FileUtils.writeByteArrayToFile(new File("dump", "biomes.mo"), sb.toString().getBytes(StandardCharsets.UTF_8));
            sender.sendMessage("Ok");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
