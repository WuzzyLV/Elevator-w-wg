package no.vestlandetmc.elevator.hooks;

import no.vestlandetmc.elevator.ElevatorPlugin;
import no.vestlandetmc.elevator.config.Config;

public class GriefDefenderHook {

	public static boolean gdHook;

	public static void gdSearch() {
		if (Config.GRIEFDEFENDER_HOOK) {
			gdHook = ElevatorPlugin.getPlugin().getServer().getPluginManager().getPlugin("GriefDefender") != null;
		} else {
			gdHook = false;
		}
	}

}
