package red.mohist.command.defaultcomamnd;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
public class Commandunban extends Command {

    public Commandunban(String name) {
        super(name);
        this.description = "Mohist related commands";
        this.usageMessage = "/unban [playername]";
        this.setPermission("bukkit.command.unban");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
        if (!p.isBanned()) {
            sender.sendMessage(ChatColor.RED + "该玩家没有被ban");
        }
        Bukkit.getServer().getBanList(BanList.Type.NAME).pardon(p.getName());
        return true;
    }
}
