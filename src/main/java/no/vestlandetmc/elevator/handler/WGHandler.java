package no.vestlandetmc.elevator.handler;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import no.vestlandetmc.elevator.ElevatorPlugin;

public class WGHandler {

	public static boolean haveTrust(Player player) {

		final LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
		final Location loc = localPlayer.getLocation();

		final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		final RegionQuery query = container.createQuery();
		final ApplicableRegionSet set = query.getApplicableRegions(loc);

		if(!player.isOp()) {
			if(!set.isMemberOfAll(localPlayer)) {
				if(!set.testState(localPlayer, Flags.USE)) {
					sendErrorMessage(player);
					return false;
				} else {
					return true;
				}
			}
		}

		return true;
	}

	public static boolean haveTrust(Player player, Location loc) {

		final LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

		final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		final RegionQuery query = container.createQuery();
		final ApplicableRegionSet set = query.getApplicableRegions(loc);

		if(!player.isOp()) {
			if(!set.isMemberOfAll(localPlayer)) {
				if(!set.testState(localPlayer, Flags.USE)) {
					sendErrorMessage(player);
					return false;
				} else {
					return true;
				}
			}
		}

		return true;
	}

	private static void sendErrorMessage(Player player) {
		if(!MessageHandler.spamMessageClaim.contains(player.getUniqueId().toString())) {
			MessageHandler.sendMessage(player, "&c&lHey! &7Sorry, but you can't use that elevator here.");
			MessageHandler.spamMessageClaim.add(player.getUniqueId().toString());

			new BukkitRunnable() {
				@Override
				public void run() {
					MessageHandler.spamMessageClaim.remove(player.getUniqueId().toString());
				}

			}.runTaskLater(ElevatorPlugin.getInstance(), 20L);
		}
	}

	public static boolean haveTrustTP(Player player) {

		final LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
		final Location loc = localPlayer.getLocation();

		final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		final RegionQuery query = container.createQuery();
		final ApplicableRegionSet set = query.getApplicableRegions(loc);

		if(!player.isOp()) {
			if(!set.isMemberOfAll(localPlayer)) {
				if(!set.testState(localPlayer, Flags.BLOCK_BREAK) || !set.testState(localPlayer, Flags.BUILD) || !set.testState(localPlayer, Flags.PASSTHROUGH)) {
					return false;
				} else {
					return true;
				}
			}
		}

		return true;
	}
}
