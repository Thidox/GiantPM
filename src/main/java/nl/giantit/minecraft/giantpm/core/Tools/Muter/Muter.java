package nl.giantit.minecraft.giantpm.core.Tools.Muter;

import nl.giantit.minecraft.giantpm.GiantPM;
import nl.giantit.minecraft.giantcore.Misc.Messages;
import nl.giantit.minecraft.giantcore.database.Driver;

import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.Map;
import nl.giantit.minecraft.giantcore.database.QueryResult;
import nl.giantit.minecraft.giantcore.database.QueryResult.QueryRow;
import nl.giantit.minecraft.giantcore.database.query.Group;
import nl.giantit.minecraft.giantcore.database.query.InsertQuery;
import nl.giantit.minecraft.giantcore.database.query.SelectQuery;
import nl.giantit.minecraft.giantcore.database.query.UpdateQuery;

/**
 *
 * @author Giant
 */
public class Muter {
	
	private static HashMap<OfflinePlayer, Muter> instance = new HashMap<OfflinePlayer, Muter>();
	
	private OfflinePlayer p;
	private ArrayList<String> muted = new ArrayList<String>();
	private boolean init = false;
	
	private void loadMutes() {
                Driver DB = GiantPM.getPlugin().getDB().getEngine();
		
                SelectQuery mResQry = DB.select("muted");
                mResQry.from("#__muted");
                mResQry.where("owner", p.getName(), Group.ValueType.EQUALS);
                
                QueryResult mResSet = mResQry.exec();
		
		if(mResSet.size() > 0) {
			QueryRow res = mResSet.getRow(0);
			if(res.getString("muted").contains(";")) {
				for(String u : res.getString("muted").split(";")) {
					OfflinePlayer m = GiantPM.getPlugin().getServer().getOfflinePlayer(u);
					if(m != null) {
						muted.add(m.getName());
					}else
						GiantPM.getPlugin().getLogger().log(Level.WARNING, "Invalid muted player passed! ({0}:{1})", new Object[]{p.getName(), u});
				}
			}
		}else{
			init = true;
		}
	}
	
	
	private void saveMutes() {
		Driver DB = GiantPM.getPlugin().getDB().getEngine();
		
		String m = "";
		for(String u : muted) {
			m += u + ";";
		}
		
		if(!init) {
                        UpdateQuery sMuteUpdateQry = DB.update("#__muted");
                        sMuteUpdateQry.set("muted", m);
                        sMuteUpdateQry.where("owner", p.getName());
                        sMuteUpdateQry.exec(); 
		}else{
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("owner");
			fields.add("muted");
                        
                        InsertQuery sMuteInsertQry = DB.insert("#__muted");
                        sMuteInsertQry.addFields(fields);
                        sMuteInsertQry.addRow();
                        sMuteInsertQry.assignValue("owner", p.getName());
                        sMuteInsertQry.assignValue("muted", m);
                        sMuteInsertQry.exec();
		}
	}
	
	private Muter(OfflinePlayer p) {
		this.p = p;
		this.loadMutes();
	}
	
	public String getMutesString() {
		String s = "";
		
		int i = 0;
		for(String r : muted) {
			if(i > 0) {
				s += GiantPM.getPlugin().getMsgHandler().getMsg(Messages.msgType.MAIN, "muteListCommaSeperator");
			}
			i++;
			
			s += r;
		}
		
		return s;
	}
	
	public MuterResp mute(OfflinePlayer m) {
		if(m != null) {
			if(!muted.contains(m.getName())) {
				muted.add(m.getName());
				return new MuterResp(MuterResp.MuterRespType.SUCCESS, "");
			}else
				return new MuterResp(MuterResp.MuterRespType.FAIL, "Passed player is already muted!");
		}else
			return new MuterResp(MuterResp.MuterRespType.FAIL, "Passed player is null!");
	}
	
	public MuterResp unmute(OfflinePlayer m) {
		if(m != null) {
			if(muted.contains(m.getName())) {
				muted.remove(m.getName());
				return new MuterResp(MuterResp.MuterRespType.SUCCESS, "");
			}else
				return new MuterResp(MuterResp.MuterRespType.FAIL, "Passed player is not muted!");
		}else
			return new MuterResp(MuterResp.MuterRespType.FAIL, "Passed player is null!");
	}
	
	public boolean isMuted(OfflinePlayer m) {
		return muted.contains(m.getName());
	}
	
	public boolean isMutedBy(OfflinePlayer m) {
		return Muter.getMuter(m).isMuted(p);
	}
	
	public static Muter getMuter(OfflinePlayer p) {
		if(instance.containsKey(p)) {
			return instance.get(p);
		}
		
		Muter m = new Muter(p);
		instance.put(p, m);
		return m;
	}
	
	public static void save() {
		for(Map.Entry<OfflinePlayer, Muter> m : instance.entrySet()) {
			m.getValue().saveMutes();
		}
	}
}
