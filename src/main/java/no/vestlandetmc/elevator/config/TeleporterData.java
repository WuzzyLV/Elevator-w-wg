package no.vestlandetmc.elevator.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
						if(Config.TP_PARTICLE_ENABLE) { particleTeleporter(player); }
						player.getWorld().playSound(player.getLocation(), "minecraft:" + Config.TP_SOUND, 1.0F, 0.7F);
						MessageHandler.sendAction(player, Config.TP_LOCAL_INIT);
						this.cancel();
						return;
					}
					if(Config.TP_PARTICLE_ENABLE) { particleTeleporter(player); }
					MessageHandler.sendAction(player, MessageHandler.placeholders(Config.TP_LOCAL_WARMUP, warmupTime(player).toString(), null, null, null));
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
		if(Config.TP_WARMUP_ENABLE) { return 0L; }
		if(warmUpTeleporter.containsKey(player.getUniqueId().toString())) {
			final long timeSeconds = Config.TP_WARMUP_TIME - ((System.currentTimeMillis() / 1000) - warmUpTeleporter.get(player.getUniqueId().toString()));
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
			MessageHandler.sendMessage(player, Config.TP_LOCAL_UNVALID);
			return;
		}

		final Location loc  = new Location(Bukkit.getWorld(world), locX, locY, locZ);

		if(teleporterExist(loc)) {
			MessageHandler.sendMessage(player, Config.TP_LOCAL_EXIST);
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

			MessageHandler.sendMessage(player, MessageHandler.placeholders(Config.TP_LOCAL_ADDED, null, tpName, null, null));

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

	private static boolean teleporterExist(Location loc) {
		if(!(ElevatorPlugin.getInstance().getDataFile().getKeys(false).toArray().length == 0) ||
				!(ElevatorPlugin.getInstance().getDataFile().getConfigurationSection("Teleporters").getKeys(false).toArray().length == 0)) {
			for(final String tp : ElevatorPlugin.getInstance().getDataFile().getConfigurationSection("Teleporters").getKeys(false)) {
				final double locX = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tp + "." + "X");
				final double locY = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tp + "." + "Y");
				final double locZ = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tp + "." + "Z");
				final World world = Bukkit.getWorld(ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tp + ".World"));

				final Location tpLoc = new Location(world, locX, locY, locZ);

				if(tpLoc.toString().equals(loc.toString())) {
					return true;
				}
			}
		}

		return false;
	}

	public static void linkTeleporter(Player player, String tp1, String tp2) {
		if(!ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tp1) ||
				!ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tp2)) {
			MessageHandler.sendMessage(player, Config.TP_LOCAL_UNEXIST);
			return;
		}

		if(ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tp1 + ".Destination") ||
				ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tp2 + ".Destination")) {
			MessageHandler.sendMessage(player, Config.TP_LOCAL_LINKEXIST);
			return;
		}

		if (!checkTpOwner(player, tp1.toUpperCase(), tp2.toUpperCase())) {
			MessageHandler.sendMessage(player, Config.TP_LOCAL_NOOWNER);
			return;
		}

		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tp1 + ".Destination", tp2);
		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tp2 + ".Destination", tp1);

		MessageHandler.sendMessage(player, MessageHandler.placeholders(Config.TP_LOCAL_LINKED, null, null, tp1, tp2));

		saveDatafile();
	}

	public static void deleteTeleporter(Player player, String tpName) {
		if (!ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tpName)) {
			MessageHandler.sendMessage(player, Config.TP_LOCAL_UNEXIST);
			return;
		}

		if (!checkTpOwner(player, tpName)) {
			MessageHandler.sendMessage(player, Config.TP_LOCAL_NOOWNER);
			return;
		}

		if (ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tpName + ".Destination")) {
			final String tpName2 = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tpName + ".Destination");
			ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName2 + ".Destination", null);
		}

		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName, null);

		MessageHandler.sendMessage(player, MessageHandler.placeholders(Config.TP_LOCAL_REMOVED, null, tpName, null, null));

		saveDatafile();
	}

	public static void deleteTeleporter(String tpName) {
		if (!ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tpName)) {
			return;
		}

		if (ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tpName + ".Destination")) {
			final String tpName2 = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tpName + ".Destination");
			ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName2 + ".Destination", null);
		}

		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName, null);

		saveDatafile();
	}

	public static void unlinkTeleporter(Player player, String tpName) {
		if (!ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tpName)) {
			MessageHandler.sendMessage(player, Config.TP_LOCAL_UNEXIST);
			return;
		}

		if (!checkTpOwner(player, tpName)) {
			MessageHandler.sendMessage(player, Config.TP_LOCAL_NOOWNER);
			return;
		}

		if(ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tpName + "." + "Destination")) {
			final String tpName2 = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tpName + "." + "Destination");

			ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName + "." + "Destination", null);
			ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName2 + "." + "Destination", null);

			MessageHandler.sendMessage(player, MessageHandler.placeholders(Config.TP_LOCAL_UNLINKED, null, null, tpName, tpName2));

			saveDatafile();
		} else { MessageHandler.sendMessage(player, MessageHandler.placeholders(Config.TP_LOCAL_NODEST, null, tpName, null, null)); }
	}

	public static void listTP(Player player) {
		int count = 0;

		MessageHandler.sendMessage(player, Config.TP_LOCAL_LISTHEADER);
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

			if(!checkTpOwner(player, tp.toUpperCase())) {
				continue;
			} else { count++; }

			if(link == null) {
				MessageHandler.sendMessage(player, "&6" + tp + " &eX:&6" + locX + " &eY:&6" + locY + " &eZ:&6" + locZ + " &ein world &6" + world);
			} else {
				MessageHandler.sendMessage(player, "&6" + tp + " &eX:&6" + locX + " &eY:&6" + locY + " &eZ:&6" + locZ + " &ein world &6" + world + " &e==> &6" + link);
			}

		}

		if(count == 0) { MessageHandler.sendMessage(player, Config.TP_LOCAL_LISTNOTP); }
	}

	public static List<String> tabCompleteTp(Player player) {
		final List<String> teleportList = new ArrayList<>();
		for(final String tp : ElevatorPlugin.getInstance().getDataFile().getConfigurationSection("Teleporters").getKeys(false)) {
			if(checkTpOwner(player, tp.toUpperCase())) {
				teleportList.add(tp.toLowerCase());
			}
		}
		return teleportList;
	}

	private static boolean checkTpOwner(Player player, String tp1, String tp2) {
		final String ownerTp1 = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tp1 + "." + "Player");
		final String ownerTp2 = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tp2 + "." + "Player");

		if (!ownerTp1.equals(player.getUniqueId().toString()) && !ownerTp2.equals(player.getUniqueId().toString())) {
			return false;
		}

		return true;
	}

	private static boolean checkTpOwner(Player player, String tp) {
		final String ownerTp = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tp + "." + "Player");

		if (ownerTp.equals(player.getUniqueId().toString())) {
			return true;
		}

		return false;
	}

	public static boolean checkTpPerms(Player player) {
		int allowedTeleporters = 0;
		int teleporterCount = 0;
		for(final PermissionAttachmentInfo perms : player.getEffectivePermissions()) {
			if(perms.getPermission().replaceAll("\\d", "").equals("elevator.teleporter.set.")) {
				if(Integer.parseInt(perms.getPermission().replaceAll("\\D", "")) > allowedTeleporters) {
					allowedTeleporters = Integer.parseInt(perms.getPermission().replaceAll("\\D", ""));
				}
			}
		}

		if(allowedTeleporters == 0) { return false; }

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

	public static void createSection() {
		if(ElevatorPlugin.getInstance().getDataFile().getKeys(false).isEmpty()) {
			ElevatorPlugin.getInstance().getDataFile().createSection("Teleporters");
			saveDatafile();
		}
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
