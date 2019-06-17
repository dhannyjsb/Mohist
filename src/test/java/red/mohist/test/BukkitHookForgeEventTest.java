package red.mohist.test;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import red.mohist.event.BukkitHookForgeEvent;
import net.minecraftforge.event.world.ExplosionEvent;

public class BukkitHookForgeEventTest implements Listener {

    /**
     * Using Bukkit to handle Forge's explosionEvent
     *
     * @param event
     */
    @EventHandler
    public void test(BukkitHookForgeEvent event){
        if (event.getEvent() instanceof ExplosionEvent.Detonate) {
            ExplosionEvent.Detonate explosionEvent = (ExplosionEvent.Detonate)event.getEvent();
            explosionEvent.getAffectedBlocks().clear();
        }
    }
}
