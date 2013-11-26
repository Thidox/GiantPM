package nl.giantit.minecraft.giantpm.core.Commands;

import nl.giantit.minecraft.giantpm.GiantPM;
import nl.giantit.minecraft.giantpm.core.Tools.Muter.Muter;
import nl.giantit.minecraft.giantpm.core.Tools.Channel.*;
import nl.giantit.minecraft.giantpm.Misc.Heraut;
import nl.giantit.minecraft.giantpm.Misc.Messages;
import nl.giantit.minecraft.giantpm.Misc.Misc;

import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
/**
 *
 * @author Giant
 */
public class join {
	
	private static Messages mH = GiantPM.getPlugin().getMsgHandler();
	
	public static void join(Player p, String[] args) {
		if(args.length >= 1) {
			String name = "";
			for(String arg : args) {
				if(Misc.isEitherIgnoreCase(arg, "join", "j"))
					continue;
				
				name = arg;
				break;
			}
			
			Player r = GiantPM.getPlugin().getSrvr().getPlayer(name);
			if(r == null) {
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("player", name);

				Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "playerUnfindable", data));
				return;
			}
			
			Muter m = Muter.getMuter((OfflinePlayer) p);
			if(!m.isMuted((OfflinePlayer) r)) {
				if(Channel.inChannel(r)) {
					Channel c = Channel.getChannel(Channel.getPlayerChannelName(r));
					ChannelResponse cR = c.joinChannel(p);
					switch(cR) {
						case CHANNELISPRIVATE:
							Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "channelIsPrivate"));
							return;
						case CHANNELJOINED:
							Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "channelJoined"));
							return;
					}
				}else{
					HashMap<String, String> data = new HashMap<String, String>();
					data.put("player", name);

					Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "playerNotInChannel", data));
					return;
				}
			}else{
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("player", name);

				Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "joiningMutedPlayer", data));
				return;
			}
		}else{
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("type", "join");

			Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "noPlayerPassed"));
		}
	}
	
	public static void part(Player p, String[] args) {
		if(Channel.inChannel(p)) {
			Channel c = Channel.getChannel(Channel.getPlayerChannelName(p));
			c.leaveChannel(p);
		}else{
			Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "notInChannel"));
		}
	}
}
