package no.vestlandetmc.elevator.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import no.vestlandetmc.elevator.ElevatorPlugin;
import no.vestlandetmc.elevator.Mechanics;
import no.vestlandetmc.elevator.handler.MessageHandler;

public class TeleporterData {

	private static File file;

	public static void setTeleporter(Player player) {
		final double locX = player.getWorld().getBlockAt(player.getLocation()).getX();
		final double locY = player.getWorld().getBlockAt(player.getLocation()).getY() - 1.0D;
		final double locZ = player.getWorld().getBlockAt(player.getLocation()).getZ();
		final String world = player.getLocation().getWorld().getName();
		final String playerName = player.getUniqueId().toString();
		int tpNumberMax = 1;

		if (!Mechanics.standOnBlock(player, player.getWorld(), Config.TP_BLOCK_TYPE)) {
			MessageHandler.sendMessage(player, "&4Not a valid teleporter block detected!");
			return;
		}

		if(ElevatorPlugin.getInstance().getDataFile().getKeys(false).toArray().length >= 1) {
			tpNumberMax = ElevatorPlugin.getInstance().getDataFile().getConfigurationSection("Teleporters").getKeys(false).size() + 1;
		}

		final String tpName = "TP" + tpNumberMax;

		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName + "." + "X", locX);
		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName + "." + "Y", locY);
		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName + "." + "Z", locZ);
		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName + "." + "World", world);
		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName + "." + "Player", playerName);

		MessageHandler.sendMessage(player, "&eTeleporter has been added as &6" + tpName);

		saveDatafile();
	}

	public static Location getTeleportLoc(String tpName) {
		final String tpTo = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tpName + "." + "Destination");
		final double locX = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tpTo + "." + "X");
		final double locY = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tpTo + "." + "Y") + 1.0D;
		final double locZ = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tpTo + "." + "Z");
		final World world = Bukkit.getWorld(ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tpTo + "." + "World"));
		final Location tpLoc  = new Location(world, locX, locY, locZ);

		return tpLoc;
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

				MessageHandler.sendConsole("&aSjekker: " + tp + " <==> " + tpLoc + " ==> " + loc);

				if(tpLoc.toString().equals(loc.toString())) {
					MessageHandler.sendConsole("&cMatchet følgende: " + tp);
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

		if (ElevatorPlugin.getInstance().getDataFile().contains("Teleporters." + tpName.toUpperCase() + "Destination")) {
			final String tpName2 = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tpName + "." + "Destination");
			ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName2 + "." + "Destination", null);
		}

		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName.toUpperCase(), null);

		MessageHandler.sendMessage(player, "&eTeleporter &6" + tpName.toUpperCase() + " &ehas been removed");

		saveDatafile();
	}

	public static void unlinkTeleporter(Player player, String tpName) {
		if (!ElevatorPlugin.getInstance().getDataFile().contains(tpName)) {
			MessageHandler.sendMessage(player, "&cOne or more teleporters does not exist. Check for spelling errors.");
			return;
		}

		if (checkTpOwner(player, tpName)) {
			MessageHandler.sendMessage(player, "&cYou have no rights to delete this teleporter");
			return;
		}

		final String tpName2 = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tpName + "." + "Destination");

		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName + "." + "Destination", null);
		ElevatorPlugin.getInstance().getDataFile().set("Teleporters." + tpName2 + "." + "Destination", null);

		saveDatafile();
	}

	public static void listTP(Player player) {
		MessageHandler.sendMessage(player, "&e---- ====== [ &6Your teleporters &e] ====== ----");
		for(final String tp : ElevatorPlugin.getInstance().getDataFile().getConfigurationSection("Teleporters").getKeys(false)) {
			final double locX = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tp + "." + "X");
			final double locY = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tp + "." + "Y");
			final double locZ = ElevatorPlugin.getInstance().getDataFile().getDouble("Teleporters." + tp + "." + "Z");
			final String world = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tp + "." + "World");
			final String link = ElevatorPlugin.getInstance().getDataFile().getString("Teleporters." + tp + "." + "Destination");

			if(checkTpOwner(player, tp)) {
				continue;
			}

			MessageHandler.sendMessage(player, "&6" + tp + " " + locX + "," + locY + "," + locZ + "," + world + " ==> " + link);

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

	private static void saveDatafile() {
		try {
			file = new File(ElevatorPlugin.getInstance().getDataFolder(), "data.dat");
			ElevatorPlugin.getInstance().getDataFile().save(file);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
