package nl.giantit.minecraft.GiantPM.core.Tools.Channel;

import nl.giantit.minecraft.GiantPM.GiantPM;
import nl.giantit.minecraft.GiantPM.core.perm;
import nl.giantit.minecraft.GiantPM.Misc.Heraut;
import nl.giantit.minecraft.GiantPM.Misc.Messages;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Giant
 */
public class Channel {
	
	private static HashMap<String, String> inChan = new HashMap<String, String>();
	private static HashMap<String, Channel> channels = new HashMap<String, Channel>();
	private Messages mH = GiantPM.getPlugin().getMsgHandler();
	
	private HashMap<String, Player> members = new HashMap<String, Player>();
	private ArrayList<String> invited = new ArrayList<String>();
	private int status = 1; //0 == open; 1 == private - inv allowed; 2 == private;
	private String name;
	private perm perms = perm.Obtain();
	
	
	private Channel(String name) {
		this.name = name;
	}
	
	public boolean isOwner(Player p) {
		return this.name.equalsIgnoreCase(p.getName());
	}
	
	public void setStatus(String state) {
		try {
			int tmp = Integer.parseInt(state);
			status = (tmp <= 0) ? 0 : ((tmp >= 2) ? 2 : tmp);
			
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("state", (tmp <= 0) ? "Public" : ((tmp >= 2) ? "Private" : "Private invitations allowed"));
			
			Heraut.say(members.get(name), mH.getMsg(Messages.msgType.MAIN, "channelStateChanged", data));
		}catch(NumberFormatException e) {
			Heraut.say(members.get(name), mH.getMsg(Messages.msgType.ERROR, "channelStateInvalid"));
		}
	}
	
	public void sendMsg(String msg) {
		HashMap<String, Player> tmp = members; //Dirty lazy fix
		for(Map.Entry<String, Player> t : tmp.entrySet()) {
			Player m = t.getValue();
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
		HashMap<String, Player> tmp = members; //Dirty lazy fix
		for(Map.Entry<String, Player> t : tmp.entrySet()) {
			Player m = t.getValue();
			if(m.isOnline()) {
				Heraut.say(m, p.getDisplayName() + ": " + msg);
			}else{
				leaveChannel(m, true);
				
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("player", m.getDisplayName());
				
				sendMsg(mH.getMsg(Messages.msgType.MAIN, "removedOfflinePlayer", data));
			}
		}
	}
	
	public ChannelResponse joinChannel(Player p) {
		if(((status == 1 && !invited.contains(p.getName())) || status == 2) && !perms.has(p, "giantpm.admin.fuckprivacy"))
			return ChannelResponse.CHANNELISPRIVATE;
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("player", p.getDisplayName());
		
		sendMsg(mH.getMsg(Messages.msgType.MAIN, "joinedChannel", data));
		inChan.put(p.getName(), this.name);
		members.put(p.getName(), p);
		if(invited.contains(p.getName()))
			invited.remove(p.getName());
			
		return ChannelResponse.CHANNELJOINED;
	}
	
	public ChannelResponse joinChannel(Player p, boolean override) {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("player", p.getDisplayName());
		
		sendMsg(mH.getMsg(Messages.msgType.MAIN, "joinedChannel", data));
		inChan.put(p.getName(), this.name);
		members.put(p.getName(), p);
		if(invited.contains(p.getName()))
			invited.remove(p.getName());
			
		return ChannelResponse.CHANNELJOINED;
	}
	
	public void leaveChannel(Player p) {
		inChan.remove(p.getName());
		members.remove(p.getName());
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("player", p.getDisplayName());
		
		sendMsg(mH.getMsg(Messages.msgType.MAIN, "leftChannel", data));
		Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "channelLeft"));
	}
	
	public void leaveChannel(Player p, boolean quiet) {
		inChan.remove(p.getName());
		members.remove(p.getName());
		
		if(!quiet) {
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("player", p.getDisplayName());

			sendMsg(mH.getMsg(Messages.msgType.MAIN, "leftChannel", data));
			Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "channelLeft"));
		}
	}
	
	public void addInvite(Player p) {
		if(!invited.contains(p.getName()))
			invited.add(p.getName());
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getMembersString() {
		String s = "";
		
		int i = 0;
		for(String pName : members.keySet()) {
			if(i > 0) {
				s += mH.getMsg(Messages.msgType.MAIN, "channelListCommaSeperator");
			}
			i++;
			
			s += pName;
		}
		
		return s;
	}
	
	public static boolean inChannel(Player p) {
		return inChan.containsKey(p.getName());
	}
	
	public static String getPlayerChannelName(Player p) {
		return inChan.get(p.getName());
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