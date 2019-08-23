package no.vestlandetmc.elevator.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

public class TabCompleteArgs implements TabCompleter {

	private static final String[] commandList = { "add", "remove", "link", "unlink", "list", "help" };

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		final List<String> commands = new ArrayList<>(Arrays.asList(commandList));

		final Set<String> commandList = new HashSet<>();
		commandList.add("add");
		commandList.add("remove");
		commandList.add("link");
		commandList.add("unlink");
		commandList.add("list");
		commandList.add("help");

		StringUtil.copyPartialMatches(args[0], commandList, commands);
		Collections.sort(commands);
		return commands;
	}

}
