package nl.giantit.minecraft.GiantPM.core.Commands;

import nl.giantit.minecraft.GiantPM.GiantPM;
import nl.giantit.minecraft.GiantPM.core.Tools.Channel.Channel;
import nl.giantit.minecraft.GiantPM.Misc.Heraut;
import nl.giantit.minecraft.GiantPM.Misc.Messages;
import nl.giantit.minecraft.GiantPM.Misc.Misc;

import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 *
 * @author Giant
 */
public class channel {
	
	private static Messages mH = GiantPM.getPlugin().getMsgHandler();
	
	public static void setStatus(Player p, String[] args) {
		if(Channel.inChannel(p)) {
			Channel c = Channel.getChannel(Channel.getPlayerChannelName(p));
			if(c.isOwner(p)) {
				if(args.length >= 1) {
					String type = "invite";

					for(String arg : args) {
						if(Misc.isEitherIgnoreCase(arg, "state", "s"))
							continue;

						type = arg;
						break;
					}

					if(Misc.isAnyIgnoreCase(type, "public", "pub", "pu", "0")) {
						c.setStatus("0");
					}else if(Misc.isAnyIgnoreCase(type, "invite", "inv", "i", "1")) {
						c.setStatus("1");
					}else if(Misc.isAnyIgnoreCase(type, "private", "priv", "pr", "2")) {
						c.setStatus("2");
					}else{
						HashMap<String, String> data = new HashMap<String, String>();
						data.put("state", type);
					
						Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "channelStatusInvalid", data));
					}
				}
			}else{
				Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "notChannelOwner"));
			}
		}else{
			Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "notInChannel"));
		}
	}
	
	public static void getMembers(Player p, String[] args) {
		if(Channel.inChannel(p)) {
			Channel c = Channel.getChannel(Channel.getPlayerChannelName(p));
			HashMap<String, String> data = new HashMap<String, String>();
				data.put("member", c.getMembersString());
				
				Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "channelMemberList", data));
		}else{
			Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "notInChannel"));
		}
	}
}
