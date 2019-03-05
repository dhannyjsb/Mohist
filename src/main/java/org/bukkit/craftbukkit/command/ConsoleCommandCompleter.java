package org.bukkit.craftbukkit.command;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import com.google.common.collect.ImmutableList;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.event.server.TabCompleteEvent;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ConsoleCommandCompleter implements Completer {
    private final CraftServer server;

    public ConsoleCommandCompleter(CraftServer server) {
        this.server = server;
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        final CraftServer server = this.server.getServer().server;
        final String buffer = line.line();
        List<String> completions = new ArrayList<String>();
        final AsyncTabCompleteEvent event = new AsyncTabCompleteEvent(server.getConsoleSender(), completions, buffer, true, null);
        event.callEvent();
        completions = (List<String>)(event.isCancelled() ? ImmutableList.of() : event.getCompletions());
        if (event.isCancelled() || event.isHandled()) {
            if (!event.isCancelled() && TabCompleteEvent.getHandlerList().getRegisteredListeners().length > 0) {
                final List<String> finalCompletions = completions;
                final Waitable<List<String>> syncCompletions = new Waitable<List<String>>() {
                    @Override
                    protected List<String> evaluate() {
                        final TabCompleteEvent syncEvent = new TabCompleteEvent(server.getConsoleSender(), buffer, finalCompletions);
                        return (List<String>)(syncEvent.callEvent() ? syncEvent.getCompletions() : ImmutableList.of());
                    }
                };
                server.getServer().processQueue.add(syncCompletions);
                try {
                    completions = syncCompletions.get();
                }
                catch (InterruptedException | ExecutionException e1) {
                    e1.printStackTrace();
                }
            }
            if (!completions.isEmpty()) {
                candidates.addAll(completions.stream().map(Candidate::new).collect(java.util.stream.Collectors.toList()));
            }
            return;
        }
        final Waitable<List<String>> waitable = new Waitable<List<String>>() {
            @Override
            protected List<String> evaluate() {
                final List<String> offers = server.getCommandMap().tabComplete(server.getConsoleSender(), buffer);
                final TabCompleteEvent tabEvent = new TabCompleteEvent(server.getConsoleSender(), buffer, (offers == null) ? Collections.EMPTY_LIST : offers);
                server.getPluginManager().callEvent(tabEvent);
                return tabEvent.isCancelled() ? Collections.EMPTY_LIST : tabEvent.getCompletions();
            }
        };
        server.getServer().processQueue.add(waitable);
        try {
            final List<String> offers = waitable.get();
            if (offers == null) {
                return;
            }
            for (final String completion : offers) {
                if (completion.isEmpty()) {
                    continue;
                }
                candidates.add(new Candidate(completion));
            }
        }
        catch (ExecutionException e2) {
            server.getLogger1().warn( "Unhandled exception when tab completing", e2);
        }
        catch (InterruptedException e3) {
            Thread.currentThread().interrupt();
        }
    }
}
