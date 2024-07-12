package no.vestlandetmc.elevator.commands;

import no.vestlandetmc.elevator.config.Config;
import no.vestlandetmc.elevator.config.TeleporterData;
import no.vestlandetmc.elevator.handler.GPHandler;
import no.vestlandetmc.elevator.handler.MessageHandler;
import no.vestlandetmc.elevator.handler.WGHandler;
import no.vestlandetmc.elevator.hooks.GriefPreventionHook;
import no.vestlandetmc.elevator.hooks.WorldGuardHook;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleporterCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			MessageHandler.sendConsole("&cYou must use this command as a player in-game");
			return true;
		}

		if (args.length == 0) {
			help(sender);
			return true;
		}

		switch (args[0]) {
			case "add":
				add(player);
				break;
			case "remove":
				if (args.length != 2) {
					MessageHandler.sendMessage(player, Config.TP_LOCALE_SPECIFYTP);
				} else {
					remove(player, args[1].toUpperCase());
				}
				break;
			case "link":
				if (args.length != 3) {
					MessageHandler.sendMessage(player, Config.TP_LOCALE_SPECIFYMORETP);
				} else {
					link(player, args[1].toUpperCase(), args[2].toUpperCase());
				}
				break;
			case "unlink":
				if (args.length != 2) {
					MessageHandler.sendMessage(player, Config.TP_LOCALE_SPECIFYTP);
				} else {
					unlink(player, args[1].toUpperCase());
				}
				break;
			case "list":
				list(player);
				break;
			default:
				help(player);
		}

		return true;
	}

	private void add(Player player) {
		if (!player.hasPermission("elevator.teleporter.add")) {
			MessageHandler.sendMessage(player, Config.ML_LOCALE_PERMISSION);
			return;
		}

		if (GriefPreventionHook.gpHook) {
			if (!GPHandler.haveTrust(player) && !GPHandler.haveTrust(player)) return;
		}
		if (WorldGuardHook.wgHook) {
			if (!WGHandler.haveTrust(player) && !WGHandler.haveTrust(player)) return;
		}

		if (TeleporterData.checkTpPerms(player)) {
			TeleporterData.setTeleporter(player);
		} else {
			MessageHandler.sendMessage(player, Config.TP_LOCALE_PERMBLOCK);
		}
	}

	private void remove(Player player, String tpName) {
		if (!player.hasPermission("elevator.teleporter.remove")) {
			MessageHandler.sendMessage(player, Config.ML_LOCALE_PERMISSION);
			return;
		}

		TeleporterData.deleteTeleporter(player, tpName);
	}

	private void link(Player player, String tpName1, String tpName2) {
		if (!player.hasPermission("elevator.teleporter.link")) {
			MessageHandler.sendMessage(player, Config.ML_LOCALE_PERMISSION);
			return;
		}

		if (tpName1.equals(tpName2)) {
			MessageHandler.sendMessage(player, Config.TP_LOCALE_LINKSELF);
			return;
		}

		TeleporterData.linkTeleporter(player, tpName1, tpName2);
	}

	private void unlink(Player player, String tpName) {
		if (!player.hasPermission("elevator.teleporter.unlink")) {
			MessageHandler.sendMessage(player, Config.ML_LOCALE_PERMISSION);
			return;
		}

		TeleporterData.unlinkTeleporter(player, tpName);

	}

	private void list(Player player) {
		if (!player.hasPermission("elevator.teleporter.list")) {
			MessageHandler.sendMessage(player, Config.ML_LOCALE_PERMISSION);
			return;
		}

		TeleporterData.listTP(player);
	}

	private void help(CommandSender sender) {
		final Player player = (Player) sender;

		if (!player.hasPermission("elevator.teleporter.use")) {
			MessageHandler.sendMessage(player, Config.ML_LOCALE_PERMISSION);
			return;
		}

		MessageHandler.sendMessage(player, Config.TP_LOCALE_HELPHEADER);
		MessageHandler.sendMessage(player, Config.TP_LOCALE_HELPADD);
		MessageHandler.sendMessage(player, Config.TP_LOCALE_HELPHELP);
		MessageHandler.sendMessage(player, Config.TP_LOCALE_HELPLINK);
		MessageHandler.sendMessage(player, Config.TP_LOCALE_HELPLIST);
		MessageHandler.sendMessage(player, Config.TP_LOCALE_HELPREMOVE);
		MessageHandler.sendMessage(player, Config.TP_LOCALE_HELPUNLINK);
	}
}
