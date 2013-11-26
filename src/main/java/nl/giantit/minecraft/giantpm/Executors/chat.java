package nl.giantit.minecraft.giantpm.Executors;

import nl.giantit.minecraft.giantcore.Misc.Heraut;
import nl.giantit.minecraft.giantcore.Misc.Misc;

import nl.giantit.minecraft.giantpm.GiantPM;
import nl.giantit.minecraft.giantpm.core.Commands.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Giant
 */
public class chat {
	
	private final GiantPM plugin;
	
	public chat(GiantPM plugin) {
		this.plugin = plugin;
	}

	public boolean exec(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		
		if(args.length == 0 || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("?")) {
			help.help(player, args);
		}else if(Misc.isEitherIgnoreCase(args[0], "mute", "m")) {
			mute.mute(player, args);
		}else if(Misc.isEitherIgnoreCase(args[0], "unmute", "um")) {
			mute.unmute(player, args);
		}else if(Misc.isEitherIgnoreCase(args[0], "inv", "i")) {
			inv.inv(player, args);
		}else if(Misc.isEitherIgnoreCase(args[0], "join", "j")) {
			join.join(player, args);
		}else if(Misc.isEitherIgnoreCase(args[0], "part", "p")) {
			join.part(player, args);
		}else if(Misc.isEitherIgnoreCase(args[0], "state", "s")) {
			channel.setStatus(player, args);
		}else if(Misc.isEitherIgnoreCase(args[0], "members", "mem")) {
			channel.getMembers(player, args);
		}else{
			Heraut.say(player, "Ok, we have no friggin clue what you are on about, so how about we just send you our help page?");
			help.help(player, args);
		}
		
		return true;
	}
}
