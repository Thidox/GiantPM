package nl.giantit.minecraft.giantpm.core.Commands;

import nl.giantit.minecraft.giantpm.GiantPM;
import nl.giantit.minecraft.giantpm.core.Tools.Muter.Muter;
import nl.giantit.minecraft.giantpm.core.Tools.Channel.Channel;
import nl.giantit.minecraft.giantpm.core.Tools.Que.Que;
import nl.giantit.minecraft.giantpm.core.Tools.Que.QueType;
import nl.giantit.minecraft.giantpm.Listeners.PlayerListener;
import nl.giantit.minecraft.giantcore.Misc.Heraut;
import nl.giantit.minecraft.giantcore.Misc.Messages;
import nl.giantit.minecraft.giantcore.Misc.Misc;

import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author Giant
 */
public class inv {
	
	private static Messages mH = GiantPM.getPlugin().getMsgHandler();
	
	public static void inv(Player p, String[] args) {
		if(args.length >= 1) {
			ArrayList<Player> pL = new ArrayList<Player>();
			ArrayList<String> oL = new ArrayList<String>();
			ArrayList<Player> mL = new ArrayList<Player>();
			for(String arg : args) {
				if(Misc.isEitherIgnoreCase(arg, "inv", "i"))
					continue;
				
				Player r = GiantPM.getPlugin().getServer().getPlayer(arg);
				if(r == null) {
					oL.add(arg);
					continue;
				}
				
				pL.add(r);
			}
			
			if(pL.size() > 0) {
				Muter m = Muter.getMuter((OfflinePlayer) p);
				Channel c;
				if(!Channel.inChannel(p)) {
					c = Channel.getChannel(p.getName());
					c.joinChannel(p, true);
				}else
					c = Channel.getChannel(Channel.getPlayerChannelName(p));
				
				String smsg = "";
				String mmsg = "";
				int mi = 0;
				int si = 0;
				
				ArrayList<Player> t = pL;
				for(Player player : t) {
					if(m.isMuted((OfflinePlayer) player)) {
						if(mi > 0)
							mmsg += ", ";
						
						mmsg += player.getDisplayName();
						mi++;
						continue;
					}
					
					c.addInvite(player);
					if(si > 0)
						smsg += ", ";
						
					smsg += player.getDisplayName();
					si++;
					
					Que.addToQue(player, QueType.JOINCHANNEL);
					PlayerListener.cQue.put(player.getName(), c.getName());
					
					HashMap<String, String> data = new HashMap<String, String>();
					data.put("player", p.getDisplayName());
					
					Heraut.say(player, mH.getMsg(Messages.msgType.MAIN, "invite", data));
				}
				
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("mutedPlayer", mmsg);
				data.put("player", smsg);
				
				if(!mmsg.equals("")) {
					Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "invitingMutedPlayers", data));
				}
				
				if(!smsg.equals("")) {
					Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "playersInvited", data));
				}
			}else{
				String msg = "";
				int i = 0;
				for(String name : oL) {
					if(i > 0)
						msg += ", ";
					
					msg += name;
					i++;
				}
				
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("player", msg);
				
				Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "playersNotFound", data));
			}
		}else{
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("type", "invite");

			Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "noPlayerPassed", data));
		}
	}
}
