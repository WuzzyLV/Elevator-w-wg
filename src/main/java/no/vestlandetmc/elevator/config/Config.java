package no.vestlandetmc.elevator.config;

import org.bukkit.Material;
import org.bukkit.Particle;

import no.vestlandetmc.elevator.handler.MessageHandler;

public class Config extends ConfigHandler {

	private Config(String fileName) {
		super(fileName);
	}

	public static Material
	BLOCK_TYPE,
	TP_BLOCK_TYPE;

	public static Particle
	PARTICLE_TYPE,
	TP_PARTICLE_TYPE;

	public static String
	ELEVATOR_UP,
	ELEVATOR_DOWN,
	ELEVATOR_DANGER,
	ELEVATOR_ACTIVATED,
	ELEVATOR_COOLDOWN,
	TP_DANGER,
	SOUND_ACTIVATED,
	ELEVATOR_SOUND,
	TP_SOUND,
	TP_USAGE_COOLDOWN;

	public static int
	PARTICLE_COUNT,
	TP_PARTICLE_COUNT,
	COOLDOWN_TIME;

	public static boolean
	PARTICAL_ENABLED,
	GRIEFPREVENTION_HOOK,
	WORLDGUARD_HOOK,
	COOLDOWN_ENABLED,
	TP_PARTICLE_ENABLE,
	TP_WARMUP_ENABLE;

	private void onLoad() {

		BLOCK_TYPE = Material.matchMaterial(getString("BlockType").toUpperCase());
		PARTICLE_TYPE = Particle.valueOf(getString("ParticleType").toUpperCase());
		PARTICAL_ENABLED = getBoolean("EnableParticle");
		ELEVATOR_UP = getString("ElevatorUp");
		ELEVATOR_DOWN = getString("ElevatorDown");
		ELEVATOR_DANGER = getString("ElevatorDanger");
		ELEVATOR_ACTIVATED = getString("ElevatorActivated");
		ELEVATOR_SOUND = getString("UsageSound");
		SOUND_ACTIVATED = getString("ActivateSound");
		PARTICLE_COUNT = getInt("ParticleCount");
		GRIEFPREVENTION_HOOK = getBoolean("Hooks.GriefPrevention");
		WORLDGUARD_HOOK = getBoolean("Hooks.WorldGuard");
		COOLDOWN_ENABLED = getBoolean("Cooldown.EnableCooldown");
		COOLDOWN_TIME = getInt("Cooldown.Time");
		ELEVATOR_COOLDOWN = getString("ElevatorCooldown");
		TP_BLOCK_TYPE = Material.matchMaterial(getString("Teleporter.BlockType").toUpperCase());
		TP_PARTICLE_TYPE = Particle.valueOf(getString("Teleporter.ParticleType").toUpperCase());
		TP_DANGER = getString("TeleporterDanger");
		TP_SOUND = getString("Teleporter.UsageSound");
		TP_PARTICLE_COUNT = getInt("Teleporter.ParticleCount");
		TP_PARTICLE_ENABLE = getBoolean("Teleporter.EnableParticle");
		TP_USAGE_COOLDOWN = getString("TeleporterUsage");

		sendInfo();

	}

	public static void initialize() {
		new Config("config.yml").onLoad();
	}

	private void sendInfo() {
		if(PARTICAL_ENABLED) {
			MessageHandler.sendConsole("&7[Elevator] &9Enabling &7particles");
			MessageHandler.sendConsole("&7[Elevator] Using &9" +  PARTICLE_TYPE.toString().replace("_", " ") + " &7as particle");
		} else {
			MessageHandler.sendConsole("&7[Elevator] &cDisabling &7particles");
		}

		MessageHandler.sendConsole("&7[Elevator] Using &9" + BLOCK_TYPE.name().replace("_", " ") + " &7as elevator block");
		MessageHandler.sendConsole("&7[Elevator] Using &9" + SOUND_ACTIVATED + " &7as activate elevator");
		MessageHandler.sendConsole("&7[Elevator] Using &9" + TP_SOUND + " &7when using elevator");
	}

}
