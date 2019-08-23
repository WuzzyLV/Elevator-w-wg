package no.vestlandetmc.elevator.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;

import no.vestlandetmc.elevator.ElevatorPlugin;
import no.vestlandetmc.elevator.Mechanics;
import no.vestlandetmc.elevator.handler.MessageHandler;

public class TeleporterData {

	private static File file;

	private static HashMap<String, Long> warmUpTeleporter = new HashMap<>();

	public static void teleporterUsed(Player player, String tpName) {
		if(!warmUpTeleporter.containsKey(player.getUniqueId().toString())) {
			warmUpTeleporter.put(player.getUniqueId().toString(), (System.currentTimeMillis() / 1000));



			new BukkitRunnable() {
				@Override
				public void run() {
					if(!warmUpTeleporter.containsKey(player.getUniqueId().toString())) {
						this.cancel();
						return;
					}

					if(warmupTime(player) <= 0) {
						warmUpTeleporter.remove(player.getUniqueId().toString());
						player.teleport(getTeleportLoc(tpName));
						particleTeleporter(player);
						player.getWorld().playSound(player.getLocation(), "minecraft:" + Config.TP_SOUND, 1.0F, 0.7F);
						MessageHandler.sendAction(player, "&bTeleportation initialized");
						this.cancel();
						return;
					}
					particleTeleporter(player);
					MessageHandler.sendAction(player, "&bPlease wait &9" + warmupTime(player) + " &bseconds while the teleporter initialize! Don't move!");
				}

			}.runTaskTimer(ElevatorPlugin.getInstance(), 0L, 2L);
		}
	}

	public static boolean teleporterMove(Player player) {
		if(warmUpTeleporter.containsKey(player.getUniqueId().toString())) {
			warmUpTeleporter.remove(player.getUniqueId().toString());
			return true;
		}

		return false;
	}

	private static Long warmupTime(Player player) {
		if(warmUpTeleporter.containsKey(player.getUniqueId().toString())) {
			final long timeSeconds = 3 - ((System.currentTimeMillis() / 1000) - warmUpTeleporter.get(player.getUniqueId().toString()));
			return timeSeconds;
		}

		return 0L;
	}

