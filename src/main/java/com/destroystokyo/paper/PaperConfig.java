package com.destroystokyo.paper;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.spigotmc.SpigotConfig;
import org.spigotmc.WatchdogThread;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class PaperConfig
{
    public static final Logger LOGGER = LogManager.getLogger("Paper");
    private static File CONFIG_FILE;
    private static final String HEADER = "This is the main configuration file for Paper.\nAs you can see, there's tons to configure. Some options may impact gameplay, so use\nwith caution, and make sure you know what each option does before configuring.\n\nIf you need help with the configuration or have any questions related to Paper,\njoin us in our IRC channel.\n\nIRC: #paper @ irc.spi.gt ( http://irc.spi.gt/iris/?channels=paper )\nWiki: https://paper.readthedocs.org/ \nPaper Forums: https://aquifermc.org/ \n";
    public static YamlConfiguration config;
    static int version;
    static Map<String, Command> commands;
    private static boolean verbose;
    private static boolean metricsStarted;
    private static final Pattern SPACE;
    private static final Pattern NOT_NUMERIC;
    public static int minChunkLoadThreads;
    public static boolean enableFileIOThreadSleep;
    public static boolean loadPermsBeforePlugins;
    public static int regionFileCacheSize;
    public static boolean enablePlayerCollisions;
    public static boolean saveEmptyScoreboardTeams;
    public static boolean bungeeOnlineMode;
    public static int packetInSpamThreshold;
    public static String flyingKickPlayerMessage;
    public static String flyingKickVehicleMessage;
    public static int playerAutoSaveRate;
    public static int maxPlayerAutoSavePerTick;
    public static boolean removeInvalidStatistics;
    public static boolean suggestPlayersWhenNullTabCompletions;
    public static String authenticationServersDownKickMessage;
    public static boolean savePlayerData;
    public static boolean useAlternativeLuckFormula;
    public static int watchdogPrintEarlyWarningEvery;
    public static int watchdogPrintEarlyWarningDelay;
    public static int tabSpamIncrement;
    public static int tabSpamLimit;
    public static int maxBookPageSize;
    public static double maxBookTotalSizeMultiplier;

    public static void init(final File configFile) {
        PaperConfig.CONFIG_FILE = configFile;
        PaperConfig.config = new YamlConfiguration();
        try {
            PaperConfig.config.load(PaperConfig.CONFIG_FILE);
        }
        catch (IOException ex2) {}
        catch (InvalidConfigurationException ex) {
            Bukkit.getLogger1().error( "Could not load paper.yml, please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }
        PaperConfig.config.options().header("This is the main configuration file for Paper.\nAs you can see, there's tons to configure. Some options may impact gameplay, so use\nwith caution, and make sure you know what each option does before configuring.\n\nIf you need help with the configuration or have any questions related to Paper,\njoin us in our IRC channel.\n\nIRC: #paper @ irc.spi.gt ( http://irc.spi.gt/iris/?channels=paper )\nWiki: https://paper.readthedocs.org/ \nPaper Forums: https://aquifermc.org/ \n");
        PaperConfig.config.options().copyDefaults(true);
        PaperConfig.verbose = getBoolean("verbose", false);
        (PaperConfig.commands = new HashMap<String, Command>()).put("paper", new PaperCommand("paper"));
        PaperConfig.version = getInt("config-version", 13);
        set("config-version", 13);
        readConfig(PaperConfig.class, null);
    }

    protected static void logError(final String s) {
        LOGGER.error(s);
    }

    protected static void log(final String s) {
        if (PaperConfig.verbose) {
            LOGGER.info(s);
        }
    }

    public static void registerCommands() {
        for (final Map.Entry<String, Command> entry : PaperConfig.commands.entrySet()) {
            MinecraftServer.getServerInst().server.getCommandMap().register(entry.getKey(), "Paper", entry.getValue());
        }
    }

    static void readConfig(final Class<?> clazz, final Object instance) {
        for (final Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers()) && method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                try {
                    method.setAccessible(true);
                    method.invoke(instance, new Object[0]);
                }
                catch (InvocationTargetException ex) {
                    throw Throwables.propagate(ex.getCause());
                }
                catch (Exception ex2) {
                    LOGGER.error( "Error invoking " + method, ex2);
                }
            }
        }
        try {
            PaperConfig.config.save(PaperConfig.CONFIG_FILE);
        }
        catch (IOException ex3) {
            LOGGER.error( "Could not save " + PaperConfig.CONFIG_FILE, ex3);
        }
    }

    public static int getSeconds(String str) {
        str = PaperConfig.SPACE.matcher(str).replaceAll("");
        final char unit = str.charAt(str.length() - 1);
        str = PaperConfig.NOT_NUMERIC.matcher(str).replaceAll("");
        double num;
        try {
            num = Double.parseDouble(str);
        }
        catch (Exception e) {
            num = 0.0;
        }
        switch (unit) {
            case 'd': {
                num *= 86400.0;
                break;
            }
            case 'h': {
                num *= 3600.0;
                break;
            }
            case 'm': {
                num *= 60.0;
                break;
            }
        }
        return (int)num;
    }

    protected static String timeSummary(int seconds) {
        String time = "";
        if (seconds > 86400) {
            time = time + TimeUnit.SECONDS.toDays(seconds) + "d";
            seconds %= 86400;
        }
        if (seconds > 3600) {
            time = time + TimeUnit.SECONDS.toHours(seconds) + "h";
            seconds %= 3600;
        }
        if (seconds > 0) {
            time = time + TimeUnit.SECONDS.toMinutes(seconds) + "m";
        }
        return time;
    }

    private static void set(final String path, final Object val) {
        PaperConfig.config.set(path, val);
    }

    private static boolean getBoolean(final String path, final boolean def) {
        PaperConfig.config.addDefault(path, def);
        return PaperConfig.config.getBoolean(path, PaperConfig.config.getBoolean(path));
    }

    private static double getDouble(final String path, final double def) {
        PaperConfig.config.addDefault(path, def);
        return PaperConfig.config.getDouble(path, PaperConfig.config.getDouble(path));
    }

    private static float getFloat(final String path, final float def) {
        return (float)getDouble(path, def);
    }

    private static int getInt(final String path, final int def) {
        PaperConfig.config.addDefault(path, def);
        return PaperConfig.config.getInt(path, PaperConfig.config.getInt(path));
    }

    private static <T> List getList(final String path, final T def) {
        PaperConfig.config.addDefault(path, def);
        return PaperConfig.config.getList(path, PaperConfig.config.getList(path));
    }

    private static String getString(final String path, final String def) {
        PaperConfig.config.addDefault(path, def);
        return PaperConfig.config.getString(path, PaperConfig.config.getString(path));
    }

    private static void chunkLoadThreads() {
        PaperConfig.minChunkLoadThreads = Math.min(6, getInt("settings.min-chunk-load-threads", 2));
    }

    private static void enableFileIOThreadSleep() {
        PaperConfig.enableFileIOThreadSleep = getBoolean("settings.sleep-between-chunk-saves", false);
        if (PaperConfig.enableFileIOThreadSleep) {
            Bukkit.getLogger().info("Enabled sleeping between chunk saves, beware of memory issues");
        }
    }

    private static void loadPermsBeforePlugins() {
        PaperConfig.loadPermsBeforePlugins = getBoolean("settings.load-permissions-yml-before-plugins", true);
    }

    private static void regionFileCacheSize() {
        PaperConfig.regionFileCacheSize = getInt("settings.region-file-cache-size", 256);
    }

    private static void enablePlayerCollisions() {
        PaperConfig.enablePlayerCollisions = getBoolean("settings.enable-player-collisions", true);
    }

    private static void saveEmptyScoreboardTeams() {
        PaperConfig.saveEmptyScoreboardTeams = getBoolean("settings.save-empty-scoreboard-teams", false);
    }

    private static void bungeeOnlineMode() {
        PaperConfig.bungeeOnlineMode = getBoolean("settings.bungee-online-mode", true);
    }

    private static void packetInSpamThreshold() {
        if (PaperConfig.version < 11) {
            final int oldValue = getInt("settings.play-in-use-item-spam-threshold", 300);
            set("settings.incoming-packet-spam-threshold", oldValue);
        }
        PaperConfig.packetInSpamThreshold = getInt("settings.incoming-packet-spam-threshold", 300);
    }

    private static void flyingKickMessages() {
        PaperConfig.flyingKickPlayerMessage = getString("messages.kick.flying-player", PaperConfig.flyingKickPlayerMessage);
        PaperConfig.flyingKickVehicleMessage = getString("messages.kick.flying-vehicle", PaperConfig.flyingKickVehicleMessage);
    }

    private static void removeInvalidStatistics() {
        if (PaperConfig.version < 12) {
            final boolean oldValue = getBoolean("remove-invalid-statistics", false);
            set("settings.remove-invalid-statistics", oldValue);
        }
        PaperConfig.removeInvalidStatistics = getBoolean("settings.remove-invalid-statistics", false);
    }

    private static void suggestPlayersWhenNull() {
        PaperConfig.suggestPlayersWhenNullTabCompletions = getBoolean("settings.suggest-player-names-when-null-tab-completions", PaperConfig.suggestPlayersWhenNullTabCompletions);
    }

    private static void authenticationServersDownKickMessage() {
        PaperConfig.authenticationServersDownKickMessage = Strings.emptyToNull(getString("messages.kick.authentication-servers-down", PaperConfig.authenticationServersDownKickMessage));
    }

    private static void savePlayerData() {
        if (!(PaperConfig.savePlayerData = getBoolean("settings.save-player-data", PaperConfig.savePlayerData))) {
            Bukkit.getLogger1().warn( "Player Data Saving is currently disabled. Any changes to your players data, such as inventories, experience points, advancements and the like will not be saved when they log out.");
        }
    }

    private static void useAlternativeLuckFormula() {
        PaperConfig.useAlternativeLuckFormula = getBoolean("settings.use-alternative-luck-formula", false);
        if (PaperConfig.useAlternativeLuckFormula) {
            Bukkit.getLogger1().info( "Using Aikar's Alternative Luck Formula to apply Luck attribute to all loot pool calculations. See https://luckformula.emc.gs");
        }
    }

    private static void watchdogEarlyWarning() {
        PaperConfig.watchdogPrintEarlyWarningEvery = getInt("settings.watchdog.early-warning-every", 5000);
        PaperConfig.watchdogPrintEarlyWarningDelay = getInt("settings.watchdog.early-warning-delay", 10000);
        WatchdogThread.doStart(SpigotConfig.timeoutTime, SpigotConfig.restartOnCrash);
    }

    private static void tabSpamLimiters() {
        PaperConfig.tabSpamIncrement = getInt("settings.spam-limiter.tab-spam-increment", PaperConfig.tabSpamIncrement);
        PaperConfig.tabSpamLimit = getInt("settings.spam-limiter.tab-spam-limit", PaperConfig.tabSpamLimit);
    }

    private static void maxBookSize() {
        PaperConfig.maxBookPageSize = getInt("settings.book-size.page-max", PaperConfig.maxBookPageSize);
        PaperConfig.maxBookTotalSizeMultiplier = getDouble("settings.book-size.total-multiplier", PaperConfig.maxBookTotalSizeMultiplier);
        if (maxBookPageSize == 1024 && maxBookTotalSizeMultiplier == 0.90D) {
            config.set("settings.book-size.page-max", 2560);
            config.set("settings.book-size.total-multiplier", 0.98D);
            maxBookPageSize = 2560;
            maxBookTotalSizeMultiplier = 0.98D;
        }
    }

    static {
        SPACE = Pattern.compile(" ");
        NOT_NUMERIC = Pattern.compile("[^-\\d.]");
        PaperConfig.minChunkLoadThreads = 2;
        PaperConfig.loadPermsBeforePlugins = true;
        PaperConfig.regionFileCacheSize = 256;
        PaperConfig.enablePlayerCollisions = true;
        PaperConfig.saveEmptyScoreboardTeams = false;
        PaperConfig.bungeeOnlineMode = true;
        PaperConfig.packetInSpamThreshold = 300;
        PaperConfig.flyingKickPlayerMessage = "Flying is not enabled on this server";
        PaperConfig.flyingKickVehicleMessage = "Flying is not enabled on this server";
        PaperConfig.removeInvalidStatistics = false;
        PaperConfig.suggestPlayersWhenNullTabCompletions = true;
        PaperConfig.authenticationServersDownKickMessage = "";
        PaperConfig.savePlayerData = true;
        PaperConfig.useAlternativeLuckFormula = false;
        PaperConfig.watchdogPrintEarlyWarningEvery = 5000;
        PaperConfig.watchdogPrintEarlyWarningDelay = 10000;
        PaperConfig.tabSpamIncrement = 10;
        PaperConfig.tabSpamLimit = 500;
        PaperConfig.maxBookPageSize = 2560;
        PaperConfig.maxBookTotalSizeMultiplier = 0.98D;
    }
}
