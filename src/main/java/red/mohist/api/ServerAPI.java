package red.mohist.api;

import io.netty.util.internal.ConcurrentSet;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerAPI {

    public static Map<String, Integer> mods = new ConcurrentHashMap();
    public static Set<String> modlists= new ConcurrentSet();

    public static int getModSize() {
        return mods.get("mods") == null ? 0 : mods.get("mods") - 4;
    }

    public static String getModList() {
        return modlists.toString();
    }
}