	private static void particleTeleporter(Player player) {
		final Particle.DustOptions options = new Particle.DustOptions(Color.AQUA, 0.5F);

		for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
			final double radius = Math.sin(i);
			final double y = Math.cos(i) + 1.0D;
			for (double a = 0; a < Math.PI * 2; a+= Math.PI / 10) {
				final double x = Math.cos(a) * radius;
				final double z = Math.sin(a) * radius;
				final Location loc = player.getLocation().add(x, y, z);
				player.getWorld().spawnParticle(Particle.REDSTONE, loc, 0, 0, 1, 0, options);
			}
		}
	}

	public static void setTeleporter(Player player) {
		final double locX = player.getWorld().getBlockAt(player.getLocation()).getX();
		final double locY = player.getWorld().getBlockAt(player.getLocation()).getY() - 1.0D;
		final double locZ = player.getWorld().getBlockAt(player.getLocation()).getZ();
		final String world = player.getLocation().getWorld().getName();

		if (!Mechanics.standOnBlock(player, player.getWorld(), Config.TP_BLOCK_TYPE)) {
			MessageHandler.sendMessage(player, "&4Not a valid teleporter block detected!");
			return;
		}

		int tpNumberMax = 1;

		if(ElevatorPlugin.getInstance().getDataFile().contains("Teleporters")) {
			if(!ElevatorPlugin.getInstance().getDataFile().getConfigurationSection("Teleporters").getKeys(false).isEmpty()) {
				while(true) {
					if(!ElevatorPlugin.getInstance().getDataFile().contains("Teleporters.TP" + tpNumberMax)) {
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

	private static void writeTpName(Player player, int tpNumberMax, double locX, double locY, double locZ, String world, String tpName) {
		if(!ElevatorPlugin.getInstance().getDataFile().contains("Teleporters.TP" + tpNumberMax)) {
			ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName + "." + "X", locX);
			ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName + "." + "Y", locY);
			ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName + "." + "Z", locZ);
			ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName + "." + "World", world);
			ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName + "." + "Player", player.getUniqueId().toString());

			MessageHandler.sendMessage(player, "&eTeleporter has been added as &6" + tpName);

			saveDatafile();
		}
	}


	public static Location getTeleportLoc(String tpName) {
		if (ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tpName + "." + "Destination")) {
			final String tpTo = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tpName + "." + "Destination");
			final double locX = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tpTo + "." + "X") + 0.5D;
			final double locY = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tpTo + "." + "Y") + 1.0D;
			final double locZ = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tpTo + "." + "Z") + 0.5D;
			final World world = Bukkit.getWorld(ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tpTo + "." + "World"));
			final Location tpLoc  = new Location(world, locX, locY, locZ);

			return tpLoc;
		}

		return null;
	}

	public static String getTeleporter(Location loc) {
		if(!(ElevatorPlugin.getInstance().getDataFile().getKeys(false).toArray().length == 0) ||
				!(ElevatorPlugin.getInstance().getDataFile().getConfigurationSection("Teleporters").getKeys(false).toArray().length == 0)) {
			for(final String tp : ElevatorPlugin.getInstance().getDataFile().getConfigurationSection("Teleporters").getKeys(false)) {
				final double locX = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tp + "." + "X");
				final double locY = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tp + "." + "Y");
				final double locZ = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tp + "." + "Z");
				final World world = Bukkit.getWorld(ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tp + ".World"));

				final Location tpLoc = new Location(world, locX, locY, locZ);

				if(tpLoc.toString().equals(loc.toString())) {
					return tp;
				}
			}
		}

		return null;
	}

	public static void linkTeleporter(Player player, String tp1, String tp2) {
		if (!ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tp1.toUpperCase()) &&
				!ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tp2.toUpperCase())) {
			MessageHandler.sendMessage(player, "&cOne or more teleporters does not exist. Check for spelling errors.");
			return;
		}

		if (!checkTpOwner(player, tp1, tp2)) {
			MessageHandler.sendMessage(player, "&cYou have no rights to link those teleporters");
			return;
		}

		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tp1.toUpperCase() + "." + "Destination", tp2.toUpperCase());
		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tp2.toUpperCase() + "." + "Destination", tp1.toUpperCase());

		saveDatafile();
	}

	public static void deleteTeleporter(Player player, String tpName) {
		if (!ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tpName.toUpperCase())) {
			MessageHandler.sendMessage(player, "&cOne or more teleporters does not exist. Check for spelling errors.");
			return;
		}

		if (!checkTpOwner(player, tpName)) {
			MessageHandler.sendMessage(player, "&cYou have no rights to delete this teleporter");
			return;
		}

		if (ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tpName.toUpperCase() + ".Destination")) {
			final String tpName2 = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tpName + ".Destination");
			ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName2 + ".Destination", null);
		}

		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName.toUpperCase(), null);

		MessageHandler.sendMessage(player, "&eTeleporter &6" + tpName.toUpperCase() + " &ehas been removed");

		saveDatafile();
	}

	public static void deleteTeleporter(String tpName) {
		if (!ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tpName.toUpperCase())) {
			return;
		}

		if (ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tpName.toUpperCase() + ".Destination")) {
			final String tpName2 = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tpName + ".Destination");
			ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName2 + ".Destination", null);
		}

		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName.toUpperCase(), null);

		saveDatafile();
	}

	public static void unlinkTeleporter(Player player, String tpName) {
		if (!ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tpName.toUpperCase())) {
			MessageHandler.sendMessage(player, "&cOne or more teleporters does not exist. Check for spelling errors.");
			return;
		}

		if (!checkTpOwner(player, tpName)) {
			MessageHandler.sendMessage(player, "&cYou have no rights to delete this teleporter");
			return;
		}

		final String tpName2 = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tpName.toUpperCase() + "." + "Destination");

		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName.toUpperCase() + "." + "Destination", null);
		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName2.toUpperCase() + "." + "Destination", null);

		saveDatafile();
	}

	public static void listTP(Player player) {
		MessageHandler.sendMessage(player, "&e---- ====== [ &6Your teleporters &e] ====== ----");
		for(final String tp : ElevatorPlugin.getInstance().getDataFile().getConfigurationSection("Teleporters").getKeys(false)) {
			final double locX = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tp + "." + "X");
			final double locY = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tp + "." + "Y");
			final double locZ = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tp + "." + "Z");
			final String world = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tp + "." + "World");
			String link;
			if(ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tp + "." + "Destination")) {
				link = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tp + "." + "Destination");
			} else {
				link = null;
			}

			if(!checkTpOwner(player, tp)) {
				continue;
			}
			if(link == null) {
				MessageHandler.sendMessage(player, "&6" + tp + " &eX:&6" + locX + " &eY:&6" + locY + " &eZ:&6" + locZ + " &ein world &6" + world);
			} else {
				MessageHandler.sendMessage(player, "&6" + tp + " &eX:&6" + locX + " &eY:&6" + locY + " &eZ:&6" + locZ + " &ein world &6" + world + " &e==> &6" + link);
			}

		}
	}

	private static boolean checkTpOwner(Player player, String tp1, String tp2) {
		final String ownerTp1 = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tp1.toUpperCase() + "." + "Player");
		final String ownerTp2 = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tp2.toUpperCase() + "." + "Player");

		if (!ownerTp1.equals(player.getUniqueId().toString()) && !ownerTp2.equals(player.getUniqueId().toString())) {
			return false;
		}

		return true;
	}

	private static boolean checkTpOwner(Player player, String tp) {
		final String ownerTp = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tp.toUpperCase() + "." + "Player");

		if (ownerTp.equals(player.getUniqueId().toString())) {
			return true;
		}

		return false;
	}

	public static boolean checkTpPerms(Player player) {
		int allowedTeleporters = 0;
		int teleporterCount = 0;
		for(final PermissionAttachmentInfo perms : player.getEffectivePermissions()) {
			if(perms.getPermission().replaceAll("\\d", "").equals("elevator.teleporters.")) {
				if(Integer.parseInt(perms.getPermission().replaceAll("\\D", "")) > allowedTeleporters) {
					allowedTeleporters = Integer.parseInt(perms.getPermission().replaceAll("\\D", ""));
				}
			}
		}

		for(final String tp : ElevatorPlugin.getInstance().getDataFile().getConfigurationSection("Teleporters").getKeys(false)) {
			if(checkTpOwner(player, tp)) {
				teleporterCount++;
				if(teleporterCount >= (allowedTeleporters * 2)) {
					return false;
				}
			}
		}

		return true;
	}

	private static void saveDatafile() {
		try {
			file = new File(ElevatorPlugin.getInstance().getDataFolder(), "data.dat");
			ElevatorPlugin.getInstance().getDataFile().save(file);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
