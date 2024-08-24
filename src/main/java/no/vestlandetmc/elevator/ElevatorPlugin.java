package no.vestlandetmc.elevator;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
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
import no.vestlandetmc.elevator.handler.WGHandler;
import no.vestlandetmc.elevator.hooks.HookManager;
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

		MessageHandler.sendConsole("&b___________ __                       __                ");
		MessageHandler.sendConsole("&b\\_   _____/|  |   _______  _______ _/  |_  ___________ ");
		MessageHandler.sendConsole("&b |    __)_ |  | _/ __ \\  \\/ /\\__  \\\\   __\\/  _ \\_  __ \\");
		MessageHandler.sendConsole("&b |        \\|  |_\\  ___/\\   /  / __ \\|  | (  <_> )  | \\/");
		MessageHandler.sendConsole("&b/_______  /|____/\\___  >\\_/  (____  /__|  \\____/|__|   ");
		MessageHandler.sendConsole("&b        \\/           \\/           \\/                   ");
		MessageHandler.sendConsole("");
		MessageHandler.sendConsole("&bElevator v" + getDescription().getVersion());
		MessageHandler.sendConsole("&bRunning on " + getServer().getName());
		MessageHandler.sendConsole("&bAuthor: " + getDescription().getAuthors().toString().replace("[", "").replace("]", "").replace(",", " and"));
		MessageHandler.sendConsole("&8&n_______________________________________________________");
		MessageHandler.sendConsole("");

		Config.initialize();
		createDatafile();

		TeleporterData.createSection();
		HookManager.initialize();
//		if (HookManager.isWorldGuardLoaded()) {
//			MessageHandler.sendConsole("&bWorldGuard is loaded, checking for flags...");
//			WGHandler.registerFlag();
//		}
		MessageHandler.sendConsole("&8&n_______________________________________________________");

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

	@Override
	public void onLoad() {
		try {
			WGHandler.registerFlag();
		} catch (Exception e) {
			// Do nothing
		}
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
