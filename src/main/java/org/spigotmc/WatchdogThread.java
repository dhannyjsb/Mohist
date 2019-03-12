package org.spigotmc;

import cn.pfcraft.Mohist;
import com.destroystokyo.paper.PaperConfig;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;

import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;

public class WatchdogThread extends Thread
{

    private static WatchdogThread instance;
    private final long timeoutTime;
    private final long earlyWarningEvery; // Paper - Timeout time for just printing a dump but not restarting
    private final long earlyWarningDelay; // Paper
    public static volatile boolean hasStarted; // Paper
    private long lastEarlyWarning; // Paper - Keep track of short dump times to avoid spamming console with short dumps
    private final boolean restart;
    private volatile long lastTick;
    private volatile boolean stopping;

    private WatchdogThread(long timeoutTime, boolean restart)
    {
        super( "Spigot Watchdog Thread" );
        this.timeoutTime = timeoutTime;
        this.restart = restart;
        earlyWarningEvery = Math.min(PaperConfig.watchdogPrintEarlyWarningEvery, timeoutTime); // Paper
        earlyWarningDelay = Math.min(PaperConfig.watchdogPrintEarlyWarningDelay, timeoutTime); // Paper
    }

    public static void doStart(int timeoutTime, boolean restart)
    {
        if ( instance == null )
        {
            instance = new WatchdogThread( timeoutTime * 1000L, restart );
            instance.start();
        }
    }

    public static void tick()
    {
        instance.lastTick = System.currentTimeMillis();
    }

    public static void doStop()
    {
        if ( instance != null )
        {
            instance.stopping = true;
        }
    }

    @Override
    public void run()
    {
        while ( !stopping )
        {
            //
            long currentTime = System.currentTimeMillis(); // Paper - do we REALLY need to call this method multiple times?
            if ( lastTick != 0 && currentTime > lastTick + earlyWarningEvery && !Boolean.getBoolean("disable.watchdog") ) // Paper - Add property to disable and short timeout
            {
                // Paper start
                boolean isLongTimeout = currentTime > lastTick + timeoutTime;
                // Don't spam early warning dumps
                if ( !isLongTimeout && (earlyWarningEvery <= 0 || !hasStarted || currentTime < lastEarlyWarning + earlyWarningEvery || currentTime < lastTick + earlyWarningDelay)) continue;
                lastEarlyWarning = currentTime;
                // Paper end
                Logger log = Bukkit.getServer().getLogger1();
                // Paper start - Different message when it's a short timeout
                if ( isLongTimeout )
                {
                log.error( "The server has stopped responding!" );
                log.error( "Please report this to https://github.com/PFCraft/Mohist" );
                log.error( "Be sure to include ALL relevant console errors and Minecraft crash reports" );
                log.error( "Mohist version: " + Mohist.getVersion() );
                //
                if(net.minecraft.world.World.haveWeSilencedAPhysicsCrash)
                {
                    log.error( "------------------------------" );
                    log.error( "During the run of the server, a physics stackoverflow was supressed" );
                    log.error( "near " + net.minecraft.world.World.blockLocation);
                }
                // Paper start - Warn in watchdog if an excessive velocity was ever set
                if ( org.bukkit.craftbukkit.CraftServer.excessiveVelEx != null )
                {
                    log.error( "------------------------------" );
                    log.error( "During the run of the server, a plugin set an excessive velocity on an entity" );
                    log.error( "This may be the cause of the issue, or it may be entirely unrelated" );
                    log.error( org.bukkit.craftbukkit.CraftServer.excessiveVelEx.getMessage());
                    for ( StackTraceElement stack : org.bukkit.craftbukkit.CraftServer.excessiveVelEx.getStackTrace() )
                    {
                        log.error( "\t\t" + stack );
                    }
                }
                // Paper end
                } else
                    {
                        log.error( "--- DO NOT REPORT THIS TO PAPER - THIS IS NOT A BUG OR A CRASH ---");
                        log.error( "The server has not responded for " + (currentTime - lastTick) / 1000 + " seconds! Creating thread dump");
                    }
                // Paper end - Different message for short timeout
                log.error( "------------------------------" );
                log.error( "Server thread dump (Look for plugins here before reporting to Spigot!):" );
                dumpThread( ManagementFactory.getThreadMXBean().getThreadInfo( MinecraftServer.getServerInst().primaryThread.getId(), Integer.MAX_VALUE ), log );
                log.error( "------------------------------" );
                //
                // Paper start - Only print full dump on long timeouts
                if ( isLongTimeout )
                {
                log.error( "Entire Thread Dump:" );
                ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads( true, true );
                for ( ThreadInfo thread : threads )
                {
                    dumpThread( thread, log );
                }
                } else {
                    log.error( "--- DO NOT REPORT THIS TO PAPER - THIS IS NOT A BUG OR A CRASH ---");
                }
                log.error( "------------------------------" );

                if ( isLongTimeout )
                {
                if ( restart )
                {
                    MinecraftServer.getServerInst().primaryThread.stop();
                }
                break;
                } // Paper end
            }

            try
            {
                sleep( 1000 );
            } catch ( InterruptedException ex )
            {
                interrupt();
            }
        }
    }

    private static void dumpThread(ThreadInfo thread, Logger log)
    {
        log.error( "------------------------------" );
        //
        log.error( "Current Thread: " + thread.getThreadName() );
        log.error( "\tPID: " + thread.getThreadId()
                + " | Suspended: " + thread.isSuspended()
                + " | Native: " + thread.isInNative()
                + " | State: " + thread.getThreadState() );
        if ( thread.getLockedMonitors().length != 0 )
        {
            log.error( "\tThread is waiting on monitor(s):" );
            for ( MonitorInfo monitor : thread.getLockedMonitors() )
            {
                log.error( "\t\tLocked on:" + monitor.getLockedStackFrame() );
            }
        }
        log.error( "\tStack:" );
        //
        for ( StackTraceElement stack : thread.getStackTrace() )
        {
            log.error( "\t\t" + stack );
        }
    }
}
