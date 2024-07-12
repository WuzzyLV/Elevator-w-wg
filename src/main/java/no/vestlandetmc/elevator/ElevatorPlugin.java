package no.vestlandetmc.elevator;

import lombok.Getter;
import no.vestlandetmc.elevator.Listener.ElevatorListener;
import no.vestlandetmc.elevator.Listener.TeleporterListener;
import no.vestlandetmc.elevator.commands.ElevatorCommand;
import no.vestlandetmc.elevator.commands.TabCompleteArgs;
import no.vestlandetmc.elevator.commands.TeleporterCommand;
import no.vestlandetmc.elevator.config.Config;
import no.vestlandetmc.elevator.config.TeleporterData;
import no.vestlandetmc.elevator.handler.MessageHandler;
import no.vestlandetmc.elevator.handler.UpdateNotification;
import no.vestlandetmc.elevator.handler.VersionHandler;
import no.vestlandetmc.elevator.hooks.GriefDefenderHook;
import no.vestlandetmc.elevator.hooks.GriefPreventionHook;
import no.vestlandetmc.elevator.hooks.WorldGuardHook;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ElevatorPlugin extends JavaPlugin {

	@Getter
	private static ElevatorPlugin plugin;
	@Getter
	private static VersionHandler versionHandler;

	private FileConfiguration data;

	@Override
	public void onEnable() {
		plugin = this;
		versionHandler = new VersionHandler();

		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "___________ __                       __                ");
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "\\_   _____/|  |   _______  _______ _/  |_  ___________ ");
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + " |    __)_ |  | _/ __ \\  \\/ /\\__  \\\\   __\\/  _ \\_  __ \\");
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + " |        \\|  |_\\  ___/\\   /  / __ \\|  | (  <_> )  | \\/");
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "/_______  /|____/\\___  >\\_/  (____  /__|  \\____/|__|   ");
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "        \\/           \\/           \\/                   ");
		getServer().getConsoleSender().sendMessage("");
		getServer().getConsoleSender().sendMessage("Elevator v" + getDescription().getVersion());
		getServer().getConsoleSender().sendMessage("Running on " + getServer().getName());
		getServer().getConsoleSender().sendMessage("Authors: " + getDescription().getAuthors().toString().replace("[", "").replace("]", "").replace(",", " and"));
		getServer().getConsoleSender().sendMessage("");

		Config.initialize();
		createDatafile();

		GriefPreventionHook.gpSearch();
		WorldGuardHook.wgSearch();
		GriefDefenderHook.gdSearch();

		TeleporterData.createSection();

		if (GriefPreventionHook.gpHook) {
			MessageHandler.sendConsole("&7[Elevator] Successfully hooked into &9GriefPrevention");
		}

		if (WorldGuardHook.wgHook) {
			MessageHandler.sendConsole("&7[Elevator] Successfully hooked into &9WorldGuard");
		}

		if (GriefDefenderHook.gdHook) {
			MessageHandler.sendConsole("&7[Elevator] Successfully hooked into &9GriefDefender");
		}

		this.getServer().getPluginManager().registerEvents(new ElevatorListener(), this);
		this.getServer().getPluginManager().registerEvents(new TeleporterListener(), this);
		this.getCommand("elevator").setExecutor(new ElevatorCommand());
		this.getCommand("teleporter").setExecutor(new TeleporterCommand());
		this.getCommand("teleporter").setTabCompleter(new TabCompleteArgs());

		new UpdateNotification(67723) {

			@Override
			public void onUpdateAvailable() {
				getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "-----------------------");
				getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[Elevator] Version " + getLatestVersion() + " is now available!");
				getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[Elevator] Download the update at https://www.spigotmc.org/resources/" + getProjectId());
				getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "-----------------------");
			}
		}.runTaskAsynchronously(this);

		final int pluginId = 22614;
		final Metrics metrics = new Metrics(this, pluginId);
	}

	public void reload() {
		Config.initialize();
	}

	public FileConfiguration getDataFile() {
		return this.data;
	}

	public void createDatafile() {
		File dataFile = new File(this.getDataFolder(), "data.dat");
		if (!dataFile.exists()) {
			dataFile.getParentFile().mkdirs();
			try {
				dataFile.createNewFile();
			} catch (final IOException e) {
				getLogger().severe(e.getMessage());
			}
		}

		data = new YamlConfiguration();
		try {
			data.load(dataFile);
		} catch (IOException | InvalidConfigurationException e) {
			getLogger().severe(e.getMessage());
		}
	}
}
