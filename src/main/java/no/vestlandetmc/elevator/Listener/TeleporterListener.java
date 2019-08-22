package no.vestlandetmc.elevator.Listener;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import no.vestlandetmc.elevator.Mechanics;
import no.vestlandetmc.elevator.config.Config;
import no.vestlandetmc.elevator.config.TeleporterData;
import no.vestlandetmc.elevator.handler.Cooldown;
import no.vestlandetmc.elevator.handler.GPHandler;
import no.vestlandetmc.elevator.handler.WGHandler;
import no.vestlandetmc.elevator.hooks.GriefPreventionHook;
import no.vestlandetmc.elevator.hooks.WorldGuardHook;

public class TeleporterListener implements Listener {


	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {

	}

	@EventHandler
	public void onPlayerTeleport(PlayerToggleSneakEvent e) {
		if (!e.getPlayer().isSneaking()) {
			if(Mechanics.standOnBlock(e.getPlayer(), e.getPlayer().getWorld(), Config.TP_BLOCK_TYPE)) {
				if (e.getPlayer().hasPermission("elevator.teleporter.use")) {
					if (Config.COOLDOWN_ENABLED) {
						if (Cooldown.elevatorUsed(e.getPlayer())) {
							return;
						}
					}

					final double locX = e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation()).getX();
					final double locY = e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation().add(0.0D, -1.0D, 0.0D)).getY();
					final double locZ = e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation()).getZ();
					final World world = e.getPlayer().getLocation().getWorld();

					final Location loc = new Location(world, locX, locY, locZ);

					final String tpName = TeleporterData.getTeleporter(loc);

					if(GriefPreventionHook.gpHook) { if(!GPHandler.haveTrust(e.getPlayer()) && !GPHandler.haveTrust(e.getPlayer())) return; }
					if(WorldGuardHook.wgHook) { if(!WGHandler.haveTrust(e.getPlayer()) && !WGHandler.haveTrust(e.getPlayer())) return; }

					e.getPlayer().teleport(TeleporterData.getTeleportLoc(tpName));

				}
			}
		}
	}
}
