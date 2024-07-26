package no.vestlandetmc.elevator.handler;

import lombok.Getter;
import no.vestlandetmc.elevator.ElevatorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Particle;

@Getter
public class VersionHandler {

	private Particle teleporterParticle;

	public VersionHandler() {
		final String bukkitVersion = Bukkit.getBukkitVersion();
		final String version = bukkitVersion.split("-")[0];
		ElevatorPlugin.getPlugin().getLogger().info("Detected " + bukkitVersion + ".");

		if (version.length() >= 4) {
			switch (version.substring(0, 4)) {
				case "1.13", "1.14", "1.15", "1.16", "1.17", "1.18", "1.19", "1.20":
					teleporterParticle = Particle.valueOf("REDSTONE");
					break;
				default:
					teleporterParticle = Particle.valueOf("DUST");
					break;
			}
		}
	}
}
