package no.vestlandetmc.elevator.handler;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import no.vestlandetmc.elevator.ElevatorPlugin;

public class MessageHandler {

	public static ArrayList<String> spamMessageClaim = new ArrayList<>();
	public static ArrayList<String> spamMessageCooldown = new ArrayList<>();

	public static void sendAction(Player player, String message) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(colorize(message)));
	}

	public static void sendMessage(Player player, String message) {
		player.sendMessage(colorize(message));
	}

	public static void sendConsole(String message) {
		ElevatorPlugin.getInstance().getServer().getConsoleSender().sendMessage(colorize(message));
	}

	public static String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static String placeholders(String message, String time) {
		final String converted = message.replaceAll("%time%", time);

		return converted;

	}

}
