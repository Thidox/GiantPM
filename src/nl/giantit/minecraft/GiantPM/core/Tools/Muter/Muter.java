package nl.giantit.minecraft.GiantPM.core.Tools.Muter;

import nl.giantit.minecraft.GiantPM.GiantPM;
import nl.giantit.minecraft.GiantPM.core.Database.db;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 *
 * @author Giant
 */
public class Muter {
	
	private static HashMap<Player, Muter> instance = new HashMap<Player, Muter>();
	
	private Player p;
	private HashMap<Player, Integer> muted = new HashMap<Player, Integer>();
	private ArrayList<Player> mutedby = new ArrayList<Player>();
	
	private void loadMutes() {
		db DB = db.Obtain();
		
		ArrayList<String> fields = new ArrayList<String>();
		fields.add("id");
		fields.add("owner");
		fields.add("muted");
		
		HashMap<String, String> where = new HashMap<String, String>();
		where.put("owner", p.getName());
		
		ArrayList<HashMap<String, String>> mResSet = DB.select(fields).from("#__muted").where(where).execQuery();
		
		where = new HashMap<String, String>();
		where.put("muted", p.getName());
		ArrayList<HashMap<String, String>> mbResSet = DB.select(fields).from("#__muted").where(where).execQuery();
		
		for(HashMap<String, String> res : mResSet) {
			Player m = GiantPM.getPlugin().getSrvr().getPlayer(res.get("muted"));
			if(m != null) {
				muted.put(m, Integer.parseInt(res.get("id")));
			}else
				GiantPM.getPlugin().getLogger().log(Level.WARNING, "Invalid muted player passed! (" + p.getName() + ":" + res.get("muted") + ")");
		}
		
		for(HashMap<String, String> res : mbResSet) {
			Player m = GiantPM.getPlugin().getSrvr().getPlayer(res.get("owner"));
			if(m != null) {
				mutedby.add(m);
			}else
				GiantPM.getPlugin().getLogger().log(Level.WARNING, "Invalid muted player passed! (" + res.get("owner") + ":" + p.getName() + ")");
		}
	}
	
	private void addMute(Player m) {
		db DB = db.Obtain();
		
		ArrayList<String> fields = new ArrayList<String>();
		fields.add("owner");
		fields.add("muted");
		
		ArrayList<HashMap<Integer, HashMap<String, String>>> values = new ArrayList<HashMap<Integer, HashMap<String, String>>>();
		HashMap<Integer, HashMap<String, String>> tmp = new HashMap<Integer, HashMap<String, String>>();
		
		int i = 0;
		for(String field : fields) {
			HashMap<String, String> temp = new HashMap<String, String>();
			if(field.equalsIgnoreCase("owner")) {
				temp.put("data", p.getName());
				tmp.put(i, temp);
			}else if(field.equalsIgnoreCase("muted")) {
				temp.put("data", m.getName());
				tmp.put(i, temp);
			}
			
			i++;
		}
		values.add(tmp);
		
		DB.insert("#__muted", fields, values).updateQuery();
		
		fields = new ArrayList<String>();
		fields.add("id");
		
		HashMap<String, String> where = new HashMap<String, String>();
		where.put("owner", p.getName());
		where.put("muted", m.getName());
		
		ArrayList<HashMap<String, String>> mResSet = DB.select(fields).from("#__muted").where(where).execQuery();
		muted.put(m, Integer.parseInt(mResSet.get(0).get("id")));
	}
	
	private void unMute(Player m) {
		db DB = db.Obtain();
		
		HashMap<String, String> where = new HashMap<String, String>();
		where.put("owner", p.getName());
		where.put("muted", m.getName());
		
		DB.delete("#__muted").where(where).updateQuery();
		muted.remove(m);
	}
	
	private Muter(Player p) {
		this.p = p;
		this.loadMutes();
	}
	
	public MuterResp mute(Player m) {
		if(m != null) {
			if(!muted.containsKey(m)) {
				addMute(m);
				return new MuterResp(MuterResp.MuterRespType.SUCCESS, "");
			}else
				return new MuterResp(MuterResp.MuterRespType.FAIL, "Passed player is already muted!");
		}else
			return new MuterResp(MuterResp.MuterRespType.FAIL, "Passed player is null!");
	}
	
	public MuterResp unmute(Player m) {
		if(m != null) {
			if(muted.containsKey(m)) {
				unMute(m);
				return new MuterResp(MuterResp.MuterRespType.SUCCESS, "");
			}else
				return new MuterResp(MuterResp.MuterRespType.FAIL, "Passed player is not muted!");
		}else
			return new MuterResp(MuterResp.MuterRespType.FAIL, "Passed player is null!");
	}
	
	public boolean isMuted(Player m) {
		return this.muted.containsKey(m);
	}
	
	public boolean isMutedBy(Player m) {
		return this.mutedby.contains(m);
	}
	
	public static Muter getMuter(Player p) {
		if(instance.containsKey(p)) {
			return instance.get(p);
		}
		
		Muter m = new Muter(p);
		instance.put(p, m);
		return m;
	}
}
