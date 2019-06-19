package red.mohist.api;

import com.google.common.collect.Maps;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerAPI {

    public static Map<EntityPlayerMP,Integer> mods = new ConcurrentHashMap<EntityPlayerMP,Integer>();
    public static Map<EntityPlayerMP,String> modlist= new ConcurrentHashMap<EntityPlayerMP,String>();

    /**
     *  Get Player ping
     *
     * @param player org.bukkit.entity.player
     */
    public static String getPing(Player player){
        int ping = getNMSPlayer(player).ping;
        return String.valueOf(ping);
    }

    public static EntityPlayerMP getNMSPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    public static int getModSize(Player player) {
        return mods.get(getNMSPlayer(player)) == null ? 0 : mods.get(getNMSPlayer(player)) - 4;
    }

    public static String getModlist(Player player) {
        return modlist.get(getNMSPlayer(player)) == null ? "null" : modlist.get(getNMSPlayer(player));
    }
}
