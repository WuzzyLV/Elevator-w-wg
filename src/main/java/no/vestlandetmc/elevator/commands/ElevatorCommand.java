package no.vestlandetmc.elevator.commands;

import no.vestlandetmc.elevator.ElevatorPlugin;
import no.vestlandetmc.elevator.config.Config;
import no.vestlandetmc.elevator.handler.MessageHandler;
import no.vestlandetmc.elevator.handler.UpdateNotification;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ElevatorCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			info(sender);
			return true;
		} else if (args[0].equals("reload")) {
			ElevatorPlugin.getPlugin().reload();
			if (sender instanceof Player) {
				if (!sender.hasPermission("elevator.admin")) {
					MessageHandler.sendMessage((Player) sender, Config.ML_LOCALE_PERMISSION);
					return true;
				}
				MessageHandler.sendMessage((Player) sender, "&3[Elevator] The config has been reloaded");
			}
			MessageHandler.sendConsole("&3[Elevator] The config has been reloaded");
			return true;
		}

		info(sender);
		return true;
	}

	private void info(CommandSender sender) {
		if (!(sender instanceof Player)) {
			MessageHandler.sendConsole("&3---------------------------------");
			MessageHandler.sendConsole("&bElevator is running version: &3v" + UpdateNotification.getCurrentVersion());
			if (UpdateNotification.isUpdateAvailable()) {
				MessageHandler.sendConsole("&aUpdate is available! New version is: &2" + UpdateNotification.getLatestVersion());
				MessageHandler.sendConsole("&aGet the new update at https://www.spigotmc.org/resources/" + UpdateNotification.getProjectId());
			} else {
				MessageHandler.sendConsole("&aYou are running the latest version!");
			}

			MessageHandler.sendConsole("&bRun &3/elevator reload &bto reload the plugin.");
			MessageHandler.sendConsole("&3---------------------------------");
		} else {
			if (!sender.hasPermission("elevator.admin")) {
				MessageHandler.sendMessage((Player) sender, Config.ML_LOCALE_PERMISSION);
				return;
			}
			MessageHandler.sendMessage((Player) sender, "&3---------------------------------");
			MessageHandler.sendMessage((Player) sender, "&bElevator is running version: &3v" + UpdateNotification.getCurrentVersion());
			if (UpdateNotification.isUpdateAvailable()) {
				MessageHandler.sendMessage((Player) sender, "&aUpdate is available! New version is: &2v" + UpdateNotification.getLatestVersion());
				MessageHandler.sendMessage((Player) sender, "&aGet the new update at https://www.spigotmc.org/resources/" + UpdateNotification.getProjectId());
			} else {
				MessageHandler.sendMessage((Player) sender, "&aYou are running the latest version!");
			}

			MessageHandler.sendMessage((Player) sender, "&bRun &3/elevator reload &bto reload the plugin.");
			MessageHandler.sendMessage((Player) sender, "&3---------------------------------");
		}
	}

}
