package no.vestlandetmc.elevator.Listener;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import no.vestlandetmc.elevator.Mechanics;
import no.vestlandetmc.elevator.config.Config;
import no.vestlandetmc.elevator.config.TeleporterData;
import no.vestlandetmc.elevator.handler.Cooldown;
import no.vestlandetmc.elevator.handler.GDHandler;
import no.vestlandetmc.elevator.handler.GPHandler;
import no.vestlandetmc.elevator.handler.MessageHandler;
import no.vestlandetmc.elevator.handler.WGHandler;
import no.vestlandetmc.elevator.hooks.GriefDefenderHook;
import no.vestlandetmc.elevator.hooks.GriefPreventionHook;
import no.vestlandetmc.elevator.hooks.WorldGuardHook;

public class TeleporterListener implements Listener {


	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
			if (TeleporterData.teleporterMove(e.getPlayer())) {
				MessageHandler.sendAction(e.getPlayer(), Config.TP_LOCALE_CANCELLED);
			}
		}
	}

	@EventHandler
	public void onTeleporterBreak(BlockBreakEvent e) {
		final Player player = e.getPlayer();
		final Location loc = player.getLocation();

		if(e.getBlock().getType() == Config.TP_BLOCK_TYPE) {
			if(!GPHandler.haveBuildTrust(player, loc, e.getBlock().getType())) { return; }
			if(!WGHandler.haveTrustTP(player)) { return; }
			if(!GDHandler.haveBuildTrust(player, loc)) { return; }

			final String tpName = TeleporterData.getTeleporter(e.getBlock().getLocation());

			if(tpName == null) { return; }

			TeleporterData.deleteTeleporter(tpName);
		}
	}

	@EventHandler
	public void onPlayerTeleport(PlayerToggleSneakEvent e) {
		if (!e.getPlayer().isSneaking()) {
			if(Mechanics.standOnBlock(e.getPlayer(), e.getPlayer().getWorld(), Config.TP_BLOCK_TYPE)) {
				if(e.getPlayer().hasPermission("elevator.teleporter.use")) {
					final double locX = e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation()).getX();
					final double locY = e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation().add(0.0D, -1.0D, 0.0D)).getY();
					final double locZ = e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation()).getZ();
					final World world = e.getPlayer().getLocation().getWorld();

					final Location loc = new Location(world, locX, locY, locZ);

					final String tpName = TeleporterData.getTeleporter(loc);
					final Location locTo = TeleporterData.getTeleportLoc(tpName);

					if(locTo == null) { return; }

					if(!(tpName == null)) {
						if(Mechanics.dangerBlock(locTo)) {
							MessageHandler.sendMessage(e.getPlayer(), Config.TP_LOCALE_DANGER);
							return;
						} else if (Config.COOLDOWN_ENABLED) {
							if (Cooldown.elevatorUsed(e.getPlayer())) {
								return;
							}
						}
						if(GriefPreventionHook.gpHook) { if(!GPHandler.haveTrust(e.getPlayer())) return; }
						if(WorldGuardHook.wgHook) { if(!WGHandler.haveTrust(e.getPlayer())) return; }
						if(GriefDefenderHook.gdHook) { if(!GDHandler.haveTrust(e.getPlayer())) return; }

						if(TeleporterData.getTeleportLoc(tpName) != null) {
							TeleporterData.teleporterUsed(e.getPlayer(), tpName);
						}
					}
				}
			}
		}
	}
}
