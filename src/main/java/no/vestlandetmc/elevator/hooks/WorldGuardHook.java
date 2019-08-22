package no.vestlandetmc.elevator.hooks;

import no.vestlandetmc.elevator.ElevatorPlugin;
import no.vestlandetmc.elevator.config.Config;

public class WorldGuardHook {

	public static boolean wgHook;

	public static void wgSearch() {
		if(Config.WORLDGUARD_HOOK) {
			if (ElevatorPlugin.getInstance().getServer().getPluginManager().getPlugin("WorldGuard") != null) {
				wgHook = true;
			} else {
				wgHook = false;
			}
		} else {
			wgHook = false;
		}

	}
}
