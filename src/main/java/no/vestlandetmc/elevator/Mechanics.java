package no.vestlandetmc.elevator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import no.vestlandetmc.elevator.config.Config;
import no.vestlandetmc.elevator.handler.MessageHandler;

public class Mechanics {

	public static boolean dangerBlock;
	public static double tpCoordinate;

	public static boolean detectBlockUp(Player player, World world, Material m) {
		if(standOnBlock(player, world, m)) {
			if(elevatorBlockExistUp(player, world)) {
				return true;
			}
		}

		return false;
	}

	public static boolean detectBlockDown(Player player, World world, Material m) {
		if(standOnBlock(player, world, m)) {
			if(elevatorBlockExistDown(player, world)) {
				return true;
			}
		}

		return false;
	}

	public static boolean standOnBlock(Player player, World world, Material m) {
		if(world.getBlockAt(player.getLocation().add(0.0D, -1.0D, 0.0D)).getType() == m) {
			return true;
		}

		return false;
	}

	private static boolean elevatorBlockExistUp(Player player, World world) {
		for (double y = 2; y <= 50; y++) {
			if(world.getBlockAt(player.getLocation().add(0.0D, y, 0.0D)).getType() == Config.BLOCK_TYPE) {
				if(dangerBlock(player, world, y)) {
					MessageHandler.sendAction(player, Config.ELEVATOR_LOCALE_DANGER);
					return false;
				} else {
					tpCoordinate = y;
					return true;
				}
			}
		}

		return false;
	}

	private static boolean elevatorBlockExistDown(Player player, World world) {
		for(double y = -2; y >= -50; y--) {
			if(world.getBlockAt(player.getLocation().add(0.0D, y, 0.0D)).getType() == Config.BLOCK_TYPE) {
				if(dangerBlock(player, world, y)) {
					MessageHandler.sendAction(player, Config.ELEVATOR_LOCALE_DANGER);
					return false;
				} else {
					tpCoordinate = y;
					return true;
				}
			}
		}

		return false;
	}

	private static boolean dangerBlock(Player player, World world, double y) {
		if(world.getBlockAt(player.getLocation().add(0.0D, (y + 1), 0.0D)).getType().isSolid() ||
				world.getBlockAt(player.getLocation().add(0.0D, (y + 1), 0.0D)).getType() == Material.LAVA ||
				world.getBlockAt(player.getLocation().add(0.0D, (y + 2), 0.0D)).getType().isSolid() ||
				world.getBlockAt(player.getLocation().add(0.0D, (y + 2), 0.0D)).getType() == Material.LAVA) {
			return true;
		}

		return false;

	}

	public static boolean dangerBlock(Location loc) {
		if(loc.getWorld().getBlockAt(loc).getType().isSolid() ||
				loc.getWorld().getBlockAt(loc).getType() == Material.LAVA ||
				loc.getWorld().getBlockAt(loc.add(0.0D, 1.0D, 0.0D)).getType().isSolid() ||
				loc.getWorld().getBlockAt(loc.add(0.0D, 1.0D, 0.0D)).getType() == Material.LAVA) {
			return true;
		}

		return false;

	}

	public static boolean blockExistClose(BlockPlaceEvent e) {
		final World w = e.getPlayer().getWorld();
		if(w.getBlockAt(e.getBlockPlaced().getLocation().add(0.0D, +1.0D, 0.0D)).getType() == Config.BLOCK_TYPE ||
				w.getBlockAt(e.getBlockPlaced().getLocation().add(0.0D, +2.0D, 0.0D)).getType() == Config.BLOCK_TYPE ||
				w.getBlockAt(e.getBlockPlaced().getLocation().add(0.0D, -1.0D, 0.0D)).getType() == Config.BLOCK_TYPE ||
				w.getBlockAt(e.getBlockPlaced().getLocation().add(0.0D, -2.0D, 0.0D)).getType() == Config.BLOCK_TYPE) {
			return false;
		}

		return true;
	}

	public static void particles(Player player, Location loc) {
		if (Config.PARTICLE_ENABLED) {
			player.getWorld().spawnParticle(Config.PARTICLE_TYPE, loc, Config.PARTICLE_COUNT);
		}
	}

	public static void teleportUp(Player player) {
		player.teleport(player.getLocation().add(0.0D, (Mechanics.tpCoordinate + 1), 0.0D));
		player.playSound(player.getLocation(), "minecraft:" + Config.TP_SOUND, 1.0F, 0.7F);
	}

	public static void teleportDown(Player player) {
		player.teleport(player.getLocation().add(0.0D, (Mechanics.tpCoordinate + 1), 0.0D));
		player.playSound(player.getLocation(), "minecraft:" + Config.TP_SOUND, 1.0F, 0.7F);
	}
}
