package nl.giantit.minecraft.GiantPM.core.Commands;

import nl.giantit.minecraft.GiantPM.GiantPM;
import nl.giantit.minecraft.GiantPM.core.Tools.Muter.*;
import nl.giantit.minecraft.GiantPM.Misc.Heraut;
import nl.giantit.minecraft.GiantPM.Misc.Messages;
import nl.giantit.minecraft.GiantPM.Misc.Misc;

import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;

/**
 *
 * @author Giant
 */
public class mute {
	
	private static Messages mH = GiantPM.getPlugin().getMsgHandler();
	
	public static void mute(Player p, String[] args) {
		Muter m = Muter.getMuter((OfflinePlayer)p);
		if(args.length == 1) {
			String muted = m.getMutesString();
			if(muted.equals("")) {
				Heraut.say(p, GiantPM.getPlugin().getMsgHandler().getMsg(Messages.msgType.ERROR, "noPlayersMuted"));
			}else{
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("muted", muted);
				
				Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "mutedPlayerList", data));
			}
		}else if(args.length >= 2) {
			for(String arg : args) {
				if(arg.equalsIgnoreCase("mute") || arg.equalsIgnoreCase("m"))
					continue;
				
				OfflinePlayer r = GiantPM.getPlugin().getSrvr().getPlayer(arg);
				
				if(r == null)
					r = Misc.getPlayer(arg);
				
				if(r == null) {
					HashMap<String, String> data = new HashMap<String, String>();
					data.put("player", arg);

					Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "playerUnfindable", data));
					continue;
				}
				
				if(!m.isMuted(r)) {
					m.mute(r);
					HashMap<String, String> data = new HashMap<String, String>();
					data.put("player", r.getName());

					Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "playerMuted", data));
				}else{
					HashMap<String, String> data = new HashMap<String, String>();
					data.put("player", r.getName());

					Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "playerAlreadyMuted", data));
					continue;
				}
			}
		}
	}
	
	public static void unmute(Player p, String[] args) {
		Muter m = Muter.getMuter((OfflinePlayer)p);
		if(args.length >= 2) {
			for(String arg : args) {
				if(arg.equalsIgnoreCase("unmute") || arg.equalsIgnoreCase("um"))
					continue;
				
				OfflinePlayer r = GiantPM.getPlugin().getSrvr().getPlayer(arg);
				
				if(r == null)
					r = Misc.getPlayer(arg);
				
				if(r == null) {
					HashMap<String, String> data = new HashMap<String, String>();
					data.put("player", arg);

					Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "playerUnfindable", data));
					continue;
				}
				
				if(m.isMuted(r)) {
					m.unmute(r);
					HashMap<String, String> data = new HashMap<String, String>();
					data.put("player", r.getName());

					Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "playerUnMuted", data));
				}else{
					HashMap<String, String> data = new HashMap<String, String>();
					data.put("player", r.getName());

					Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "playerNotMuted", data));
					continue;
				}
			}
		}
	}
}
