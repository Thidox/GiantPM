package nl.giantit.bukkit.GiantPM;

import nl.giantit.bukkit.GiantPM.Misc.Storage;
import nl.giantit.bukkit.GiantPM.Misc.Messager;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Giant
 */
public class GiantPM extends JavaPlugin {
	public static final Logger log = Logger.getLogger("Minecraft");
	public static Server Server;
	
	public static String directory;
	public static File dir;
	
	public static String name = "GiantPM";
	public static String codename = "Chatting Cows";
	public static String version = "2.0";
	
	public GiantPM plugin;
	public PlayerListener listen;
	public Storage Storage;
	
	@Override
	public void onEnable() {
		Server = this.getServer();
		
		getDataFolder().mkdir();
		getDataFolder().setWritable(true);
		getDataFolder().setExecutable(true);

		directory = getDataFolder() + File.separator;
		dir = getDataFolder();
		plugin = this;
		name = getDescription().getName();
		version = getDescription().getVersion();
		
		Storage = new Storage();
		listen = new PlayerListener(this);
		
		getServer().getPluginManager().registerEvents(listen, this);
		
		log.log(Level.INFO, "[" + name + "][" + version + "](" + codename + ") was succesfully enabled");
	}
	
	@Override
	public void onDisable() {
		log.log(Level.INFO, "[" + name + "][" + version + "](" + codename + ") was succesfully disabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Who the hell are you?!");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("pm")) {
			if(args.length == 0 || args[0].equals("help")) {
				Messager.say((Player) sender, "To use private messaging use following syntax:");
				Messager.say((Player) sender, "[player](, [player]): [message]");
			}
			return true;
		}
		return false;
	}
}
