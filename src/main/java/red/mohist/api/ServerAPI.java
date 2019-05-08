package red.mohist.api;

import java.util.HashMap;

public class ServerAPI {

    public static HashMap<String, Integer> mods = new HashMap();

    public static int getModSize() {
        return mods.get("mods") - 4;
    }
}
