package nl.giantit.minecraft.GiantPM.core.Tools.Que;

import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 *
 * @author Giant
 */
public class Que {
	
	private static HashMap<Player, QueType> que = new HashMap<Player, QueType>();
	
	public static void addToQue(Player p, QueType qt) {
		addToQue(p, qt, false);
	}
	
	public static void addToQue(Player p, QueType qt, Boolean ow) {
		if(!que.containsKey(p) || ow == true) {
			que.put(p, qt);
		}
	}
	
	public static QueType remFromQue(Player p) {
		if(que.containsKey(p)) {
			QueType t = que.get(p);
			que.remove(p);
			return t;
		}
		
		return QueType.PLAYERNOTINQUE;
	}
	
	public static boolean isInQue(Player p) {
		return que.containsKey(p);
	}
	
	public static QueType getQueType(Player p) {
		if(que.containsKey(p)) {
			return que.get(p);
		}
		
		return QueType.PLAYERNOTINQUE;
	}
}
