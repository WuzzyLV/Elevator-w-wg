package no.vestlandetmc.elevator.config;

import no.vestlandetmc.elevator.ElevatorPlugin;
import no.vestlandetmc.elevator.handler.MessageHandler;
import no.vestlandetmc.elevator.hooks.VanishManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeleporterData {

	private static final HashMap<String, Long> warmUpTeleporter = new HashMap<>();

	public static void teleporterUsed(Player player, String tpName) {
		if (!warmUpTeleporter.containsKey(player.getUniqueId().toString())) {
			warmUpTeleporter.put(player.getUniqueId().toString(), (System.currentTimeMillis() / 1000));


			new BukkitRunnable() {
				@Override
				public void run() {
					if (!warmUpTeleporter.containsKey(player.getUniqueId().toString())) {
						this.cancel();
						return;
					}

					if (warmupTime(player) <= 0) {
						warmUpTeleporter.remove(player.getUniqueId().toString());
						player.teleport(getTeleportLoc(tpName));
						if (Config.TP_PARTICLE_ENABLE) {
							particleTeleporter(player);
						}
						player.getWorld().playSound(player.getLocation(), "minecraft:" + Config.TP_SOUND, 1.0F, 0.7F);
						MessageHandler.sendAction(player, Config.TP_LOCALE_INIT);
						this.cancel();
						return;
					}
					if (Config.TP_PARTICLE_ENABLE && !VanishManager.isVanished(player)) {
						particleTeleporter(player);
					}
					MessageHandler.sendAction(player, MessageHandler.placeholders(Config.TP_LOCALE_WARMUP, warmupTime(player).toString(), null, null, null));
				}

			}.runTaskTimer(ElevatorPlugin.getPlugin(), 0L, 2L);
		}
	}

	public static boolean teleporterMove(Player player) {
		if (warmUpTeleporter.containsKey(player.getUniqueId().toString())) {
			warmUpTeleporter.remove(player.getUniqueId().toString());
			return true;
		}

		return false;
	}

	private static Long warmupTime(Player player) {
		if (Config.TP_WARMUP_ENABLE) {
			return 0L;
		}
		if (warmUpTeleporter.containsKey(player.getUniqueId().toString())) {
			return Config.TP_WARMUP_TIME - ((System.currentTimeMillis() / 1000) - warmUpTeleporter.get(player.getUniqueId().toString()));
		}

		return 0L;
	}

	private static void particleTeleporter(Player player) {
		final Particle.DustOptions options = new Particle.DustOptions(Color.AQUA, 0.5F);

		for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
			final double radius = Math.sin(i);
			final double y = Math.cos(i) + 1.0D;
			for (double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
				final double x = Math.cos(a) * radius;
				final double z = Math.sin(a) * radius;
				final Location loc = player.getLocation().add(x, y, z);
				final Particle particle = ElevatorPlugin.getVersionHandler().getTeleporterParticle();
				player.getWorld().spawnParticle(particle, loc, 0, 0, 1, 0, options);
			}
		}
	}

	public static void setTeleporter(Player player) {
		final double locX = player.getWorld().getBlockAt(player.getLocation()).getX();
		final double locY = player.getWorld().getBlockAt(player.getLocation()).getY() - 1.0D;
		final double locZ = player.getWorld().getBlockAt(player.getLocation()).getZ();
		final String world = player.getLocation().getWorld().getName();

		if (!standOnBlock(player)) {
			MessageHandler.sendMessage(player, Config.TP_LOCALE_UNVALID);
			return;
		}

		final Location loc = new Location(Bukkit.getWorld(world), locX, locY, locZ);

		if (teleporterExist(loc)) {
			MessageHandler.sendMessage(player, Config.TP_LOCALE_EXIST);
			return;
		}

		int tpNumberMax = 1;

		if (ElevatorPlugin.getPlugin().getDataFile().contains("Teleporters")) {
			if (!ElevatorPlugin.getPlugin().getDataFile().getConfigurationSection("Teleporters").getKeys(false).isEmpty()) {
				while (true) {
					if (!ElevatorPlugin.getPlugin().getDataFile().contains("Teleporters.TP" + tpNumberMax)) {
						final String tpName = "TP" + tpNumberMax;
						writeTpName(player, tpNumberMax, locX, locY, locZ, world, tpName);
						break;

					} else {
						tpNumberMax++;
					}
				}
			} else {
				writeTpName(player, tpNumberMax, locX, locY, locZ, world, "TP1");
			}
		}
	}

	private static boolean standOnBlock(Player player) {
		return player.getWorld().getBlockAt(player.getLocation().add(0.0D, -1.0D, 0.0D)).getType() == Config.TP_BLOCK_TYPE;
	}

	private static void writeTpName(Player player, int tpNumberMax, double locX, double locY, double locZ, String world, String tpName) {
		if (!ElevatorPlugin.getPlugin().getDataFile().contains("Teleporters.TP" + tpNumberMax)) {
			ElevatorPlugin.getPlugin().getDataFile().set("Teleporters." + tpName + "." + "X", locX);
			ElevatorPlugin.getPlugin().getDataFile().set("Teleporters." + tpName + "." + "Y", locY);
			ElevatorPlugin.getPlugin().getDataFile().set("Teleporters." + tpName + "." + "Z", locZ);
			ElevatorPlugin.getPlugin().getDataFile().set("Teleporters." + tpName + "." + "World", world);
			ElevatorPlugin.getPlugin().getDataFile().set("Teleporters." + tpName + "." + "Player", player.getUniqueId().toString());

			MessageHandler.sendMessage(player, MessageHandler.placeholders(Config.TP_LOCALE_ADDED, null, tpName, null, null));

			saveDatafile();
		}
	}


	public static Location getTeleportLoc(String tpName) {
		if (ElevatorPlugin.getPlugin().getDataFile().contains("Teleporters." + tpName + ".Destination")) {
			final String tpTo = ElevatorPlugin.getPlugin().getDataFile().getString("Teleporters." + tpName + ".Destination");
			final double locX = ElevatorPlugin.getPlugin().getDataFile().getDouble("Teleporters." + tpTo + ".X") + 0.5D;
			final double locY = ElevatorPlugin.getPlugin().getDataFile().getDouble("Teleporters." + tpTo + ".Y") + 1.0D;
			final double locZ = ElevatorPlugin.getPlugin().getDataFile().getDouble("Teleporters." + tpTo + ".Z") + 0.5D;
			final World world = Bukkit.getWorld(ElevatorPlugin.getPlugin().getDataFile().getString("Teleporters." + tpTo + "." + "World"));

			return new Location(world, locX, locY, locZ);
		}

		return null;
	}

	public static String getTeleporter(Location loc) {
		if (!(ElevatorPlugin.getPlugin().getDataFile().getKeys(false).toArray().length == 0) ||
				!(ElevatorPlugin.getPlugin().getDataFile().getConfigurationSection("Teleporters").getKeys(false).toArray().length == 0)) {
			for (final String tp : ElevatorPlugin.getPlugin().getDataFile().getConfigurationSection("Teleporters").getKeys(false)) {
				final double locX = ElevatorPlugin.getPlugin().getDataFile().getDouble("Teleporters." + tp + "." + "X");
				final double locY = ElevatorPlugin.getPlugin().getDataFile().getDouble("Teleporters." + tp + "." + "Y");
				final double locZ = ElevatorPlugin.getPlugin().getDataFile().getDouble("Teleporters." + tp + "." + "Z");
				final World world = Bukkit.getWorld(ElevatorPlugin.getPlugin().getDataFile().getString("Teleporters." + tp + ".World"));

				final Location tpLoc = new Location(world, locX, locY, locZ);

				if (tpLoc.toString().equals(loc.toString())) {
					return tp;
				}
			}
		}

		return null;
	}

	private static boolean teleporterExist(Location loc) {
		if (!(ElevatorPlugin.getPlugin().getDataFile().getKeys(false).toArray().length == 0) ||
				!(ElevatorPlugin.getPlugin().getDataFile().getConfigurationSection("Teleporters").getKeys(false).toArray().length == 0)) {
			for (final String tp : ElevatorPlugin.getPlugin().getDataFile().getConfigurationSection("Teleporters").getKeys(false)) {
				final double locX = ElevatorPlugin.getPlugin().getDataFile().getDouble("Teleporters." + tp + "." + "X");
				final double locY = ElevatorPlugin.getPlugin().getDataFile().getDouble("Teleporters." + tp + "." + "Y");
				final double locZ = ElevatorPlugin.getPlugin().getDataFile().getDouble("Teleporters." + tp + "." + "Z");
				final World world = Bukkit.getWorld(ElevatorPlugin.getPlugin().getDataFile().getString("Teleporters." + tp + ".World"));

				final Location tpLoc = new Location(world, locX, locY, locZ);

				if (tpLoc.toString().equals(loc.toString())) {
					return true;
				}
			}
		}

		return false;
	}

	public static void linkTeleporter(Player player, String tp1, String tp2) {
		if (!ElevatorPlugin.getPlugin().getDataFile().contains("Teleporters." + tp1) ||
				!ElevatorPlugin.getPlugin().getDataFile().contains("Teleporters." + tp2)) {
			MessageHandler.sendMessage(player, Config.TP_LOCALE_UNEXIST);
			return;
		}

		if (ElevatorPlugin.getPlugin().getDataFile().contains("Teleporters." + tp1 + ".Destination") ||
				ElevatorPlugin.getPlugin().getDataFile().contains("Teleporters." + tp2 + ".Destination")) {
			MessageHandler.sendMessage(player, Config.TP_LOCALE_LINKEXIST);
			return;
		}

		if (!checkTpOwner(player, tp1.toUpperCase(), tp2.toUpperCase())) {
			MessageHandler.sendMessage(player, Config.TP_LOCALE_NOOWNER);
			return;
		}

		ElevatorPlugin.getPlugin().getDataFile().set("Teleporters." + tp1 + ".Destination", tp2);
		ElevatorPlugin.getPlugin().getDataFile().set("Teleporters." + tp2 + ".Destination", tp1);

		MessageHandler.sendMessage(player, MessageHandler.placeholders(Config.TP_LOCALE_LINKED, null, null, tp1, tp2));

		saveDatafile();
	}

	public static void deleteTeleporter(Player player, String tpName) {
		if (!ElevatorPlugin.getPlugin().getDataFile().contains("Teleporters." + tpName)) {
			MessageHandler.sendMessage(player, Config.TP_LOCALE_UNEXIST);
			return;
		}

		if (!checkTpOwner(player, tpName)) {
			MessageHandler.sendMessage(player, Config.TP_LOCALE_NOOWNER);
			return;
		}

		if (ElevatorPlugin.getPlugin().getDataFile().contains("Teleporters." + tpName + ".Destination")) {
			final String tpName2 = ElevatorPlugin.getPlugin().getDataFile().getString("Teleporters." + tpName + ".Destination");
			ElevatorPlugin.getPlugin().getDataFile().set("Teleporters." + tpName2 + ".Destination", null);
		}

		ElevatorPlugin.getPlugin().getDataFile().set("Teleporters." + tpName, null);

		MessageHandler.sendMessage(player, MessageHandler.placeholders(Config.TP_LOCALE_REMOVED, null, tpName, null, null));

		saveDatafile();
	}

	public static void deleteTeleporter(String tpName) {
		if (!ElevatorPlugin.getPlugin().getDataFile().contains("Teleporters." + tpName)) {
			return;
		}

		if (ElevatorPlugin.getPlugin().getDataFile().contains("Teleporters." + tpName + ".Destination")) {
			final String tpName2 = ElevatorPlugin.getPlugin().getDataFile().getString("Teleporters." + tpName + ".Destination");
			ElevatorPlugin.getPlugin().getDataFile().set("Teleporters." + tpName2 + ".Destination", null);
		}

		ElevatorPlugin.getPlugin().getDataFile().set("Teleporters." + tpName, null);

		saveDatafile();
	}

	public static void unlinkTeleporter(Player player, String tpName) {
		if (!ElevatorPlugin.getPlugin().getDataFile().contains("Teleporters." + tpName)) {
			MessageHandler.sendMessage(player, Config.TP_LOCALE_UNEXIST);
			return;
		}

		if (!checkTpOwner(player, tpName)) {
			MessageHandler.sendMessage(player, Config.TP_LOCALE_NOOWNER);
			return;
		}

		if (ElevatorPlugin.getPlugin().getDataFile().contains("Teleporters." + tpName + "." + "Destination")) {
			final String tpName2 = ElevatorPlugin.getPlugin().getDataFile().getString("Teleporters." + tpName + "." + "Destination");

			ElevatorPlugin.getPlugin().getDataFile().set("Teleporters." + tpName + "." + "Destination", null);
			ElevatorPlugin.getPlugin().getDataFile().set("Teleporters." + tpName2 + "." + "Destination", null);

			MessageHandler.sendMessage(player, MessageHandler.placeholders(Config.TP_LOCALE_UNLINKED, null, null, tpName, tpName2));

			saveDatafile();
		} else {
			MessageHandler.sendMessage(player, MessageHandler.placeholders(Config.TP_LOCALE_NODEST, null, tpName, null, null));
		}
	}

	public static void listTP(Player player) {
		int count = 0;

		MessageHandler.sendMessage(player, Config.TP_LOCALE_LISTHEADER);
		for (final String tp : ElevatorPlugin.getPlugin().getDataFile().getConfigurationSection("Teleporters").getKeys(false)) {
			final double locX = ElevatorPlugin.getPlugin().getDataFile().getDouble("Teleporters." + tp + "." + "X");
			final double locY = ElevatorPlugin.getPlugin().getDataFile().getDouble("Teleporters." + tp + "." + "Y");
			final double locZ = ElevatorPlugin.getPlugin().getDataFile().getDouble("Teleporters." + tp + "." + "Z");
			final String world = ElevatorPlugin.getPlugin().getDataFile().getString("Teleporters." + tp + "." + "World");
			String link;
			if (ElevatorPlugin.getPlugin().getDataFile().contains("Teleporters." + tp + "." + "Destination")) {
				link = ElevatorPlugin.getPlugin().getDataFile().getString("Teleporters." + tp + "." + "Destination");
			} else {
				link = null;
			}

			if (!checkTpOwner(player, tp.toUpperCase())) {
				continue;
			} else {
				count++;
			}

			if (link == null) {
				MessageHandler.sendMessage(player, "&6" + tp + " &eX:&6" + locX + " &eY:&6" + locY + " &eZ:&6" + locZ + " &ein world &6" + world);
			} else {
				MessageHandler.sendMessage(player, "&6" + tp + " &eX:&6" + locX + " &eY:&6" + locY + " &eZ:&6" + locZ + " &ein world &6" + world + " &e==> &6" + link);
			}

		}

		if (count == 0) {
			MessageHandler.sendMessage(player, Config.TP_LOCALE_LISTNOTP);
		}
	}

	public static List<String> tabCompleteTp(Player player) {
		final List<String> teleportList = new ArrayList<>();
		for (final String tp : ElevatorPlugin.getPlugin().getDataFile().getConfigurationSection("Teleporters").getKeys(false)) {
			if (checkTpOwner(player, tp.toUpperCase())) {
				teleportList.add(tp.toLowerCase());
			}
		}
		return teleportList;
	}

	private static boolean checkTpOwner(Player player, String tp1, String tp2) {
		final String ownerTp1 = ElevatorPlugin.getPlugin().getDataFile().getString("Teleporters." + tp1 + "." + "Player");
		final String ownerTp2 = ElevatorPlugin.getPlugin().getDataFile().getString("Teleporters." + tp2 + "." + "Player");
		return ownerTp1.equals(player.getUniqueId().toString()) || ownerTp2.equals(player.getUniqueId().toString());
	}

	private static boolean checkTpOwner(Player player, String tp) {
		final String ownerTp = ElevatorPlugin.getPlugin().getDataFile().getString("Teleporters." + tp + "." + "Player");
		return ownerTp.equals(player.getUniqueId().toString());
	}

	public static boolean checkTpPerms(Player player) {
		int allowedTeleporters = 0;
		int teleporterCount = 0;

		if (player.hasPermission("elevator.teleporter.bypass") || player.hasPermission("elevator.admin")) {
			return true;
		}

		for (final PermissionAttachmentInfo perms : player.getEffectivePermissions()) {
			if (perms.getPermission().replaceAll("\\d", "").equals("elevator.teleporter.block.")) {
				if (Integer.parseInt(perms.getPermission().replaceAll("\\D", "")) > allowedTeleporters) {
					allowedTeleporters = Integer.parseInt(perms.getPermission().replaceAll("\\D", ""));
				}
			}
		}

		if (allowedTeleporters == 0) {
			return false;
		}

		for (final String tp : ElevatorPlugin.getPlugin().getDataFile().getConfigurationSection("Teleporters").getKeys(false)) {
			if (checkTpOwner(player, tp)) {
				teleporterCount++;
				if (teleporterCount >= (allowedTeleporters * 2)) {
					return false;
				}
			}
		}

		return true;
	}

	public static void createSection() {
		if (ElevatorPlugin.getPlugin().getDataFile().getKeys(false).isEmpty()) {
			ElevatorPlugin.getPlugin().getDataFile().createSection("Teleporters");
			saveDatafile();
		}
	}

	private static void saveDatafile() {
		try {
			File file = new File(ElevatorPlugin.getPlugin().getDataFolder(), "data.dat");
			ElevatorPlugin.getPlugin().getDataFile().save(file);
		} catch (final IOException e) {
			ElevatorPlugin.getPlugin().getLogger().severe(e.getMessage());
		}
	}

}
