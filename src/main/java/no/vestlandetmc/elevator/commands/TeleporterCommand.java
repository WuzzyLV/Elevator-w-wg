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
			if(args.length != 2) {
				MessageHandler.sendMessage((Player) sender, "&cPlease specify a teleporter!");
				break;
			} else {
				remove(sender, args[1].toUpperCase());
				break;
			}
		case "link":
			if(args.length != 3) {
				MessageHandler.sendMessage((Player) sender, "&cPlease specify at least two teleporters!");
				break;
			} else {
				link(sender, args[1].toUpperCase(), args[2].toUpperCase());
				break;
			}
		case "unlink":
			if(args.length != 2) {
				MessageHandler.sendMessage((Player) sender, "&cPlease specify a teleporter!");
				break;
			} else {
				unlink(sender, args[1].toUpperCase());
				break;
			}
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
		if(TeleporterData.checkTpPerms(player)) {
			TeleporterData.setTeleporter(player);
		} else {
			MessageHandler.sendMessage(player, "&cYou dont have permission to set more teleporters!");
		}
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
			MessageHandler.sendMessage(player, "&6/teleporter add &f- &eSet teleportation block");
			MessageHandler.sendMessage(player, "&6/teleporter help &f- &eThis help page");
			MessageHandler.sendMessage(player, "&6/teleporter link &f[tpblock1] [tpblock2] - &eLink two teleportation blocks togheter");
			MessageHandler.sendMessage(player, "&6/teleporter list &f- &eGet a list of all your teleporters");
			MessageHandler.sendMessage(player, "&6/teleporter remove &f- &eRemove a teleportation block");
			MessageHandler.sendMessage(player, "&6/teleporter unlink &f[tpblock1] - &eUnlink two teleportation blocks. Only need to specify one.");
		} else {
			MessageHandler.sendConsole("&cYou must use this command as a player in-game");
		}
	}
}
