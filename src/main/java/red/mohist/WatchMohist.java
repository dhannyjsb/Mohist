package red.mohist;

import net.minecraft.server.MinecraftServer;
import red.mohist.i18n.Message;

import java.util.Timer;
import java.util.TimerTask;

public class WatchMohist extends TimerTask
{
    private static Timer timer;
    private static long Time;
    private static long WarnTime;
    
    @Override
    public void run() {
        long curTime = System.currentTimeMillis();
        if (WatchMohist.Time > 0L && curTime - WatchMohist.Time > 2000L && curTime - WatchMohist.WarnTime > 30000L) {
            WatchMohist.WarnTime = curTime;
            Mohist.LOGGER.warn(Message.getString(Message.WatchMohist_1));
            Mohist.LOGGER.warn(Message.getFormatString(Message.WatchMohist_2, new Object[]{(curTime - WatchMohist.Time)}));
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
        WatchMohist.timer.schedule(new WatchMohist(), 30000L, 500L);
    }
    
    public static void stop() {
        WatchMohist.timer.cancel();
    }
    
    static {
        WatchMohist.timer = new Timer();
        WatchMohist.Time = 0L;
        WatchMohist.WarnTime = 0L;
    }
}
