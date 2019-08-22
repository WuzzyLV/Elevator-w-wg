package no.vestlandetmc.elevator.hooks;

import no.vestlandetmc.elevator.ElevatorPlugin;
import no.vestlandetmc.elevator.config.Config;

public class GriefPreventionHook {

	public static boolean gpHook;

	public static void gpSearch() {
		if(Config.GRIEFPREVENTION_HOOK) {
			if (ElevatorPlugin.getInstance().getServer().getPluginManager().getPlugin("GriefPrevention") != null) {
				gpHook = true;
			} else {
				gpHook = false;
			}
		} else {
			gpHook = false;
		}
	}
}
