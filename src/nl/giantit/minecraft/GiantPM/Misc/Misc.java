package nl.giantit.minecraft.GiantPM.Misc;

import nl.giantit.minecraft.GiantPM.GiantPM;

import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Giant
 */
public class Misc {
	
	private static HashMap<String, OfflinePlayer> players = new HashMap<String, OfflinePlayer>();
	
	private static OfflinePlayer getOfflinePlayer(final String name) {
		
		OfflinePlayer found = null;
		int lastLength = Integer.MAX_VALUE;
		for(OfflinePlayer p : GiantPM.getPlugin().getSrvr().getOfflinePlayers()) {
			if(p.getFirstPlayed() <= 0)
				continue;
			
			if (p.getName().toLowerCase().startsWith(name.toLowerCase())) {
				int length = p.getName().length() - name.length();
				if(length < lastLength) {
					found = p;
					lastLength = length;
				}
				
				if(length == 0)
					break;
			}
		}
		
		if(found != null)
			players.put(name, found);
		
		return found;
	}
	
	public static boolean isEither(String target, String is, String either) {
		if(target.equals(is) || target.equals(either))
			return true;
		return false;
	}
	
	public static boolean isEitherIgnoreCase(String target, String is, String either) {
		if(target.equalsIgnoreCase(is) || target.equalsIgnoreCase(either))
			return true;
		return false;
	}
	
	public static boolean isAny(String target, String ...test) {
		for(String t : test) {
			if(target.equals(t))
				return true;
		}
		return false;
	}
	
	public static boolean isAnyIgnoreCase(String target, String ...test) {
		for(String t : test) {
			if(target.equalsIgnoreCase(t))
				return true;
		}
		return false;
	}
	
	public static Boolean contains(List<String> haystack, String needle) {
		for(String hay : haystack) {
			hay = hay.replace("[", "");
			hay = hay.replace("]", "");
			if(hay.equals(needle)) {
				return true;
			}
		}
		return false;
	}
	
	public static Boolean containsIgnoreCase(List<String> haystack, String needle) {
		for(String hay : haystack) {
			hay = hay.replace("[", "");
			hay = hay.replace("]", "");
			if(hay.equalsIgnoreCase(needle)) {
				return true;
			}
		}
		return false;
	}
	
	public static OfflinePlayer getPlayer(String name) {
		if(players.containsKey(name))
			return players.get(name);
		
		return getOfflinePlayer(name);
	}
}
