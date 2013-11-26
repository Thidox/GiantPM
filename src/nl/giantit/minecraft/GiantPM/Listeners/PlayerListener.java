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
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.OfflinePlayer;

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
	private HashMap<Player, HashMap<OfflinePlayer, String>> pnQue = new HashMap<Player, HashMap<OfflinePlayer, String>>();
	public static HashMap<String, String> cQue = new HashMap<String, String>();
	
	public PlayerListener(GiantPM plugin) {
		this.plugin = plugin;
		this.mH = plugin.getMsgHandler();
	}
	
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		Player p = event.getPlayer();
		String[] tmp = {};
		String msg = "";
		Boolean skipPm = false;
		if(message.contains(":")) {
			String m = message.replaceFirst(":", "-:-_-:-");
			tmp = m.split("-:-_-:-");

			int i = 0;
			for(String part : tmp) {
				if(i == 0) {
					if(tmp[0].isEmpty())
						skipPm = true;
					
					i++;
					continue;
				}

				msg += part;
			}
		}else
			msg = message;
		
		if(msg.startsWith(" "))
			msg = msg.replaceFirst(" ", "");
		
		if(Que.isInQue(p)) {
			switch(Que.remFromQue(p)) {
				case PARTIALNAME:
					if(Misc.isAnyIgnoreCase(message, "yes", "yea", "yep", "da", "ja", "oui", "ye", "y", "si")) {
						HashMap<OfflinePlayer, String> pn = pnQue.get(p);
						for(Map.Entry<OfflinePlayer, String> entry : pn.entrySet()) {
							if(entry.getKey().isOnline()) {
								HashMap<String, String> data = new HashMap<String, String>();
								data.put("player", p.getDisplayName());
								data.put("receiver", entry.getKey().getName());
								data.put("msg", entry.getValue());
								
								if(!Muter.getMuter((OfflinePlayer)p).isMutedBy(entry.getKey())) {
									Heraut.say(entry.getKey().getPlayer(), mH.getMsg(Messages.msgType.MAIN, "whispers", data));
								}
								
								Heraut.say(p, plugin.getMsgHandler().getMsg(Messages.msgType.MAIN, "whisperTo", data));
								Heraut.say(p, plugin.getMsgHandler().getMsg(Messages.msgType.MAIN, "whisperMsg", data));
								Que.addToQue(entry.getKey().getPlayer(), QueType.REPLY);
								Replier.addReply(entry.getKey().getPlayer(), p);
							}else{
								//Mailer.send(p, entry.getKey(), entry.getValue());
								Heraut.say(p, "We are sorry but offline messages are currently not supported!");
							}
						}
						event.setCancelled(true);
						return;
					}
					break;
				case JOINCHANNEL:
					if(Misc.isAnyIgnoreCase(message, "yes", "yea", "yep", "da", "ja", "oui", "ye", "y", "si")) {
						Channel c = Channel.getChannel(cQue.get(p.getName()));
						ChannelResponse cR = c.joinChannel(p);
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
		
		String cmd;
		if(tmp.length > 0)
			cmd = tmp[0];
		else
			cmd = tmp.toString();
		
		if(Commander.doCommand(p, cmd, msg)) {
			event.setCancelled(true);
			return;
		}
		
		if(!skipPm) {
			if(tmp.length > 0) {
				Muter m = Muter.getMuter((OfflinePlayer)p);
				String[] users = new String[]{};
				if(tmp[0].contains(", ")) {
					users = tmp[0].split(", ");
				}else{
					users = new String[]{tmp[0]};
				}

				Boolean broken = false, partial = false;
				ArrayList<Player> rs = new ArrayList<Player>();

				for(String user : users) {
					if(user.contains(" ")) {
						broken = true;
						break;
					}

					user = Heraut.clean(user);

					OfflinePlayer r = plugin.getSrvr().getPlayer(user);

					if(r == null)
						r = Misc.getPlayer(user);

					if(r == null)
						continue;

					if(!r.isOnline() && r.getFirstPlayed() <= 0) {
						Heraut.say(p, "We are sorry but for offline messages you need a full name!");
						event.setCancelled(true);
						return;
					}

					if(!m.isMuted(r)) {
						if(r.getName().equalsIgnoreCase(user)) {
							if(r.isOnline())
								rs.add(r.getPlayer());
							else{
								Heraut.say(p, "We are sorry but offline messages are currently not supported!");
								event.setCancelled(true);
							}
						}else{
							HashMap<OfflinePlayer, String> t;
							if(!pnQue.containsKey(p)) {
								t = new HashMap<OfflinePlayer, String>();
							}else{
								t = pnQue.get(p);
							}

							t.put(r, msg);
							pnQue.put(p, t);
							Que.addToQue(p, QueType.PARTIALNAME);
							HashMap<String, String> data = new HashMap<String, String>();
							data.put("player", r.getName());
							data.put("partial", user);

							Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "partialNamePassed", data));
							event.setCancelled(true);
							partial = true;
						}
					}else{
						HashMap<String, String> data = new HashMap<String, String>();
						data.put("player", r.getName());

						Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "playerMuted", data));
						event.setCancelled(true);
					}
				}

				if(false == broken) {
					if(rs.size() > 0) {
						String receivers = "";
						String cs = mH.getMsg(Messages.msgType.MAIN, "whisperToCommaSeperator");
						for(Player r : rs) {
							if(!m.isMutedBy((OfflinePlayer)r)) {
								HashMap<String, String> data = new HashMap<String, String>();
								data.put("player", p.getDisplayName());
								data.put("receiver", r.getName());
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
						event.setCancelled(true);
					}

					if(partial == true || rs.size() > 0) {
						return;
					}
				}
			}
		}
		
		if(!event.isCancelled() && Channel.inChannel(p)) {
			Channel c = Channel.getChannel(Channel.getPlayerChannelName(p));
			c.sendMsg(p, message);
			event.setCancelled(true);
		}
	}
}