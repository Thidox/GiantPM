package nl.giantit.minecraft.GiantPM.Listeners;

import nl.giantit.minecraft.GiantPM.GiantPM;
import nl.giantit.minecraft.GiantPM.Misc.Heraut;
import nl.giantit.minecraft.GiantPM.Misc.Messages;
import nl.giantit.minecraft.GiantPM.Misc.Misc;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChatEvent;

/**
 *
 * @author Giant
 */
public class PlayerListener {
	
	GiantPM plugin;
	
	public PlayerListener(GiantPM plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		String message = event.getMessage();
		if(message.contains(": ")) {
			String[] tmp = message.split(": ");
			if(tmp.length > 0) {
				Player player = event.getPlayer();


			}
		}
	}
}
