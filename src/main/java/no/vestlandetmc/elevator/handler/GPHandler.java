package no.vestlandetmc.elevator.handler;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import no.vestlandetmc.elevator.ElevatorPlugin;

public class GPHandler {

	public static boolean haveTrust(Player player) {
		final Location loc = player.getLocation();
		final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(loc, true, null);

		if(claim == null) {
			return true;
		}

		final String accessDenied = claim.allowAccess(player);
		if(accessDenied != null) {
			if(!MessageHandler.spamMessageClaim.contains(player.getUniqueId().toString())) {
				MessageHandler.sendMessage(player, "&c" + accessDenied);
				MessageHandler.spamMessageClaim.add(player.getUniqueId().toString());

				new BukkitRunnable() {
					@Override
					public void run() {
						MessageHandler.spamMessageClaim.remove(player.getUniqueId().toString());
					}

				}.runTaskLater(ElevatorPlugin.getInstance(), 20L);
			}
			return false;
		} else {
			return true;
		}

	}

	public static boolean haveTrust(Player player, Location loc) {
		final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(loc, true, null);

		if(claim == null) {
			return true;
		}

		final String accessDenied = claim.allowAccess(player);
		if(accessDenied != null) {
			if(!MessageHandler.spamMessageClaim.contains(player.getUniqueId().toString())) {
				MessageHandler.sendMessage(player, "&c" + accessDenied);
				MessageHandler.spamMessageClaim.add(player.getUniqueId().toString());

				new BukkitRunnable() {
					@Override
					public void run() {
						MessageHandler.spamMessageClaim.remove(player.getUniqueId().toString());
					}

				}.runTaskLater(ElevatorPlugin.getInstance(), 20L);
			}
			return false;
		} else {
			return true;
		}

	}

}
