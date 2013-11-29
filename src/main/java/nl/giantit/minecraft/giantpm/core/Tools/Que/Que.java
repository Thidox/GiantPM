package nl.giantit.minecraft.giantpm.core.Tools.Que;

import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 *
 * @author Giant
 */
public class Que {
	
	private static HashMap<String, QueType> que = new HashMap<String, QueType>();
	
	public static void addToQue(Player p, QueType qt) {
		addToQue(p, qt, false);
	}
	
	public static void addToQue(Player p, QueType qt, Boolean ow) {
		if(!que.containsKey(p.getName()) || ow == true) {
			que.put(p.getName(), qt);
		}
	}
	
	public static QueType remFromQue(Player p) {
		if(que.containsKey(p.getName())) {
			QueType t = que.remove(p.getName());
			return t;
		}
		
		return QueType.PLAYERNOTINQUE;
	}
	
	public static boolean isInQue(Player p) {
		return que.containsKey(p.getName());
	}
	
	public static QueType getQueType(Player p) {
		if(que.containsKey(p.getName())) {
			return que.get(p.getName());
		}
		
		return QueType.PLAYERNOTINQUE;
	}
}
