package nl.giantit.minecraft.GiantPM.core.Tools.Channel;

import nl.giantit.minecraft.GiantPM.GiantPM;
import nl.giantit.minecraft.GiantPM.core.perm;
import nl.giantit.minecraft.GiantPM.Misc.Heraut;
import nl.giantit.minecraft.GiantPM.Misc.Messages;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Giant
 */
public class Channel {
	
	private static HashMap<Player, String> inChan = new HashMap<Player, String>();
	private static HashMap<String, Channel> channels = new HashMap<String, Channel>();
	private Messages mH = GiantPM.getPlugin().getMsgHandler();
	
	private ArrayList<Player> members = new ArrayList<Player>();
	private int status = 1; //0 == open; 1 == private - inv allowed; 2 == private;
	private String name;
	private Player owner;
	private perm perms = perm.Obtain();
	
	
	private Channel(String name) {
		this.name = name;
		
	}
	
	public void setStatus(String state) {
		try {
			int tmp = Integer.parseInt(state);
			status = (tmp <= 0) ? 0 : ((tmp >= 2) ? 2 : tmp);
			
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("state", (tmp <= 0) ? "Public" : ((tmp >= 2) ? "Private" : "Private invitations allowed"));
			
			Heraut.say(owner, mH.getMsg(Messages.msgType.MAIN, "channelStateChanged", data));
		}catch(NumberFormatException e) {
			Heraut.say(owner, mH.getMsg(Messages.msgType.ERROR, "channelStateInvalid"));
		}
	}
	
	public void sendMsg(String msg) {
		for(Player m : members) {
			if(m.isOnline()) {
				Heraut.say(m, msg);
			}else{
				leaveChannel(m);
				
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("player", m.getDisplayName());
				
				sendMsg(mH.getMsg(Messages.msgType.MAIN, "removedOfflinePlayer", data));
			}
		}
	}
	
	public void sendMsg(Player p, String msg) {
		for(Player m : members) {
			if(m.isOnline()) {
				Heraut.say(m, p.getDisplayName() + ": " + msg);
			}else{
				leaveChannel(m);
				
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("player", m.getDisplayName());
				
				sendMsg(mH.getMsg(Messages.msgType.MAIN, "removedOfflinePlayer", data));
			}
		}
	}
	
	public ChannelResponse joinChannel(Player p, boolean inv) {
		if(((status == 1 && !inv) || status == 2) && !perms.has(p, "giantpm.admin.fuckprivacy"))
			return ChannelResponse.CHANNELISPRIVATE;
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("player", p.getDisplayName());
		
		sendMsg(mH.getMsg(Messages.msgType.MAIN, "joinedChannel", data));
		inChan.put(p, this.name);
		members.add(p);
		return ChannelResponse.CHANNELJOINED;
	}
	
	public void leaveChannel(Player p) {
		inChan.remove(p);
		members.remove(p);
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("player", p.getDisplayName());
		
		sendMsg(mH.getMsg(Messages.msgType.MAIN, "leftChannel", data));
		Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "channelLeft"));
	}
	
	public static boolean inChannel(Player p) {
		return inChan.containsKey(p);
	}
	
	public static String getPlayerChannelName(Player p) {
		return inChan.get(p);
	}
	
	public static Channel getChannel(String cName) {
		if(channels.containsKey(cName)) {
			return channels.get(cName);
		}
		
		Channel c = new Channel(cName);
		channels.put(cName, c);
		return c;
	}
}