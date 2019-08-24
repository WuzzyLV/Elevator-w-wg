package no.vestlandetmc.elevator.Listener;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import no.vestlandetmc.elevator.Mechanics;
import no.vestlandetmc.elevator.config.Config;
import no.vestlandetmc.elevator.handler.Cooldown;
import no.vestlandetmc.elevator.handler.GPHandler;
import no.vestlandetmc.elevator.handler.MessageHandler;
import no.vestlandetmc.elevator.handler.UpdateNotification;
import no.vestlandetmc.elevator.handler.WGHandler;
import no.vestlandetmc.elevator.hooks.GriefPreventionHook;
import no.vestlandetmc.elevator.hooks.WorldGuardHook;

public class ElevatorListener implements Listener {

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		if (e.getPlayer().hasPermission("elevator.use")) {
			if (!e.getPlayer().isOnGround() && e.getPlayer().getVelocity().getY() > 0.0D) {
				final World w = e.getPlayer().getWorld();
				final Location loc = e.getPlayer().getLocation();
				if(Mechanics.detectBlockUp(e.getPlayer(), w, Config.BLOCK_TYPE)) {
					if(Config.COOLDOWN_ENABLED) {
						if(Cooldown.elevatorUsed(e.getPlayer())) {
							return;
						}
					}
					if(GriefPreventionHook.gpHook) { if(!GPHandler.haveTrust(e.getPlayer())) return; }
					if(WorldGuardHook.wgHook) { if(!WGHandler.haveTrust(e.getPlayer())) return; }
					Mechanics.teleportUp(e.getPlayer());
					MessageHandler.sendAction(e.getPlayer(), Config.ELEVATOR_LOCALE_UP);
					Mechanics.particles(e.getPlayer(), loc);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent e) {
		if (e.getPlayer().hasPermission("elevator.use")) {
			if (!e.getPlayer().isSneaking()) {
				final World w = e.getPlayer().getWorld();
				final Location loc = e.getPlayer().getLocation();
				if(Mechanics.detectBlockDown(e.getPlayer(), w, Config.BLOCK_TYPE)) {
					if(Config.COOLDOWN_ENABLED) {
						if(Cooldown.elevatorUsed(e.getPlayer())) {
							return;
						}
					}
					if(GriefPreventionHook.gpHook) { if(!GPHandler.haveTrust(e.getPlayer())) return; }
					if(WorldGuardHook.wgHook) { if(!WGHandler.haveTrust(e.getPlayer())) return; }
					Mechanics.teleportDown(e.getPlayer());
					MessageHandler.sendAction(e.getPlayer(), Config.ELEVATOR_LOCALE_DOWN);
					Mechanics.particles(e.getPlayer(), loc);
				}
			}
		}
	}

	@EventHandler
	public void BlockPlaceEvent(BlockPlaceEvent e) {
		if(e.getPlayer().hasPermission("elevator.use")) {
			final World w = e.getPlayer().getWorld();
			if(Mechanics.blockExistClose(e)) {
				if (e.getBlockPlaced().getType() == Config.BLOCK_TYPE) {
					for (double y = 50.0D; y > -51.0D; y--) {
						if (y + e.getBlockPlaced().getLocation().getY() > e.getBlockPlaced().getLocation().getY() + 2.0D ||
								y + e.getBlockPlaced().getLocation().getY() < e.getBlockPlaced().getLocation().getY() - 2.0D) {
							if (w.getBlockAt(e.getBlockPlaced().getLocation().add(0.0D, y, 0.0D)).getType() == Config.BLOCK_TYPE) {
								MessageHandler.sendAction(e.getPlayer(), Config.ELEVATOR_LOCALE_ACTIVATED);
								e.getPlayer().playSound(e.getPlayer().getLocation(), "minecraft:" + Config.SOUND_ACTIVATED, 1.0F, 1.0F);
								break;
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent p) {
		final Player player = p.getPlayer();

		if(player.isOp()) {
			if(UpdateNotification.isUpdateAvailable()) {
				MessageHandler.sendMessage(player, "&a------------------------------------");
				MessageHandler.sendMessage(player, "&aElevator is outdated. Update is available!");
				MessageHandler.sendMessage(player, "&aYour version is &a&l " + UpdateNotification.getCurrentVersion() + " &aand can be updated to version &a&l" + UpdateNotification.getLatestVersion());
				MessageHandler.sendMessage(player, "&aGet the new update at https://www.spigotmc.org/resources/" + UpdateNotification.getProjectId());
				MessageHandler.sendMessage(player, "&a------------------------------------");
			}
		}
	}
}
