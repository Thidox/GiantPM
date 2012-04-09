package nl.giantit.minecraft.GiantPM;

import nl.giantit.minecraft.GiantPM.Executors.chat;
import nl.giantit.minecraft.GiantPM.Misc.Messages;
import nl.giantit.minecraft.GiantPM.Misc.Storage;
import nl.giantit.minecraft.GiantPM.core.config;
import nl.giantit.minecraft.GiantPM.core.perm;
import nl.giantit.minecraft.GiantPM.core.Database.db;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

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
	
	private static GiantPM plugin;
	private static Server Server;
	private db database;
	private perm perms;
	private chat chat;
	private Messages msgHandler;
	private Storage storHandler;
	private String name, dir, pubName;
	private String bName = "Chatty Cow";
	
	public static final Logger log = Logger.getLogger("Minecraft");
	
	private void setPlugin() {
		GiantPM.plugin = this;
	}
	
	public GiantPM() {
		this.setPlugin();
	}
	
	@Override
	public void onEnable() {
		Server = this.getServer();
		
		this.name = getDescription().getName();
		this.dir = getDataFolder().toString();
		
		File configFile = new File(getDataFolder(), "conf.yml");
		if(!configFile.exists()) {
			getDataFolder().mkdir();
			getDataFolder().setWritable(true);
			getDataFolder().setExecutable(true);
			
			extractDefaultFile("conf.yml");
		}
		config conf = config.Obtain();
		
		storHandler = new Storage();
		
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		
		log.log(Level.INFO, "[" + name + "](" + bName + ") was succesfully enabled");
	}
	
	@Override
	public void onDisable() {
		log.log(Level.INFO, "[" + name + "](" + bName + ") was succesfully disabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Who the hell are you?!");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("pm")) {
			chat.exec(sender, cmd, commandLabel, args);
			/*if(args.length == 0 || args[0].equals("help")) {
				Messager.say((Player) sender, "To use private messaging use following syntax:");
				Messager.say((Player) sender, "[player](, [player]): [message]");
			}*/
			return true;
		}
		return false;
	}
	
	public String getPubName() {
		return this.pubName;
	}
	
	public String getDir() {
		return this.dir;
	}
	
	public String getSeparator() {
		return File.separator;
	}
	
	public db getDB() {
		return this.database;
	}
	
	public perm getPermMan() {
		return this.perms;
	}
	
	public void setPermMan(perm perm) {
		this.perms = perm;
	}
	
	public Server getSrvr() {
		return getServer();
	}
	
	public Storage getStorageHandler() {
		return this.storHandler;
	}
	
	public Messages getMsgHandler() {
		return this.msgHandler;
	}
	
	public static GiantPM getPlugin() {
		return GiantPM.plugin;
	}
	
	public void extract(String file) {
		extractDefaultFile(file);
	}
	
	private void extractDefaultFile(String file) {
		File configFile = new File(getDataFolder(), file);
		if (!configFile.exists()) {
			InputStream input = this.getClass().getResourceAsStream("/nl/giantit/minecraft/" + name + "/core/Default/" + file);
			if (input != null) {
				FileOutputStream output = null;

				try {
					output = new FileOutputStream(configFile);
					byte[] buf = new byte[8192];
					int length = 0;

					while ((length = input.read(buf)) > 0) {
						output.write(buf, 0, length);
					}

					log.log(Level.INFO, "[" + name + "] copied default file: " + file);
				} catch (Exception e) {
					Server.getPluginManager().disablePlugin(this);
					log.log(Level.SEVERE, "[" + name + "] AAAAAAH!!! Can't extract the requested file!!", e);
					return;
				} finally {
					try {
						if (input != null) {
							input.close();
						}
					} catch (Exception e) {
						Server.getPluginManager().disablePlugin(this);
						log.log(Level.SEVERE, "[" + name + "] AAAAAAH!!! Severe error!!", e);	
					}
					try {
						if (output != null) {
							output.close();
						}
					} catch (Exception e) {
						Server.getPluginManager().disablePlugin(this);
						log.log(Level.SEVERE, "[" + name + "] AAAAAAH!!! Severe error!!", e);
					}
				}
			}
		}
	}
}
