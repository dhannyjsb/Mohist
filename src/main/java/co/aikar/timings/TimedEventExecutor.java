package co.aikar.timings;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

public class TimedEventExecutor implements EventExecutor {

    private final EventExecutor executor;
    private final Timing timings;

    /**
      * Wraps an event executor and associates a timing handler to it.
      *
      * @param executor Executor to wrap
      * @param plugin Owning plugin
      * @param method EventHandler method
      * @param eventClass Owning class
      */
    public TimedEventExecutor(EventExecutor executor, Plugin plugin, Method method, Class<? extends Event> eventClass) {
        this.executor = executor;
        String id;

                if (method == null) {
                if (executor.getClass().getEnclosingClass() != null) { // Oh Skript, how we love you
                        method = executor.getClass().getEnclosingMethod();
                    }
            }

                if (method != null) {
                id = method.getDeclaringClass().getName();
            } else {
                id = executor.getClass().getName();
            }

        
                        final String eventName = eventClass.getSimpleName();
        boolean verbose = "BlockPhysicsEvent".equals(eventName);
        this.timings = Timings.ofSafe(plugin.getName(), (verbose ? "## " : "") +
                    "Event: " + id + " (" + eventName + ")", null);
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        if (event.isAsynchronous() || !Timings.timingsEnabled || !Bukkit.isPrimaryThread()) {
                executor.execute(listener, event);
                return;
            }
        timings.startTiming();
        executor.execute(listener, event);
        timings.stopTiming();
    }
}