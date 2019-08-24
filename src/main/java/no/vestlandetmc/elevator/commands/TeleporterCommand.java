package no.vestlandetmc.elevator.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import no.vestlandetmc.elevator.config.Config;
import no.vestlandetmc.elevator.config.Permissions;
import no.vestlandetmc.elevator.config.TeleporterData;
import no.vestlandetmc.elevator.handler.GPHandler;
import no.vestlandetmc.elevator.handler.MessageHandler;
import no.vestlandetmc.elevator.handler.WGHandler;
import no.vestlandetmc.elevator.hooks.GriefPreventionHook;
import no.vestlandetmc.elevator.hooks.WorldGuardHook;

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
				MessageHandler.sendMessage((Player) sender, Config.TP_LOCALE_SPECIFYTP);
				break;
			} else {
				remove(sender, args[1].toUpperCase());
				break;
			}
		case "link":
			if(args.length != 3) {
				MessageHandler.sendMessage((Player) sender, Config.TP_LOCALE_SPECIFYMORETP);
				break;
			} else {
				link(sender, args[1].toUpperCase(), args[2].toUpperCase());
				break;
			}
		case "unlink":
			if(args.length != 2) {
				MessageHandler.sendMessage((Player) sender, Config.TP_LOCALE_SPECIFYTP);
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
		if (!(player instanceof Player)) {
			MessageHandler.sendConsole("&cYou must use this command as a player in-game");
			return;
		}

		if(!Permissions.hasPermission(player, "add")) {
			MessageHandler.sendMessage(player, Config.ML_LOCALE_PERMISSION);
			return;
		}

		if(GriefPreventionHook.gpHook) { if(!GPHandler.haveTrust(player) && !GPHandler.haveTrust(player)) return; }
		if(WorldGuardHook.wgHook) { if(!WGHandler.haveTrust(player) && !WGHandler.haveTrust(player)) return; }

		if(TeleporterData.checkTpPerms(player)) {
			TeleporterData.setTeleporter(player);
		} else {
			MessageHandler.sendMessage(player, Config.TP_LOCALE_PERMBLOCK);
		}
	}

	private void remove(CommandSender sender, String tpName) {
		final Player player = (Player) sender;
		if (!(player instanceof Player)) {
			MessageHandler.sendConsole("&cYou must use this command as a player in-game");
			return;
		}

		if(!Permissions.hasPermission(player, "remove")) {
			MessageHandler.sendMessage(player, Config.ML_LOCALE_PERMISSION);
			return;
		}

		TeleporterData.deleteTeleporter(player, tpName);
	}

	private void link(CommandSender sender, String tpName1, String tpName2) {
		final Player player = (Player) sender;
		if (!(player instanceof Player)) {
			MessageHandler.sendConsole("&cYou must use this command as a player in-game");
			return;
		}

		if(!Permissions.hasPermission(player, "link")) {
			MessageHandler.sendMessage(player, Config.ML_LOCALE_PERMISSION);
			return;
		}

		if(tpName1.equals(tpName2)) {
			MessageHandler.sendMessage(player, Config.TP_LOCALE_LINKSELF);
			return;
		}

		TeleporterData.linkTeleporter(player, tpName1, tpName2);
	}

	private void unlink(CommandSender sender, String tpName) {
		final Player player = (Player) sender;
		if (!(player instanceof Player)) {
			MessageHandler.sendConsole("&cYou must use this command as a player in-game");
			return;
		}

		if(!Permissions.hasPermission(player, "unlink")) {
			MessageHandler.sendMessage(player, Config.ML_LOCALE_PERMISSION);
			return;
		}

		TeleporterData.unlinkTeleporter(player, tpName);

	}

	private void list(CommandSender sender) {
		final Player player = (Player) sender;
		if (!(player instanceof Player)) {
			MessageHandler.sendConsole("&cYou must use this command as a player in-game");
			return;
		}

		if(!Permissions.hasPermission(player, "list")) {
			MessageHandler.sendMessage(player, Config.ML_LOCALE_PERMISSION);
			return;
		}

		TeleporterData.listTP(player);
	}

	private void help(CommandSender sender) {
		final Player player = (Player) sender;
		if (!(player instanceof Player)) {
			MessageHandler.sendConsole("&cYou must use this command as a player in-game");
			return;
		}

		if(!Permissions.hasPermission(player, "help")) {
			MessageHandler.sendMessage(player, Config.ML_LOCALE_PERMISSION);
			return;
		}

		if (player instanceof Player) {
			MessageHandler.sendMessage(player, Config.TP_LOCALE_HELPHEADER);
			MessageHandler.sendMessage(player, Config.TP_LOCALE_HELPADD);
			MessageHandler.sendMessage(player, Config.TP_LOCALE_HELPHELP);
			MessageHandler.sendMessage(player, Config.TP_LOCALE_HELPLINK);
			MessageHandler.sendMessage(player, Config.TP_LOCALE_HELPLIST);
			MessageHandler.sendMessage(player, Config.TP_LOCALE_HELPREMOVE);
			MessageHandler.sendMessage(player, Config.TP_LOCALE_HELPUNLINK);
		} else {
			MessageHandler.sendConsole("&cYou must use this command as a player in-game");
		}
	}
}
