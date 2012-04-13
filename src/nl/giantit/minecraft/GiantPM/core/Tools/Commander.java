package nl.giantit.minecraft.GiantPM.core.Tools;

import nl.giantit.minecraft.GiantPM.Misc.Misc;
import nl.giantit.minecraft.GiantPM.core.Commands.*;

import org.bukkit.entity.Player;

/**
 *
 * @author Giant
 */
public class Commander {
	
	public static boolean doCommand(Player p, String cmd, String arg) {
		String[] args = {arg};
		if(arg.contains(" ")) {
		  args = arg.split(" ");
		}
		
		if(cmd.equalsIgnoreCase("mute")) {
			mute.mute(p, args);
			return true;
		}else if(Misc.isEitherIgnoreCase(cmd, "um", "unmute")) {
			mute.unmute(p, args);
			return true;
		}/*else if(cmd.equalsIgnoreCase("join")) {
			
		}else if(cmd.equalsIgnoreCase("part")) {
			
		}else if(cmd.equalsIgnoreCase("mail")) {
			
		}*/
		
		return false;
	}
	
}
