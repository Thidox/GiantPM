package nl.giantit.minecraft.GiantPM.Executors;

import nl.giantit.minecraft.GiantPM.GiantPM;
import nl.giantit.minecraft.GiantPM.core.Commands.*;
import nl.giantit.minecraft.GiantPM.Misc.Heraut;
import nl.giantit.minecraft.GiantPM.Misc.Misc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Giant
 */
public class chat {
	
	private GiantPM plugin;
	
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
		}else{
			Heraut.say(player, "Ok, we have no friggin clue what you are on about, so how about we just send you our help page?");
			help.help(player, args);
		}
		
		return true;
	}
}
