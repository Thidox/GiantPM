package nl.giantit.minecraft.giantpm.Listeners;

import nl.giantit.minecraft.giantpm.GiantPM;
import nl.giantit.minecraft.giantpm.Misc.Messages;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class ServerListener implements Listener {

	private GiantPM plugin;
	private Messages mH;
	
	public ServerListener(GiantPM plugin) {
		this.plugin = plugin;
		this.mH = plugin.getMsgHandler();
	}
	
	
	@EventHandler
	public void onServerCommand(ServerCommandEvent event) {
		event.getSender().sendMessage(event.getCommand());
	}
}
