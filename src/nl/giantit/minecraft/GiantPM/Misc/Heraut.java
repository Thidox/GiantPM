package nl.giantit.minecraft.GiantPM.Misc;

import nl.giantit.minecraft.GiantPM.GiantPM;
import org.bukkit.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;


/**
 *
 * @author Giant
 */
public class Heraut {
	 
	public static String parse(String input) {
		return ChatColor.translateAlternateColorCodes('&', input);//input.replaceAll("(&([a-fA-F0-9]))", "§$2").replace("\\\\\u00A7", "&");
    }
	
	public static String clean(String input) {
		return input.replaceAll("(&([a-fA-F0-9]))", "");
	}

	public static void say (Player player, String message) {
		player.sendMessage(parse(message));
	}
	
	public static void say (CommandSender sender, String message) {
		sender.sendMessage(message);
	}


	public static void broadcast (String message) {
		for(Player p : GiantPM.getPlugin().getSrvr().getOnlinePlayers()) {
			p.sendMessage(parse(message));
		}
	}
}
