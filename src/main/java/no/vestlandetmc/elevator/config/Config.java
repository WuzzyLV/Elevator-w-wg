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
	PARTICLE_TYPE;

	public static String
	ELEVATOR_UP,
	ELEVATOR_DOWN,
	ELEVATOR_DANGER,
	ELEVATOR_ACTIVATED,
	ELEVATOR_COOLDOWN,
	SOUND_ACTIVATED,
	ELEVATOR_SOUND,
	TP_SOUND,
	TP_LOCAL_DANGER,
	TP_LOCAL_CANCELLED,
	TP_LOCAL_INIT,
	TP_LOCAL_WARMUP,
	TP_LOCAL_UNVALID,
	TP_LOCAL_EXIST,
	TP_LOCAL_UNEXIST,
	TP_LOCAL_ADDED,
	TP_LOCAL_LINKEXIST,
	TP_LOCAL_NOOWNER,
	TP_LOCAL_LINKED,
	TP_LOCAL_REMOVED,
	TP_LOCAL_UNLINKED,
	TP_LOCAL_NODEST,
	TP_LOCAL_LISTHEADER,
	TP_LOCAL_LISTNOTP,
	TP_LOCAL_SPECIFYTP,
	TP_LOCAL_SPECIFYMORETP,
	TP_LOCAL_PERMBLOCK,
	TP_LOCAL_HELPHEADER,
	TP_LOCAL_HELPADD,
	TP_LOCAL_HELPHELP,
	TP_LOCAL_HELPLINK,
	TP_LOCAL_HELPLIST,
	TP_LOCAL_HELPREMOVE,
	TP_LOCAL_HELPUNLINK;

	public static int
	PARTICLE_COUNT,
	COOLDOWN_TIME,
	TP_WARMUP_TIME;

	public static boolean
	PARTICLE_ENABLED,
	GRIEFPREVENTION_HOOK,
	WORLDGUARD_HOOK,
	COOLDOWN_ENABLED,
	TP_PARTICLE_ENABLE,
	TP_WARMUP_ENABLE;

	private void onLoad() {

		BLOCK_TYPE = Material.matchMaterial(getString("Elevator.BlockType").toUpperCase());
		PARTICLE_TYPE = Particle.valueOf(getString("Elevator.ParticleType").toUpperCase());
		PARTICLE_ENABLED = getBoolean("Elevator.EnableParticle");
		ELEVATOR_UP = getString("ElevatorLocal.ElevatorUp");
		ELEVATOR_DOWN = getString("ElevatorLocal.ElevatorDown");
		ELEVATOR_DANGER = getString("ElevatorLocal.ElevatorDanger");
		ELEVATOR_ACTIVATED = getString("ElevatorLocal.ElevatorActivated");
		ELEVATOR_SOUND = getString("Elevator.UsageSound");
		SOUND_ACTIVATED = getString("Elevator.ActivateSound");
		PARTICLE_COUNT = getInt("Elevator.ParticleCount");
		GRIEFPREVENTION_HOOK = getBoolean("Hooks.GriefPrevention");
		WORLDGUARD_HOOK = getBoolean("Hooks.WorldGuard");
		COOLDOWN_ENABLED = getBoolean("Cooldown.EnableCooldown");
		COOLDOWN_TIME = getInt("Cooldown.Time");
		ELEVATOR_COOLDOWN = getString("Cooldown.Local");
		TP_WARMUP_ENABLE = getBoolean("Teleporter.WarmupEnable");
		TP_WARMUP_TIME = getInt("Teleporter.WarmupTime");
		TP_BLOCK_TYPE = Material.matchMaterial(getString("Teleporter.BlockType").toUpperCase());
		TP_SOUND = getString("Teleporter.UsageSound");
		TP_PARTICLE_ENABLE = getBoolean("Teleporter.EnableParticle");
		TP_LOCAL_DANGER = getString("TeleporterLocal.Danger");
		TP_LOCAL_CANCELLED = getString("TeleporterLocal.Cancelled");
		TP_LOCAL_INIT = getString("TeleporterLocal.Initialized");
		TP_LOCAL_WARMUP = getString("TeleporterLocal.Warmup");
		TP_LOCAL_UNVALID = getString("TeleporterLocal.UnvalidBlock");
		TP_LOCAL_EXIST = getString("TeleporterLocal.Exist");
		TP_LOCAL_UNEXIST = getString("TeleporterLocal.Unexist");
		TP_LOCAL_ADDED = getString("TeleporterLocal.Added");
		TP_LOCAL_LINKEXIST = getString("TeleporterLocal.LinkExist");
		TP_LOCAL_NOOWNER = getString("TeleporterLocal.NoOwner");
		TP_LOCAL_LINKED = getString("TeleporterLocal.Linked");
		TP_LOCAL_REMOVED = getString("TeleporterLocal.Removed");
		TP_LOCAL_UNLINKED = getString("TeleporterLocal.Unlinked");
		TP_LOCAL_NODEST = getString("TeleporterLocal.NoDestination");
		TP_LOCAL_LISTHEADER = getString("TeleporterLocal.ListHeader");
		TP_LOCAL_LISTNOTP = getString("TeleporterLocal.ListNoTeleporters");
		TP_LOCAL_SPECIFYTP = getString("TeleporterLocal.SpecifyTeleporter");
		TP_LOCAL_SPECIFYMORETP = getString("TeleporterLocal.SpecifyMoreTeleporter");
		TP_LOCAL_PERMBLOCK = getString("TeleporterLocal.PermissionBlocks");
		TP_LOCAL_HELPHEADER = getString("TeleporterLocal.HelpHeader");
		TP_LOCAL_HELPADD = getString("TeleporterLocal.HelpAdd");
		TP_LOCAL_HELPHELP = getString("TeleporterLocal.HelpHelp");
		TP_LOCAL_HELPLINK = getString("TeleporterLocal.HelpLink");
		TP_LOCAL_HELPLIST = getString("TeleporterLocal.HelpList");
		TP_LOCAL_HELPREMOVE = getString("TeleporterLocal.HelpRemove");
		TP_LOCAL_HELPUNLINK = getString("TeleporterLocal.HelpUnlink");

		boolean saveFile = false;

		//Cleanup old path in config file
		if(contains("ElevatorUp")) {
			set("ElevatorUp", null);
			saveFile = true;
		}

		if(contains("ElevatorDown")) {
			set("ElevatorDown", null);
			saveFile = true;
		}

		if(contains("ElevatorDanger")) {
			set("ElevatorDanger", null);
			saveFile = true;
		}

		if(contains("ElevatorActivated")) {
			set("ElevatorActivated", null);
			saveFile = true;
		}

		if(contains("ElevatorCooldown")) {
			set("ElevatorCooldown", null);
			saveFile = true;
		}

		if(contains("BlockType")) {
			set("BlockType", null);
			saveFile = true;
		}

		if(contains("EnableParticle")) {
			set("EnableParticle", null);
			saveFile = true;
		}

		if(contains("ParticleType")) {
			set("ParticleType", null);
			saveFile = true;
		}

		if(contains("ParticleCount")) {
			set("ParticleCount", null);
			saveFile = true;
		}

		if(contains("UsageSound")) {
			set("UsageSound", null);
			saveFile = true;
		}

		if(contains("ActivateSound")) {
			set("ActivateSound", null);
			saveFile = true;
		}

		if(saveFile) {
			saveConfig();
			MessageHandler.sendConsole("&7[Elevator] &6The config file has been cleaned. Please check the config file for changes.");
		}

		sendInfo();

	}

	public static void initialize() {
		new Config("config.yml").onLoad();
	}

	private void sendInfo() {
		if(PARTICLE_ENABLED) {
			MessageHandler.sendConsole("&7[Elevator] &9Enabling &7particles for elevators");
			MessageHandler.sendConsole("&7[Elevator] Using &9" +  PARTICLE_TYPE.toString().replace("_", " ") + " &7as particle for elevators");
		} else {
			MessageHandler.sendConsole("&7[Elevator] &cDisabling &7particles for elevators");
		}

		if(TP_PARTICLE_ENABLE) {
			MessageHandler.sendConsole("&7[Elevator] &9Enabling &7particles for teleporters");
		} else {
			MessageHandler.sendConsole("&7[Elevator] &cDisabling &7particles for teleporters");
		}

		MessageHandler.sendConsole("&7[Elevator] Using &9" + BLOCK_TYPE.name().replace("_", " ") + " &7as elevator block");
		MessageHandler.sendConsole("&7[Elevator] Using &9" + TP_BLOCK_TYPE.name().replace("_", " ") + " &7as teleporter block");
		MessageHandler.sendConsole("&7[Elevator] Using the sound &9" + SOUND_ACTIVATED + " &7on activate elevator");
		MessageHandler.sendConsole("&7[Elevator] Using the sound &9" + ELEVATOR_SOUND + " &7when using elevator");
		MessageHandler.sendConsole("&7[Elevator] Using the sound &9" + TP_SOUND + " &7when using teleporters");
	}
}
