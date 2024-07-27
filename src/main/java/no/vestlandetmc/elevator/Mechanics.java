package no.vestlandetmc.elevator;

import no.vestlandetmc.elevator.config.Config;
import no.vestlandetmc.elevator.handler.MessageHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Mechanics {

	public static boolean standOnBlock(Location location, Material material) {
		return location.getWorld().getBlockAt(location.add(0.0D, -1.0D, 0.0D)).getType() == material;
	}

	public static Location getElevatorLocationUp(Player player) {
		final int distance = Config.BLOCK_DISTANCE;
		for (double y = 2; y <= distance; y++) {
			final Block block = player.getWorld().getBlockAt(player.getLocation().add(0.0D, y, 0.0D));
			if (block.getType() == Config.BLOCK_TYPE) {
				if (dangerBlock(block.getLocation())) {
					MessageHandler.sendAction(player, Config.ELEVATOR_LOCALE_DANGER);
					return null;
				} else {
					final Location tpCoordinate = block.getLocation().add(0D, 1D, 0D);
					tpCoordinate.setPitch(player.getLocation().getPitch());
					tpCoordinate.setYaw(player.getLocation().getYaw());
					return tpCoordinate;
				}
			}
		}

		return null;
	}

	public static Location getElevatorLocationDown(Player player) {
		final int distance = -Config.BLOCK_DISTANCE;
		for (double y = -2; y >= distance; y--) {
			final Block block = player.getWorld().getBlockAt(player.getLocation().add(0.0D, y, 0.0D));
			if (block.getType() == Config.BLOCK_TYPE) {
				if (dangerBlock(block.getLocation())) {
					MessageHandler.sendAction(player, Config.ELEVATOR_LOCALE_DANGER);
					return null;
				} else {
					final Location tpCoordinate = block.getLocation().add(0D, 1D, 0D);
					tpCoordinate.setPitch(player.getLocation().getPitch());
					tpCoordinate.setYaw(player.getLocation().getYaw());
					return tpCoordinate;
				}
			}
		}

		return null;
	}

	public static boolean dangerBlock(Location location) {
		final Material getBlock1 = location.getWorld().getBlockAt(location.add(0.0D, 1.0D, 0.0D)).getType();
		final Material getBlock2 = location.getWorld().getBlockAt(location.add(0.0D, 2.0D, 0.0D)).getType();

		if (getBlock1.name().endsWith("SIGN") || getBlock2.name().endsWith("SIGN")) {
			return false;
		}

		return getBlock1.isSolid() || getBlock1 == Material.LAVA || getBlock2.isSolid() || getBlock2 == Material.LAVA;
	}

	public static void setParticles(Player player) {
		if (Config.PARTICLE_ENABLED) {
			final Location loc = player.getLocation().add(0.0D, 0.5D, 0.0D);
			player.getWorld().spawnParticle(Config.PARTICLE_TYPE, loc, Config.PARTICLE_COUNT);
		}
	}

	public static void teleport(Player player, Location tpCoordinate) {
		player.teleport(tpCoordinate);
		player.playSound(player.getLocation(), "minecraft:" + Config.TP_SOUND, 1.0F, 0.7F);
	}
}
