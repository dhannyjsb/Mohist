package red.mohist.event.player;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UseUnknowCommand extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final CommandSender commandSender;
    private final String useCommand;
    private final String[] commandArgs;

    public UseUnknowCommand(CommandSender commandSender, String command, String[] commandArgs) {
        this.useCommand = command;
        this.commandArgs = commandArgs;
        this.commandSender = commandSender;
    }


    @Override
    public String toString() {
        return "UseUnknowCommand{" + "commandSender=" + this.commandSender + ", useCommande=" + this.useCommand + ", commandArgs=" + this.commandArgs + '}';
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
