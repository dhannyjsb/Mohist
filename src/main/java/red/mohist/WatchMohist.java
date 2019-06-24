package red.mohist;

import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import red.mohist.i18n.Message;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang.StringUtils.*;
import static org.spigotmc.TicksPerSecondCommand.format;

public class WatchMohist implements Runnable {

    private static ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(1);
    private static long Time = 0L;
    private static long WarnTime = 0L;
    
    @Override
    public void run() {
        long curTime = System.currentTimeMillis();
        if (WatchMohist.Time > 0L && curTime - WatchMohist.Time > 2000L && curTime - WatchMohist.WarnTime > 30000L) {
            WatchMohist.WarnTime = curTime;
            Mohist.LOGGER.warn(Message.getString(Message.WatchMohist_1));

            double[] tps = Bukkit.getTPS();
            String[] tpsAvg = new String[tps.length];
            for ( int i = 0; i < tps.length; i++ ) {
                tpsAvg[i] = format( tps[i] );
            }

            Mohist.LOGGER.warn(Message.getFormatString(Message.WatchMohist_2, new Object[]{ String.valueOf(curTime - WatchMohist.Time), join(tpsAvg, ", ") }));
            Mohist.LOGGER.warn(Message.getString(Message.WatchMohist_3));
            for (StackTraceElement stack : MinecraftServer.getServerInst().primaryThread.getStackTrace()) {
                Mohist.LOGGER.warn(Message.getString(Message.WatchMohist_4) + stack);
            }
            Mohist.LOGGER.warn(Message.getString(Message.WatchMohist_1));
        }
    }

    public static void update() {
        WatchMohist.Time = System.currentTimeMillis();
    }
    
    public static void start() {
        timer.scheduleAtFixedRate(new WatchMohist(), 30000L, 500L, TimeUnit.MILLISECONDS);
    }
    
    public static void stop() {
        timer.shutdown();
    }
}