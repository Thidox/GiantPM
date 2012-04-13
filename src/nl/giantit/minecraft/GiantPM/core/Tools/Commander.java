package nl.giantit.minecraft.GiantPM.core.Tools;

import nl.giantit.minecraft.GiantPM.Misc.Misc;
import nl.giantit.minecraft.GiantPM.core.Commands.*;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Giant
 */
public class Commander {
	
	public static boolean doCommand(Player p, String cmd, String arg) {
		List<String> args = new ArrayList<String>();
		
		if(cmd.equalsIgnoreCase("mute")) {
			args.add(cmd);
			if(arg.contains(" ")) {
				args.addAll(Arrays.asList(arg.split(" ")));
			}else if(!arg.equals("")) {
				args.add(arg);
			}

			mute.mute(p, args.toArray(new String[args.size()]));
			return true;
		}else if(Misc.isEitherIgnoreCase(cmd, "um", "unmute")) {
			args.add(cmd);
			if(arg.contains(" ")) {
				args.addAll(Arrays.asList(arg.split(" ")));
			}else if(!arg.equals("")) {
				args.add(arg);
			}

			mute.unmute(p, args.toArray(new String[args.size()]));
			return true;
		}/*else if(cmd.equalsIgnoreCase("join")) {
			
		}else if(cmd.equalsIgnoreCase("part")) {
			
		}else if(cmd.equalsIgnoreCase("mail")) {
			
		}*/
		
		return false;
	}
	
}
