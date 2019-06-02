package org.spigotmc;

import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import red.mohist.Mohist;
import red.mohist.i18n.Message;

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
                log.error( Message.getString(Message.Watchdog_One) );
                log.error( Message.getString(Message.Watchdog_Two) );
                log.error( Message.getString(Message.Watchdog_Three) );
                log.error( Message.getString(Message.Watchdog_Four) + Mohist.getVersion() );
                //
                if(net.minecraft.world.World.haveWeSilencedAPhysicsCrash)
                {
                    log.error( "------------------------------" );
                    log.error( Message.getString(Message.Watchdog_Five) );
                    log.error( Message.getString(Message.Watchdog_Six) + net.minecraft.world.World.blockLocation );
                }
                log.error( "------------------------------" );
                log.error( Message.getString(Message.Watchdog_Seven) );
                dumpThread( ManagementFactory.getThreadMXBean().getThreadInfo( MinecraftServer.getServerInst().primaryThread.getId(), Integer.MAX_VALUE ), log );
                log.error( "------------------------------" );
                //
                log.error( Message.getString(Message.Watchdog_Eight) );
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
        log.error( Message.getString(Message.Watchdog_Nine) + thread.getThreadName() );
        log.error( "\t" + Message.getString(Message.Watchdog_Ten) + thread.getThreadId()
                + " | " + Message.getString(Message.Watchdog_Eleven) + thread.isSuspended()
                + " | " + Message.getString(Message.Watchdog_Twelve) + thread.isInNative()
                + " | " + Message.getString(Message.Watchdog_Thirteen) + thread.getThreadState() );
        if ( thread.getLockedMonitors().length != 0 )
        {
            log.error( "\t" + Message.getString(Message.Watchdog_Sixteen) );
            for ( MonitorInfo monitor : thread.getLockedMonitors() )
            {
                log.error( "\t\t" + Message.getString(Message.Watchdog_Fourteen) + monitor.getLockedStackFrame() );
            }
        }
        log.error( "\t" + Message.getString(Message.Watchdog_Fifteen) );
        //
        for ( StackTraceElement stack : thread.getStackTrace() )
        {
            log.error( "\t\t" + stack );
        }
    }
}
