package no.vestlandetmc.elevator.commands;

import no.vestlandetmc.elevator.config.TeleporterData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompleteArgs implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			return Arrays.asList("add", "remove", "link", "unlink", "list", "help");
		} else if (args.length == 1) {
			final List<String> commandList = new ArrayList<>();

			if ("add".startsWith(args[0])) {
				commandList.add("add");
			}
			if ("remove".startsWith(args[0])) {
				commandList.add("remove");
			}
			if ("link".startsWith(args[0])) {
				commandList.add("link");
			}
			if ("unlink".startsWith(args[0])) {
				commandList.add("unlink");
			}
			if ("list".startsWith(args[0])) {
				commandList.add("list");
			}
			if ("help".startsWith(args[0])) {
				commandList.add("help");
			}

			return commandList;

		} else if (args.length == 2 && args[0].equals("link")) {
			return TeleporterData.tabCompleteTp((Player) sender);

		} else if (args.length == 3 && args[0].equals("link")) {
			return TeleporterData.tabCompleteTp((Player) sender);

		} else if (args.length == 2 && args[0].equals("unlink")) {
			return TeleporterData.tabCompleteTp((Player) sender);

		} else if (args.length == 2 && args[0].equals("remove")) {
			return TeleporterData.tabCompleteTp((Player) sender);
		}

		return null;
	}
}
