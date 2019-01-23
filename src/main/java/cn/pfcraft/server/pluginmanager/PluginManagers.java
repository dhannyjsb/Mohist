package cn.pfcraft.server.pluginmanager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public class PluginManagers {

	public static boolean hasPermission(CommandSender sender) {
		if (sender != Bukkit.getServer().getConsoleSender()) {
			if (sender.isOp())
				return true;

			sender.sendMessage("§7Please enter the correct command");
			return false;
		}
		return true;
	}

	public static boolean loadPluginCommand(CommandSender sender, String label, String[] split) {
		if (!hasPermission(sender))
			return true;

		if (split.length < 2) {
			sender.sendMessage(ChatColor.GOLD + "/" + label + " load <plugin> §b- load plugin");
			return true;
		}
        String jarName = split[1] + (split[1].endsWith(".jar") ? "" : ".jar");
        File toLoad = new File("plugins" + File.separator + jarName);

        if (!toLoad.exists()) {
            jarName = split[1] + (split[1].endsWith(".jar") ? ".unloaded" : ".jar.unloaded");
            toLoad = new File("plugins" + File.separator + jarName);
            if (!toLoad.exists()) {
                sender.sendMessage("§cNo files found " + split[1] + ".jar");
                return true;
            } else {
                String fileName = jarName.substring(0, jarName.length() - (".unloaded".length()));
                toLoad = new File("plugins" + File.separator + fileName);
                File unloaded = new File("plugins" + File.separator + jarName);
                unloaded.renameTo(toLoad);
            }
        }

		PluginDescriptionFile desc = Control.getDescription(toLoad);
		if (desc == null) {
            sender.sendMessage("§cjar Not included plugin.yml file");
			return true;
		}
        final Plugin[] pl = Bukkit.getPluginManager().getPlugins();
        ArrayList<Plugin> plugins = new ArrayList<Plugin>(java.util.Arrays.asList(pl));
        for(Plugin p: plugins) {
            if (desc.getName().equals(p.getName())) {
                sender.sendMessage(desc.getName()+"§7Unable to load repeatedly");
                return true;
            }
        }
		Plugin p = null;
		if ((p = Control.loadPlugin(toLoad)) != null) {
			Control.enablePlugin(p);
            sender.sendMessage(p.getDescription().getName()+p.getDescription().getVersion()+"Loaded successfully");
		} else
		sender.sendMessage(split[1]+"Unable to load! (View console for details.)");

		return true;
	}

	public static boolean unloadPluginCommand(final CommandSender sender, final String label, final String[] split) {
		if (!hasPermission(sender))
			return true;

		if (split.length < 2) {
			sender.sendMessage(ChatColor.GOLD + "/" + label + " unload <plugin> §b- Uninstall plugin");
			return true;
		}

		final Plugin p = Bukkit.getServer().getPluginManager().getPlugin(split[1]);

		if (p == null)
			sender.sendMessage("§7No plugins found" + split[1]);
		else {
			if (Control.unloadPlugin(p, true))
				sender.sendMessage(p.getDescription().getName()+p.getDescription().getVersion()+"Uninstall successfully");
			else
				sender.sendMessage(split[1] + "Unable to load! (View console for details.)");
		}

		return true;
	}

	public static boolean reloadPluginCommand(final CommandSender sender, final String label, final String[] split) {
		if (!hasPermission(sender))
			return true;

		if (split.length < 2) {
			sender.sendMessage(ChatColor.GOLD + "/" + label + " reload <plugin> §b- reload plugin");
			return true;
		}

		final Plugin p = Bukkit.getServer().getPluginManager().getPlugin(split[1]);

		if (p == null)
			sender.sendMessage("§7No plugins found" + split[1]);
		else {
			final File file = Control.getFile((JavaPlugin) p);

			if (file == null) {
                sender.sendMessage(p.getName()+"jarFile is missing");
				return true;
			}

			File name = new File("plugins" + File.separator + file.getName());
			JavaPlugin loaded = null;
			if (!Control.unloadPlugin(p, false))
				sender.sendMessage(split[1]+"An error occurred while uninstalling");
			else if ((loaded = (JavaPlugin) Control.loadPlugin(name)) == null)
				sender.sendMessage(split[1]+"An error occurred during overloading");

			Control.enablePlugin(loaded);
			sender.sendMessage(split[1]+"reload success");
		}
		return true;
	}
}
