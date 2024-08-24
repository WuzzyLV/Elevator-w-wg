package no.vestlandetmc.elevator.handler;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import no.vestlandetmc.elevator.ElevatorPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicBoolean;

public class WGHandler {

	public static StateFlag ELEVATOR_FLAG;

	public static void registerFlag(){
		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
		try {
			StateFlag flag = new StateFlag("elevators", false);
			registry.register(flag);
			ELEVATOR_FLAG = flag;

		}catch (FlagConflictException e){
			Flag<?> existing = registry.get("elevators");
			if (existing instanceof StateFlag){
				ELEVATOR_FLAG = (StateFlag) existing;
			}
		}
	}

	public static boolean haveTrust(Player player) {

		final LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
		final Location loc = localPlayer.getLocation();

		final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		final RegionQuery query = container.createQuery();
		final ApplicableRegionSet set = query.getApplicableRegions(loc);

		AtomicBoolean allowed = new AtomicBoolean(false);
		set.getRegions().forEach(region -> {
			if (region.getFlag(ELEVATOR_FLAG) != null){
				StateFlag.State flag = region.getFlag(ELEVATOR_FLAG);
				if (flag == StateFlag.State.ALLOW){
					allowed.set(true);
				}
			}
		});

		return allowed.get();
	}

	private static void sendErrorMessage(Player player) {
		if (!MessageHandler.spamMessageClaim.contains(player.getUniqueId().toString())) {
			MessageHandler.sendMessage(player, "&c&lHey! &7Sorry, but you can't use that elevator here.");
			MessageHandler.spamMessageClaim.add(player.getUniqueId().toString());

			new BukkitRunnable() {
				@Override
				public void run() {
					MessageHandler.spamMessageClaim.remove(player.getUniqueId().toString());
				}

			}.runTaskLater(ElevatorPlugin.getPlugin(), 20L);
		}
	}

	public static boolean haveTrustTP(Player player) {
		if (ElevatorPlugin.getPlugin().getServer().getPluginManager().getPlugin("WorldGuard") == null) {
			return false;
		}

		final LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
		final Location loc = localPlayer.getLocation();

		final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		final RegionQuery query = container.createQuery();
		final ApplicableRegionSet set = query.getApplicableRegions(loc);

		if (!player.isOp()) {
			if (!set.isMemberOfAll(localPlayer)) {
				return set.testState(localPlayer, Flags.BLOCK_BREAK) && set.testState(localPlayer, Flags.BUILD) && set.testState(localPlayer, Flags.PASSTHROUGH);
			}
		}

		return true;
	}
}
