package nl.giantit.minecraft.giantpm.core.Commands;

import nl.giantit.minecraft.giantpm.GiantPM;
import nl.giantit.minecraft.giantcore.Misc.Heraut;
import nl.giantit.minecraft.giantcore.Misc.Messages;
import nl.giantit.minecraft.giantpm.core.Config;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import nl.giantit.minecraft.giantcore.perms.Permission;



/**
 *
 * @author Giant
 */
public class help {
	
	private static ArrayList<String[]> entries = new ArrayList<String[]>();
	private final static Config conf = Config.Obtain();
        private final static Permission perm = GiantPM.getPlugin().getPermHandler().getEngine();
        
	private static void init() {
		entries = new ArrayList<String[]>();
		entries.add(new String[] {"/pm", "Show GiantPM help page 1", "null"});
		entries.add(new String[] {"/pm help|h|? (page)", "Show GiantPM help page x", "null"});
		entries.add(new String[] {"[player](, [player]): [message]", "Send a private message to given player", "null"});
		entries.add(new String[] {"/pm mute|m", "Show your muted players list", "null"});
		entries.add(new String[] {"mute: ", "Join the conversation the given player is in", "null"});
		entries.add(new String[] {"/pm mute|m [player]( [player])", "Mute given player", "null"});
		entries.add(new String[] {"mute: [player]( [player])", "Join the conversation the given player is in", "null"});
		entries.add(new String[] {"/pm unmute|um [player]( [player])", "Unmute given player", "null"});
		entries.add(new String[] {"unmute|um: [player]( [player])", "Unmute given player", "null"});
		entries.add(new String[] {"/pm join|j [player]", "Join the conversation the given player is in", "null"});
		entries.add(new String[] {"join: [player]", "Join the conversation the given player is in", "null"});
		entries.add(new String[] {"/pm part|p", "Leave the conversation that you are currently in", "null"});
		entries.add(new String[] {"part: ", "Leave the conversation that you are currently in", "null"});
		entries.add(new String[] {"/pm inv|i [player]( [player])", "Invite given player to conversation", "null"});
		entries.add(new String[] {"inv: [player]( [player])", "Invite given player to conversation", "null"});
		entries.add(new String[] {"/pm state|s [state]", "Set the privacy settings of the conversation", "null"});
		entries.add(new String[] {"state: [state]", "Set the privacy settings of the conversation", "null"});
	}
	
	public static void help(Player player, String[] args) {
		if(entries.isEmpty())
			init();
		
		ArrayList<String[]> uEntries = new ArrayList<String[]>();
		for(int i = 0; i < entries.size(); i++) {
			String[] data = entries.get(i);

			String permission = data[2];

			if(permission.equalsIgnoreCase("null") || perm.has(player, permission)) {
				uEntries.add(data);				
			}
		}
		
		String name = GiantPM.getPlugin().getPubName();
		int perPage = conf.getInt("GiantPM.global.perPage");
		int curPag = 0;
		
		int page;
		if(args.length >= 2) {
			try{
				curPag = Integer.parseInt(args[1]);
			}catch(NumberFormatException e) {
				curPag = 1;
			}
		}else
			curPag = 1;
		
		curPag = (curPag > 0) ? curPag : 1;
		
		int pages = ((int)Math.ceil((double)uEntries.size() / (double)perPage) < 1) ? 1 : (int)Math.ceil((double)uEntries.size() / (double)perPage);
		int start = (curPag * perPage) - perPage;
		
		if(uEntries.size() <= 0) {
			Heraut.say(player, "&e[&3" + name + "&e]&c Sorry no help entries yet :(");
		}else if(curPag > pages) {
			Heraut.say(player, "&e[&3" + name + "&e]&c My help list only has &e" + pages + " &cpages!!");
		}else{
			Heraut.say(player, "&e[&3" + name + "&e]&f Help. Page: &e" + curPag + "&f/&e" + pages);

			for(int i = start; i < (((start + perPage) > uEntries.size()) ? uEntries.size() : (start + perPage)); i++) {
				String[] data = uEntries.get(i);

				String helpEntry = data[0];
				String description = data[1];
				
				Messages msg = GiantPM.getPlugin().getMsgHandler();
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("command", helpEntry);
				params.put("description", description);
				
				Heraut.say(player, msg.getMsg(Messages.msgType.MAIN, "helpCommand", params));
			}
		}
	}
}
