package no.vestlandetmc.elevator.hooks;

import no.vestlandetmc.elevator.ElevatorPlugin;
import no.vestlandetmc.elevator.config.Config;

public class GriefPreventionHook {

	public static boolean gpHook;

	public static void gpSearch() {
		if (Config.GRIEFPREVENTION_HOOK) {
			gpHook = ElevatorPlugin.getPlugin().getServer().getPluginManager().getPlugin("GriefPrevention") != null;
		} else {
			gpHook = false;
		}
	}
}
