package no.vestlandetmc.elevator.config;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

public class Permissions {

	public static boolean hasPermission(Player player,String command) {
		boolean hasPerm = false;
		for(final String perms : checkPerm(command)) {
			if(player.hasPermission(perms)) {
				hasPerm = true;
			}
		}

		return hasPerm;
	}

	private static List<String> checkPerm(String command) {
		return Arrays.asList("elevator.admin", "elevator.teleporter.basic", "elevator.teleporter." + command);
	}

}
