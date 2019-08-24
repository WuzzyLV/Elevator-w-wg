package no.vestlandetmc.elevator.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import no.vestlandetmc.elevator.config.Config;
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
				MessageHandler.sendMessage((Player) sender, Config.TP_LOCAL_SPECIFYTP);
				break;
			} else {
				remove(sender, args[1].toUpperCase());
				break;
			}
		case "link":
			if(args.length != 3) {
				MessageHandler.sendMessage((Player) sender, Config.TP_LOCAL_SPECIFYMORETP);
				break;
			} else {
				link(sender, args[1].toUpperCase(), args[2].toUpperCase());
				break;
			}
		case "unlink":
			if(args.length != 2) {
				MessageHandler.sendMessage((Player) sender, Config.TP_LOCAL_SPECIFYTP);
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

		if(GriefPreventionHook.gpHook) { if(!GPHandler.haveTrust(player) && !GPHandler.haveTrust(player)) return; }
		if(WorldGuardHook.wgHook) { if(!WGHandler.haveTrust(player) && !WGHandler.haveTrust(player)) return; }

		if(TeleporterData.checkTpPerms(player)) {
			TeleporterData.setTeleporter(player);
		} else {
			MessageHandler.sendMessage(player, Config.TP_LOCAL_PERMBLOCK);
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
			MessageHandler.sendMessage(player, Config.TP_LOCAL_HELPHEADER);
			MessageHandler.sendMessage(player, Config.TP_LOCAL_HELPADD);
			MessageHandler.sendMessage(player, Config.TP_LOCAL_HELPHELP);
			MessageHandler.sendMessage(player, Config.TP_LOCAL_HELPLINK);
			MessageHandler.sendMessage(player, Config.TP_LOCAL_HELPLIST);
			MessageHandler.sendMessage(player, Config.TP_LOCAL_HELPREMOVE);
			MessageHandler.sendMessage(player, Config.TP_LOCAL_HELPUNLINK);
		} else {
			MessageHandler.sendConsole("&cYou must use this command as a player in-game");
		}
	}
}
