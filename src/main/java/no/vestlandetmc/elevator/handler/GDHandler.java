package no.vestlandetmc.elevator.handler;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.griefdefender.api.Core;
import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.claim.TrustTypes;
import com.griefdefender.lib.flowpowered.math.vector.Vector3i;

import no.vestlandetmc.elevator.ElevatorPlugin;

public class GDHandler {

	public static boolean haveTrust(Player player) {
		final Location loc = player.getLocation();
		final Vector3i vector = Vector3i.from(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		final Core gd = GriefDefender.getCore();
		final Claim claim = gd.getClaimManager(loc.getWorld().getUID()).getClaimAt(vector);

		if(claim.isWilderness()) {	return true; }

		if(claim.getUserTrusts(TrustTypes.ACCESSOR).contains(player.getUniqueId())) {
			if(!MessageHandler.spamMessageClaim.contains(player.getUniqueId().toString())) {
				MessageHandler.sendMessage(player, "&cYou do not have accesstrust in this claim.");
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
		final Vector3i vector = Vector3i.from(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		final Core gd = GriefDefender.getCore();
		final Claim claim = gd.getClaimManager(loc.getWorld().getUID()).getClaimAt(vector);

		if(claim.isWilderness()) {	return true; }

		if(claim.getUserTrusts(TrustTypes.ACCESSOR).contains(player.getUniqueId())) {
			if(!MessageHandler.spamMessageClaim.contains(player.getUniqueId().toString())) {
				MessageHandler.sendMessage(player, "&cYou do not have accesstrust in this claim.");
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

	public static boolean haveBuildTrust(Player player, Location loc) {
		if (ElevatorPlugin.getInstance().getServer().getPluginManager().getPlugin("GriefDefender") == null) { return false; }

		final Vector3i vector = Vector3i.from(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		final Core gd = GriefDefender.getCore();
		final Claim claim = gd.getClaimManager(loc.getWorld().getUID()).getClaimAt(vector);

		if(claim.isWilderness()) {	return true; }

		if(claim.getUserTrusts(TrustTypes.BUILDER).contains(player.getUniqueId())) {
			return false;
		} else {
			return true;
		}

	}

}
