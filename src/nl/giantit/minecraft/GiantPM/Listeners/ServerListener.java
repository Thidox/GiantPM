package nl.giantit.minecraft.GiantPM.Listeners;

import nl.giantit.minecraft.GiantPM.GiantPM;
import nl.giantit.minecraft.GiantPM.core.config;
import nl.giantit.minecraft.GiantPM.core.perm;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

import ru.tehkode.permissions.bukkit.*;
import ru.tehkode.permissions.*;

/**
 *
 * @author Giant
 */
public class ServerListener implements Listener {
	
	private GiantPM plugin;
	private config conf = config.Obtain();
	
	public ServerListener(GiantPM plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		if (plugin.getPermMan() != null) {
			if (event.getPlugin().getDescription().getName().equals("PermissionsEx")) {
				plugin.setPermMan(null);
				plugin.log.log(Level.INFO, "[" + plugin.getName() + "] un-hooked from PermissionsEX.");
				plugin.getPluginLoader().disablePlugin(plugin);
			}
		}
	}

	@EventHandler
	public void onPluginEnable(PluginEnableEvent event) {
		if(conf.getBoolean("GiantPM.permissions.usePermissions") == true) {
			if(conf.getString("GiantPM.permissions.permissionEngine").equals("PEX")) {
				if (plugin.getPermMan() == null) {
					Plugin PermissionsEx = plugin.getServer().getPluginManager().getPlugin("PermissionsEx");

					if (PermissionsEx != null) {
						if (PermissionsEx.isEnabled() && PermissionsEx.getClass().getName().equals("ru.tehkode.permissions.bukkit.PermissionsEx")) {
							PermissionsEx pex = (PermissionsEx)PermissionsEx;
							plugin.setPermMan(new perm((PermissionManager)pex.getPermissionManager()));
							plugin.log.log(Level.INFO, "[" + plugin.getName() + "] hooked into PermissionsEX.");
						}
					}
				}
			}
		}
	}
}
