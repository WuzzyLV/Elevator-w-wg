package no.vestlandetmc.elevator;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import no.vestlandetmc.elevator.Listener.ElevatorListener;
import no.vestlandetmc.elevator.Listener.TeleporterListener;
import no.vestlandetmc.elevator.commands.ElevatorCommand;
import no.vestlandetmc.elevator.commands.TabCompleteArgs;
import no.vestlandetmc.elevator.commands.TeleporterCommand;
import no.vestlandetmc.elevator.config.Config;
import no.vestlandetmc.elevator.handler.MessageHandler;
import no.vestlandetmc.elevator.handler.UpdateNotification;
import no.vestlandetmc.elevator.hooks.GriefPreventionHook;
import no.vestlandetmc.elevator.hooks.WorldGuardHook;

public class ElevatorPlugin extends JavaPlugin {

	private static ElevatorPlugin instance;
	private File dataFile;
	private FileConfiguration data;

	public static ElevatorPlugin getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;

		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "___________ __                       __                ");
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "\\_   _____/|  |   _______  _______ _/  |_  ___________ ");
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + " |    __)_ |  | _/ __ \\  \\/ /\\__  \\\\   __\\/  _ \\_  __ \\");
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + " |        \\|  |_\\  ___/\\   /  / __ \\|  | (  <_> )  | \\/");
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "/_______  /|____/\\___  >\\_/  (____  /__|  \\____/|__|   ");
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "        \\/           \\/           \\/                   ");
		getServer().getConsoleSender().sendMessage("");
		getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "Elevator v" + getDescription().getVersion());
		getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "Running on " + getServer().getName());
		getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "Authors: " + getDescription().getAuthors().toString().replace("[", "").replace("]", "").replace(",", " and"));
		getServer().getConsoleSender().sendMessage("");

		Config.initialize();
		createDatafile();
		GriefPreventionHook.gpSearch();
		WorldGuardHook.wgSearch();

		if(GriefPreventionHook.gpHook) {
			MessageHandler.sendConsole("&7[Elevator] Successfully hooked into &9GriefPrevention");
		}

		if(WorldGuardHook.wgHook) {
			MessageHandler.sendConsole("&7[Elevator] Successfully hooked into &9WorldGuard");
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
	}

	public void reload() {
		Config.initialize();
	}

	public FileConfiguration getDataFile() {
		return this.data;
	}

	public void createDatafile() {
		dataFile = new File(this.getDataFolder(), "data.dat");
		if (!dataFile.exists()) {
			dataFile.getParentFile().mkdirs();
			try {
				dataFile.createNewFile();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		data = new YamlConfiguration();
		try {
			data.load(dataFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}
