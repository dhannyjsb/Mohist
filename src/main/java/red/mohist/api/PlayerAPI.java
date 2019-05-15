package red.mohist.api;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Map;

public class PlayerAPI {

    public static Map<EntityPlayerMP,Integer> mods = Maps.newHashMap();

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

    public static EntityPlayerMP getNMSPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    public static int getModSize(Player player) {
        return mods.get(getNMSPlayer(player)) - 4;
    }
}
