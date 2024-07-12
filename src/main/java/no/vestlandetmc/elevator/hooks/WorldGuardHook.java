package no.vestlandetmc.elevator.hooks;

import no.vestlandetmc.elevator.ElevatorPlugin;
import no.vestlandetmc.elevator.config.Config;

public class WorldGuardHook {

	public static boolean wgHook;

	public static void wgSearch() {
		if (Config.WORLDGUARD_HOOK) {
			wgHook = ElevatorPlugin.getPlugin().getServer().getPluginManager().getPlugin("WorldGuard") != null;
		} else {
			wgHook = false;
		}

	}
}
