package nl.giantit.minecraft.GiantPM.core.Tools;

import nl.giantit.minecraft.GiantPM.GiantPM;
import nl.giantit.minecraft.GiantPM.Misc.Heraut;
import nl.giantit.minecraft.GiantPM.Misc.Messages;

import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 *
 * @author Giant
 */
public class Replier {
	
	private static HashMap<Player, Player> replier = new HashMap<Player, Player>();
	private static Messages mH = GiantPM.getPlugin().getMsgHandler();
	
	public static void doReply(Player p, String msg) {
		if(!replier.containsKey(p)) {
			Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "noReplyTarget"));
		}else{
			Player r = replier.get(p);
			
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("player", p.getDisplayName());
			data.put("receiver", r.getDisplayName());
			data.put("msg", msg);
			
			Heraut.say(r, mH.getMsg(Messages.msgType.MAIN, "whispers", data));
			Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "repliedTo", data));
			replier.put(r, p);
			replier.remove(p);
		}
	}
	
	public static void addReply(Player p, Player r) {
		replier.put(r, p);
	}
	
	public static boolean inReply(Player p) {
		return replier.containsKey(p);
	}
}
