package org.spigotmc;

import co.aikar.timings.Timing;
import co.aikar.timings.Timings;
import co.aikar.timings.TimingsManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.plugin.Plugin;
import sun.reflect.Reflection;

import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * Provides custom timing sections for /timings merged.
 */
public class CustomTimingsHandler
{
    private final Timing handler;

    public CustomTimingsHandler(String name) {
        Timing timing;
        Plugin plugin = null;
        try {
            plugin = TimingsManager.getPluginByClassloader(Reflection.getCallerClass(2));
        } catch (Exception ignored) {}
        new AuthorNagException("Deprecated use of CustomTimingsHandler. Please Switch to Timings.of ASAP").printStackTrace();
        if (plugin != null) {
                timing = Timings.of(plugin, "(Deprecated API) " + name);
            } else {
                try {
                        final Method ofSafe = TimingsManager.class.getMethod("getHandler", String.class, String.class, Timing.class, boolean.class);
                        timing = (Timing) ofSafe.invoke("Minecraft", "(Deprecated API) " + name, null, true);
                    } catch (Exception e) {
                        Bukkit.getLogger().log(Level.SEVERE, "This handler could not be registered");
                        timing = Timings.NULL_HANDLER;
            }
        }
        handler = timing;
    }

    public void startTiming() { handler.startTiming(); }
    public void stopTiming() { handler.stopTiming(); }

}
