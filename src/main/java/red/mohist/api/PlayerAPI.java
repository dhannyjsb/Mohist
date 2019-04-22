package red.mohist.api;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerAPI {

    /**
     *  Get Player ping
     *
     * @param player org.bukkit.entity.player
     */
    public static String getPing(Player player){
        CraftPlayer cp = ((CraftPlayer)player);
        int ping = cp.getHandle().ping;
        return String.valueOf(ping);
    }
}
