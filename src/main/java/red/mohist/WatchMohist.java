package red.mohist;

import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.bukkit.Bukkit;
import red.mohist.i18n.Message;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang.StringUtils.join;
import static org.spigotmc.TicksPerSecondCommand.format;

public class WatchMohist implements Runnable {

    private static ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("WatchMohist").daemon(true).build());
    private static long Time = 0L;
    private static long WarnTime = 0L;
    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    {
        threadMXBean.setThreadCpuTimeEnabled(true);
    }

    public static void update() {
        WatchMohist.Time = System.currentTimeMillis();
    }

    public static void start() {
        timer.scheduleAtFixedRate(new WatchMohist(), 48000L, 600L, TimeUnit.MILLISECONDS);
    }

    public static void stop() {
        timer.shutdown();
    }

    private void dumpThreadCpuTime() {
        List<ThreadCpuTime> list = new ArrayList<>();
        long[] ids = threadMXBean.getAllThreadIds();
        for (long id : ids) {
            ThreadCpuTime item = new ThreadCpuTime();
            item.cpuTime = threadMXBean.getThreadCpuTime(id) / 1000000;
            item.userTime = threadMXBean.getThreadUserTime(id) / 1000000;
            item.name = threadMXBean.getThreadInfo(id).getThreadName();
            item.id = id;
            list.add(item);
        }
        list.sort(Comparator.comparingLong(i -> i.id));
        System.out.println("=============== thread cpu cost ===============");
        for (ThreadCpuTime threadCpuTime : list) {
            System.out.println(String.format("%s %s %s %s", threadCpuTime.id, threadCpuTime.name, threadCpuTime.cpuTime, threadCpuTime.userTime));
        }
    }

    @Override
    public void run() {
        if (MohistConfig.printThreadTimeCost) {
            dumpThreadCpuTime();
        }
        long curTime = System.currentTimeMillis();
        if (WatchMohist.Time > 0L && curTime - WatchMohist.Time > 2400L && curTime - WatchMohist.WarnTime > 48000L && String.valueOf(curTime - WatchMohist.Time).contains("-")) {
            WatchMohist.WarnTime = curTime;
            Mohist.LOGGER.debug(Message.getString(Message.WatchMohist_1));

            double[] tps = Bukkit.getTPS();
            String[] tpsAvg = new String[tps.length];
            for (int i = 0; i < tps.length; i++) {
                tpsAvg[i] = format(tps[i]);
            }

            Mohist.LOGGER.debug(Message.getFormatString(Message.WatchMohist_2, new Object[]{String.valueOf(curTime - WatchMohist.Time), join(tpsAvg, ", ")}));
            Mohist.LOGGER.debug(Message.getString(Message.WatchMohist_3));
            for (StackTraceElement stack : MinecraftServer.getServerInst().primaryThread.getStackTrace()) {
                Mohist.LOGGER.debug(Message.getString(Message.WatchMohist_4) + stack);
            }
            Mohist.LOGGER.debug(Message.getString(Message.WatchMohist_1));
        }
    }

    public static class ThreadCpuTime {
        private long id;
        private long cpuTime;
        private long userTime;
        private String name;
    }
}