package no.vestlandetmc.elevator.handler;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import no.vestlandetmc.elevator.ElevatorPlugin;
import no.vestlandetmc.elevator.config.Config;

public class Cooldown {
	private static HashMap<String, Long> cooldownElevator = new HashMap<>();

	public static boolean elevatorUsed(Player player) {
		if(!cooldownElevator.containsKey(player.getUniqueId().toString())) {
			cooldownElevator.put(player.getUniqueId().toString(), (System.currentTimeMillis() / 1000));

			new BukkitRunnable() {
				@Override
				public void run() {
					cooldownElevator.remove(player.getUniqueId().toString());
				}

			}.runTaskLater(ElevatorPlugin.getInstance(), (20L * Config.COOLDOWN_TIME));

			return false;

		} else {
			spamPrevent(player);
			return true;
		}

	}

	private static void spamPrevent(Player player) {
		if(!MessageHandler.spamMessageCooldown.contains(player.getUniqueId().toString())) {
			MessageHandler.sendMessage(player, MessageHandler.placeholders(Config.ELEVATOR_COOLDOWN, getCooldownTime(player), null, null, null));
			MessageHandler.spamMessageCooldown.add(player.getUniqueId().toString());

			new BukkitRunnable() {
				@Override
				public void run() {
					MessageHandler.spamMessageCooldown.remove(player.getUniqueId().toString());
				}

			}.runTaskLater(ElevatorPlugin.getInstance(), 20L);
		}
	}

	private static String getCooldownTime(Player player) {
		final long timeSeconds = Config.COOLDOWN_TIME - ((System.currentTimeMillis() / 1000) - cooldownElevator.get(player.getUniqueId().toString()));

		return Long.toString(timeSeconds);
	}

}
