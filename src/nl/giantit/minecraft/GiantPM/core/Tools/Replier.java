package nl.giantit.minecraft.GiantPM.core.Tools;

import nl.giantit.minecraft.GiantPM.GiantPM;
import nl.giantit.minecraft.GiantPM.core.Tools.Que.*;
import nl.giantit.minecraft.GiantPM.Misc.Heraut;
import nl.giantit.minecraft.GiantPM.Misc.Messages;

import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 *
 * @author Giant
 */
public class Replier {
	
	private static HashMap<String, Player> players = new HashMap<String, Player>();
	private static HashMap<String, String> replier = new HashMap<String, String>();
	private static Messages mH = GiantPM.getPlugin().getMsgHandler();
	
	public static void doReply(Player p, String msg) {
		if(!replier.containsKey(p.getName())) {
			Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "noReplyTarget"));
		}else{
			Player r = players.get(replier.get(p.getName()));
			
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("player", p.getDisplayName());
			data.put("receiver", r.getDisplayName());
			data.put("msg", msg);
			
			Heraut.say(r, mH.getMsg(Messages.msgType.MAIN, "whispers", data));
			Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "repliedTo", data));
			Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "whisperMsg", data));
			
			Que.addToQue(r, QueType.REPLY);
			replier.put(r.getName(), p.getName());
			
			if(!players.containsKey(r.getName()))
				players.put(r.getName(), r);
			
			if(!players.containsKey(p.getName()))
				players.put(p.getName(), p);
			
			replier.remove(p.getName());
		}
	}
	
	public static void addReply(Player p, Player r) {
		replier.put(p.getName(), r.getName());
		if(!players.containsKey(r.getName()))
			players.put(r.getName(), r);
		
		if(!players.containsKey(p.getName()))
			players.put(p.getName(), p);
	}
	
	public static boolean inReply(Player p) {
		return replier.containsKey(p.getName());
	}
}
