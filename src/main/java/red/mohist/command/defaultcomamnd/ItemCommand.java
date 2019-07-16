package red.mohist.command.defaultcomamnd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import red.mohist.api.CustomNameAPI;
import red.mohist.api.ServerAPI;
import red.mohist.i18n.Message;

import java.util.Locale;

public class ItemCommand extends Command {

    public ItemCommand(String name) {
        super(name);
        this.description = "Item commands";
        this.usageMessage = "/item [info]";
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
            case "info":
                // Not recommended for use in games, only test output
                info(sender);
                break;
        }
        return false;
    }

    private void info(CommandSender sender) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            ItemStack itemStack = p.getInventory().getItemInMainHand();
            int id = itemStack.getTypeId();
            String type = itemStack.getType().name();
            String i18nname = CustomNameAPI.getItemName(itemStack);
            int id1 = ServerAPI.injectmaterials.containsKey(type) ? id : ServerAPI.injectmaterials.get(type);
        }
    }
}
