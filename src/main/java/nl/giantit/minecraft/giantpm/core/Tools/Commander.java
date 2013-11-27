package nl.giantit.minecraft.giantpm.core.Tools;

import nl.giantit.minecraft.giantcore.Misc.Misc;
import nl.giantit.minecraft.giantpm.core.Commands.*;

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
		}else if(cmd.equalsIgnoreCase("inv")) {
			if(arg.contains(" ")) {
				args.addAll(Arrays.asList(arg.split(" ")));
			}else if(!arg.equals("")) {
				args.add(arg);
			}

			inv.inv(p, args.toArray(new String[args.size()]));
			return true;
		}else if(cmd.equalsIgnoreCase("join")) {
			if(arg.contains(" ")) {
				args.addAll(Arrays.asList(arg.split(" ")));
			}else if(!arg.equals("")) {
				args.add(arg);
			}

			join.join(p, args.toArray(new String[args.size()]));
			return true;
		}else if(cmd.equalsIgnoreCase("part")) {
			if(arg.contains(" ")) {
				args.addAll(Arrays.asList(arg.split(" ")));
			}else if(!arg.equals("")) {
				args.add(arg);
			}

			join.part(p, args.toArray(new String[args.size()]));
			return true;
		}else if(cmd.equalsIgnoreCase("state")) {
			if(arg.contains(" ")) {
				args.addAll(Arrays.asList(arg.split(" ")));
			}else if(!arg.equals("")) {
				args.add(arg);
			}

			channel.setStatus(p, args.toArray(new String[args.size()]));
			return true;
		}else if(cmd.equalsIgnoreCase("members")) {
			if(arg.contains(" ")) {
				args.addAll(Arrays.asList(arg.split(" ")));
			}else if(!arg.equals("")) {
				args.add(arg);
			}

			channel.getMembers(p, args.toArray(new String[args.size()]));
			return true;
		}/*else if(cmd.equalsIgnoreCase("mail")) {
			
		}*/
		
		return false;
	}
	
}
