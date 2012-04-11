package nl.giantit.minecraft.GiantPM.Listeners;

import nl.giantit.minecraft.GiantPM.GiantPM;
import nl.giantit.minecraft.GiantPM.core.Tools.*;
import nl.giantit.minecraft.GiantPM.core.Tools.Channel.*;
import nl.giantit.minecraft.GiantPM.core.Tools.Muter.*;
import nl.giantit.minecraft.GiantPM.core.Tools.Que.*;
import nl.giantit.minecraft.GiantPM.Misc.Heraut;
import nl.giantit.minecraft.GiantPM.Misc.Messages;
import nl.giantit.minecraft.GiantPM.Misc.Misc;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Giant
 */
public class PlayerListener implements Listener {
	
	private GiantPM plugin;
	private Messages mH;
	
	private HashMap<String, Player> replier = new HashMap<String, Player>();
	private HashMap<Player, String> cQue = new HashMap<Player, String>();
	private HashMap<Player, HashMap<Player, String>> pnQue = new HashMap<Player, HashMap<Player, String>>();
	
	public PlayerListener(GiantPM plugin) {
		this.plugin = plugin;
		this.mH = plugin.getMsgHandler();
	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		String message = event.getMessage();
		Player p = event.getPlayer();
		
		String[] tmp = {};
		String msg = "";
		if(message.contains(": ")) {
			tmp = message.split(": ");

			int i = 0;
			for(String part : tmp) {
				i++;
				if(i == 1)
					continue;

				msg += part;
			}
		}
		
		if(Que.isInQue(p)) {
			switch(Que.remFromQue(p)) {
				case PARTIALNAME:
					if(Misc.isAnyIgnoreCase(message, "yes", "yea", "yep", "da", "ja", "oui", "ye", "y", "si")) {
						HashMap<Player, String> pn = pnQue.get(p);
						for(Map.Entry<Player, String> entry : pn.entrySet()) {
							if(entry.getKey().isOnline()) {
								HashMap<String, String> data = new HashMap<String, String>();
								data.put("player", p.getDisplayName());
								data.put("receiver", entry.getKey().getDisplayName());
								data.put("msg", entry.getValue());
								
								if(!Muter.getMuter(p).isMutedBy(entry.getKey())) {
									Heraut.say(entry.getKey(), mH.getMsg(Messages.msgType.MAIN, "whispers", data));
								}
								
								Heraut.say(p, plugin.getMsgHandler().getMsg(Messages.msgType.MAIN, "whisperTo", data));
								Heraut.say(p, plugin.getMsgHandler().getMsg(Messages.msgType.MAIN, "whisperMsg", data));
								Que.addToQue(entry.getKey(), QueType.REPLY);
								Replier.addReply(entry.getKey(), p);
							}else{
								//Mailer.send(p, entry.getKey(), entry.getValue());
								Heraut.say(p, "We are sorry but ofline messages are currently not supported!");
							}
						}
						event.setCancelled(true);
						return;
					}
					break;
				case JOINCHANNEL:
					if(Misc.isAnyIgnoreCase(message, "yes", "yea", "yep", "da", "ja", "oui", "ye", "y", "si")) {
						Channel c = Channel.getChannel(cQue.get(p));
						ChannelResponse cR = c.joinChannel(p, true);
						switch(cR) {
							case CHANNELISPRIVATE:
								Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "channelIsPrivate"));
								event.setCancelled(true);
								return;
							case CHANNELJOINED:
								Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "channelJoined"));
								event.setCancelled(true);
								return;
						}
					}
					break;
				case REPLY:
					if(tmp.length > 0 && Misc.isAnyIgnoreCase(tmp[0], "r", "re", "rep", "repl", "reply")) {
						Replier.doReply(p, msg);
						event.setCancelled(true);
						return;
					}
					break;
				default:
					break;
			}
		}
		
		if(tmp.length > 0) {
			Muter m = Muter.getMuter(p);
			String[] users = new String[]{};
			if(tmp[0].contains(", ")) {
				users = tmp[0].split(", ");
			}else{
				users = new String[]{tmp[0]};
			}

			Boolean broken = false;
			ArrayList<Player> rs = new ArrayList<Player>();

			for(String user : users) {
				if(user.contains(" ")) {
					broken = true;
					break;
				}
				
				user = Heraut.clean(user);

				Player r = plugin.getSrvr().getPlayer(user);
				if(r == null)
					continue;
				
				
				if(!m.isMuted(r)) {
					if(r.getName().equalsIgnoreCase(user)) {
						rs.add(r);
					}else{
						HashMap<Player, String> t;
						if(!pnQue.containsKey(p)) {
							t = new HashMap<Player, String>();
						}else{
							t = pnQue.get(p);
						}
						
						t.put(r, msg);
						pnQue.put(p, t);
						Que.addToQue(p, QueType.PARTIALNAME);
						HashMap<String, String> data = new HashMap<String, String>();
						data.put("player", r.getDisplayName());
						data.put("partial", user);
						
						Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "partialNamePassed", data));
					}
				}else{
					HashMap<String, String> data = new HashMap<String, String>();
					data.put("player", r.getDisplayName());
					
					Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "playerMuted", data));
				}
			}
			
			if(false == broken) {
				if(rs.size() > 0) {
					String receivers = "";
					String cs = mH.getMsg(Messages.msgType.MAIN, "whisperToCommaSeperator");
					for(Player r : rs) {
						if(!m.isMutedBy(r)) {
							HashMap<String, String> data = new HashMap<String, String>();
							data.put("player", p.getDisplayName());
							data.put("receiver", r.getDisplayName());
							data.put("msg", msg);

							Que.addToQue(r, QueType.REPLY);
							Replier.addReply(r, p);
							Heraut.say(r, mH.getMsg(Messages.msgType.MAIN, "whispers", data));
						}

						if(receivers.length() > 0) 
							receivers += cs;
						receivers += r.getDisplayName();
					}

					HashMap<String, String> data = new HashMap<String, String>();
					data.put("receiver", receivers);
					data.put("msg", msg);

					Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "whisperTo", data));
					Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "whisperMsg", data));
				}
				
				event.setCancelled(true);
			}
		}
	}
}