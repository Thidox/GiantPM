package nl.giantit.bukkit.GiantPM.Misc;

import nl.giantit.bukkit.GiantPM.GiantPM;
import org.bukkit.entity.Player;

/**
 *
 * @author Giant
 */
public class Messager {

	private static Player player = null;

	public static void savePlayer(Player sender) {
		Messager.player = sender;
	}

	public static String parse(String input) {
		return input.replaceAll("(&([a-fA-F0-9]))", "§$2").replace("\\\\\u00A7", "&");
	}
	
	public static String clean(String input) {
		return input.replaceAll("(&([a-fA-F0-9]))", "");
	}

	public static void say (String message) {
		player.sendMessage(parse(message));
	}

	public static void say (Player player, String message) {
		player.sendMessage(parse(message));
	}

	public static void broadcast (String message) {
		for(Player p : GiantPM.Server.getOnlinePlayers()) {
			p.sendMessage(parse(message));
		}
	}
}
