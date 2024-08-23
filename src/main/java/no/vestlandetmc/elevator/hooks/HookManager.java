package no.vestlandetmc.elevator.hooks;

import lombok.Getter;
import no.vestlandetmc.elevator.ElevatorPlugin;
import no.vestlandetmc.elevator.handler.MessageHandler;

public class HookManager {

	@Getter
	private static boolean griefDefenderLoaded = false;
	@Getter
	private static boolean griefPreventionLoaded = false;
	@Getter
	private static boolean worldGuardLoaded = false;

	public static void initialize() {
		final ElevatorPlugin plugin = ElevatorPlugin.getPlugin();
		final String[] vanishPlugins = {
				"GriefDefender",
				"GriefPrevention",
				"WorldGuard",
				"CMI",
				"Essentials",
				"PremiumVanish",
				"SuperVanish"
		};

		for (String vanish : vanishPlugins) {
			if (plugin.getServer().getPluginManager().getPlugin(vanish) != null) {
				switch (vanish) {
					case "GriefDefender" -> griefDefenderLoaded = true;
					case "GriefPrevention" -> griefPreventionLoaded = true;
					case "WorldGuard" -> worldGuardLoaded = true;
				}

				MessageHandler.sendConsole("&7Successfully hooked into &b" + vanish);
			}
		}
	}
}
