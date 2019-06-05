package red.mohist.event.server;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BukkitServerStartDoneEvent extends Event{
    private static final HandlerList handlers = new HandlerList();

    public BukkitServerStartDoneEvent() {

    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
