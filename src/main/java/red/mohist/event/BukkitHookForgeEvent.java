package red.mohist.event;

import net.minecraftforge.fml.common.eventhandler.Event;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class BukkitHookForgeEvent extends org.bukkit.event.Event {
    private static final HandlerList handlers = new HandlerList();
    private final Event event;

    public BukkitHookForgeEvent(Event event) {
        super(!Bukkit.getServer().isPrimaryThread());
        this.event = event;
    }

    /**
     * @return Forge and Mods Event
     */
    public Event getEvent() {
        return this.event;
    }

    public String getEventName() {
        return event.getClass().getSimpleName();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
