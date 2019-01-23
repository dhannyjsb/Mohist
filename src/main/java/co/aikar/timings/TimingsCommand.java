package co.aikar.timings;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;


public class TimingsCommand extends BukkitCommand {
    private static final List<String> TIMINGS_SUBCOMMANDS = ImmutableList.of("report", "reset", "on", "off", "paste", "verbon", "verboff");
    private long lastResetAttempt = 0;

    public TimingsCommand(String name) {
        super(name);
        this.description = "Manages Spigot Timings data to see performance of the server.";
        this.usageMessage = "/timings <reset|report|on|off|verbon|verboff>";
        this.setPermission("bukkit.command.timings");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return true;
        }
        final String arg = args[0];
        if ("on".equalsIgnoreCase(arg)) {
            Timings.setTimingsEnabled(true);
            sender.sendMessage("Enabled Timings & Reset");
            return true;
        } else if ("off".equalsIgnoreCase(arg)) {
            Timings.setTimingsEnabled(false);
            sender.sendMessage("Disabled Timings");
            return true;
        }

            if (!Timings.isTimingsEnabled()) {
            sender.sendMessage("Please enable timings by typing /timings on");
            return true;
        }

            long now = System.currentTimeMillis();
        if ("verbon".equalsIgnoreCase(arg)) {
            Timings.setVerboseTimingsEnabled(true);
            sender.sendMessage("Enabled Verbose Timings");
            return true;
        } else if ("verboff".equalsIgnoreCase(arg)) {
            Timings.setVerboseTimingsEnabled(false);
            sender.sendMessage("Disabled Verbose Timings");
            return true;
        } else if ("reset".equalsIgnoreCase(arg)) {
            if (now - lastResetAttempt < 30000) {
                    TimingsManager.reset();
                    sender.sendMessage(ChatColor.RED + "Timings reset. Please wait 5-10 minutes before using /timings report.");
                } else {
                    lastResetAttempt = now;
                    sender.sendMessage(ChatColor.RED + "WARNING: Timings v2 should not be reset. If you are encountering lag, please wait 3 minutes and then issue a report. The best timings will include 10+ minutes, with data before and after your lag period. If you really want to reset, run this command again within 30 seconds.");
                }

                } else if ("cost".equals(arg)) {
            sender.sendMessage("Timings cost: " + TimingsExport.getCost());
        } else  if (
                "paste".equalsIgnoreCase(arg) ||
                            "report".equalsIgnoreCase(arg) ||
                            "get".equalsIgnoreCase(arg) ||
                            "merged".equalsIgnoreCase(arg) ||
                            "separate".equalsIgnoreCase(arg)
                                ) {
            Timings.generateReport(sender);
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

            if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], TIMINGS_SUBCOMMANDS,
                        new ArrayList<String>(TIMINGS_SUBCOMMANDS.size()));
        }
        return ImmutableList.of();
    }
}