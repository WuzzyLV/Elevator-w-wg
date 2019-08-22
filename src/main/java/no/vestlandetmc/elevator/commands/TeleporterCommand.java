package no.vestlandetmc.elevator.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import no.vestlandetmc.elevator.config.TeleporterData;
import no.vestlandetmc.elevator.handler.MessageHandler;

public class TeleporterCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			help(sender);
			return true;
		}

		switch (args[0]) {
		case "add":
			add(sender);
			break;
		case "remove":
			if(args[1] == null) {
				break;
			}
			remove(sender, args[1]);
			break;
		case "link":
			if(args[1] == null && args[2] == null) {
				break;
			}
			link(sender, args[1], args[2]);
			break;
		case "unlink":
			if(args[1] == null) {
				break;
			}
			unlink(sender, args[1]);
			break;
		case "list":
			list(sender);
			break;
		default:
			help(sender);
		}

		return true;
	}

	private void add(CommandSender sender) {
		final Player player = (Player) sender;
		TeleporterData.setTeleporter(player);
	}

	private void remove(CommandSender sender, String tpName) {
		final Player player = (Player) sender;
		TeleporterData.deleteTeleporter(player, tpName);
	}

	private void link(CommandSender sender, String tpName1, String tpName2) {
		final Player player = (Player) sender;
		TeleporterData.linkTeleporter(player, tpName1, tpName2);
	}

	private void unlink(CommandSender sender, String tpName) {
		final Player player = (Player) sender;
		TeleporterData.unlinkTeleporter(player, tpName);

	}

	private void list(CommandSender sender) {
		final Player player = (Player) sender;
		TeleporterData.listTP(player);
	}

	private void help(CommandSender sender) {
		final Player player = (Player) sender;
		if (player instanceof Player) {
			MessageHandler.sendMessage(player, "&e---- ====== [ &6Teleporters &e] ====== ---- ");
			MessageHandler.sendMessage(player, "");
		} else {
			MessageHandler.sendConsole("&cYou must use this command as a player in-game");
		}
	}

}
