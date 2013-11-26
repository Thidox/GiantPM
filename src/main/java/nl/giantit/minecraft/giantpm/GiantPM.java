package nl.giantit.minecraft.giantpm;

import nl.giantit.minecraft.giantpm.core.config;
import nl.giantit.minecraft.giantpm.core.Database.db;
import nl.giantit.minecraft.giantpm.core.perm;
import nl.giantit.minecraft.giantpm.core.Tools.Muter.Muter;
import nl.giantit.minecraft.giantpm.Executors.chat;
import nl.giantit.minecraft.giantpm.Listeners.*;
import nl.giantit.minecraft.giantpm.core.Updater.Updater;
import nl.giantit.minecraft.giantpm.Misc.Messages;

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
	private Updater updater;
	private static Server Server;
	private db database;
	private perm perms;
	private chat chat;
	private Messages msgHandler;
	private String name, dir, pubName;
	private String bName = "Whining Spider";
	
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
		try {
			this.updater = new Updater(this);
			conf.loadConfig(configFile);
			this.database = new db(this);
			
			if(conf.getBoolean("GiantPM.permissions.usePermissions") == true) {
				getServer().getPluginManager().registerEvents(new PluginListener(this), this);
				if(conf.getString("GiantPM.permissions.permissionEngine").equals("sperm")) {
					setPermMan(new perm());
				}
			}
			
			pubName = conf.getString("GiantPM.global.name");
			chat = new chat(this);
			msgHandler = new Messages(this);
			
			getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
			getServer().getPluginManager().registerEvents(new ServerListener(this), this);
			
			log.log(Level.INFO, "[" + name + "](" + bName + ") was succesfully enabled");
		}catch(Exception e) {
			log.log(Level.SEVERE, "[" + this.name + "](" + this.bName + ") Failed to load!");
			if(conf.getBoolean("GiantPM.global.debug")) {
				log.log(Level.INFO, "" + e);
			}
			Server.getPluginManager().disablePlugin(this);
		}
	}
	
	@Override
	public void onDisable() {
		Muter.save();
		log.log(Level.INFO, "[" + name + "](" + bName + ") was succesfully disabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Who the hell are you?!");
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("pm")) {
			return chat.exec(sender, cmd, commandLabel, args);
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
	
	public Messages getMsgHandler() {
		return this.msgHandler;
	}
	
	public Updater getUpdater() {
		return this.updater;
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
