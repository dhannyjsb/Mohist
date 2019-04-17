package org.spigotmc;

import cn.pfcraft.Mohist;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;

public class WatchdogThread extends Thread
{

    private static WatchdogThread instance;
    private final long timeoutTime;
    private final boolean restart;
    private volatile long lastTick;
    private volatile boolean stopping;

    private WatchdogThread(long timeoutTime, boolean restart)
    {
        super( "Spigot Watchdog Thread" );
        this.timeoutTime = timeoutTime;
        this.restart = restart;
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
            if ( lastTick != 0 && System.currentTimeMillis() > lastTick + timeoutTime && !Boolean.getBoolean("disable.watchdog"))
            {
                Logger log = Mohist.LOGGER;
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
                log.error( "------------------------------" );
                log.error( "Server thread dump (Look for plugins here before reporting to Spigot!):" );
                dumpThread( ManagementFactory.getThreadMXBean().getThreadInfo( MinecraftServer.getServerInst().primaryThread.getId(), Integer.MAX_VALUE ), log );
                log.error( "------------------------------" );
                //
                log.error( "Entire Thread Dump:" );
                ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads( true, true );
                for ( ThreadInfo thread : threads )
                {
                    dumpThread( thread, log );
                }
                log.error( "------------------------------" );

                if ( restart )
                {
                    MinecraftServer.getServerInst().primaryThread.stop();
                }
                break;
            }

            try
            {
                sleep( 10000 );
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
