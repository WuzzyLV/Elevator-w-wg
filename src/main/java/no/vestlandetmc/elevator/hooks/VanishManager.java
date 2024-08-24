package no.vestlandetmc.elevator.hooks;

//import com.Zrips.CMI.Containers.CMIUser;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import de.myzelyam.api.vanish.VanishAPI;
import no.vestlandetmc.elevator.ElevatorPlugin;
import org.bukkit.entity.Player;

public class VanishManager {

	public static boolean isVanished(Player player) {
		final ElevatorPlugin plugin = ElevatorPlugin.getPlugin();

//		if (plugin.getServer().getPluginManager().getPlugin("CMI") != null) {
//			final CMIUser user = CMIUser.getUser(player);
//			if (user.isVanished()) return true;
//		}

		if (plugin.getServer().getPluginManager().getPlugin("Essentials") != null) {
			final Essentials essentials = (Essentials) plugin.getServer().getPluginManager().getPlugin("Essentials");
			final User user = essentials.getUser(player);
			if (user.isVanished()) return true;
		}

		if (plugin.getServer().getPluginManager().getPlugin("PremiumVanish") != null ||
				plugin.getServer().getPluginManager().getPlugin("SuperVanish") != null) {
			if (VanishAPI.isInvisible(player)) return true;
		}

		return false;
	}
}
