package nl.giantit.minecraft.giantpm;

import nl.giantit.minecraft.giantcore.database.Database;
import nl.giantit.minecraft.giantcore.GiantCore;
import nl.giantit.minecraft.giantcore.GiantPlugin;
import nl.giantit.minecraft.giantcore.Misc.Messages;
import nl.giantit.minecraft.giantcore.core.Eco.Eco;
import nl.giantit.minecraft.giantcore.perms.PermHandler;

import nl.giantit.minecraft.giantpm.core.Config;
import nl.giantit.minecraft.giantpm.core.Tools.Muter.Muter;
import nl.giantit.minecraft.giantpm.Executors.chat;
import nl.giantit.minecraft.giantpm.Listeners.*;
import nl.giantit.minecraft.giantpm.core.Updater.Updater;

import java.util.logging.Level;
import java.io.File;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 *
 * @author Giant
 */
public class GiantPM extends GiantPlugin {
	
	private static GiantPM plugin;
	private Updater updater;
	
	private GiantCore gc;
	
	private Database db;
	private PermHandler permHandler;
	private chat chat;
	private Messages msgHandler;
	private String name, dir, pubName;
	private String bName = "Whining Spider";
	
	private void setPlugin() {
		GiantPM.plugin = this;
	}
	
	public GiantPM() {
		this.setPlugin();
	}
	
	@Override
	public void onEnable() {
		this.gc = GiantCore.getInstance();
		if(this.gc == null) {
			getLogger().severe("Failed to hook into required GiantCore!");
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		
		if(this.gc.getProtocolVersion() < 0.3) {
			getLogger().severe("The GiantCore version you are using it not made for this plugin!");
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		
		this.name = getDescription().getName();
		this.dir = getDataFolder().toString();
		
		File configFile = new File(getDataFolder(), "conf.yml");
		if(!configFile.exists()) {
			getDataFolder().mkdir();
			getDataFolder().setWritable(true);
			getDataFolder().setExecutable(true);
			
			this.extract("conf.yml");
			if(!configFile.exists()) {
				getLogger().severe("Failed to extract configuration file!");
				this.getPluginLoader().disablePlugin(this);
				return;
			}
		}
		
		Config conf = Config.Obtain(this);
		try {
			this.updater = new Updater(this);
			conf.loadConfig(configFile);
			if(!conf.isLoaded()) {
				getLogger().severe("Failed to load configuration file!");
				this.getPluginLoader().disablePlugin(this);
				return;
			}
			
			HashMap<String, String> dbConf = conf.getMap(this.name + ".db.mc");
			dbConf.put("debug", conf.getString(this.name + ".global.debug"));
			
			this.db = this.gc.getDB(this, null, dbConf);
			
			if(conf.getBoolean(this.name + ".permissions.usePermissions")) {
				permHandler = this.gc.getPermHandler(PermHandler.findEngine(conf.getString(this.name + ".permissions.Engine")), conf.getBoolean(this.name + ".permissions.opHasPerms"));
			}else{
				permHandler = this.gc.getPermHandler(PermHandler.findEngine("NOPERM"), true);
			}
			
			pubName = conf.getString("GiantPM.global.name");
			chat = new chat(this);
			msgHandler = new Messages(this, 0.3);
			
			getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
			getServer().getPluginManager().registerEvents(new ServerListener(this), this);
			
			getLogger().log(Level.INFO, "[" + name + "](" + bName + ") was succesfully enabled");
		}catch(Exception e) {
			getLogger().log(Level.SEVERE, "[" + this.name + "](" + this.bName + ") Failed to load!");
			if(conf.getBoolean("GiantPM.global.debug")) {
				getLogger().log(Level.INFO, "" + e);
			}
			this.getServer().getPluginManager().disablePlugin(this);
		}
	}
	
	@Override
	public void onDisable() {
		Muter.save();
		this.db.getEngine().close();
		getLogger().log(Level.INFO, "[" + name + "](" + bName + ") was succesfully disabled");
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
	
	@Override
	public GiantCore getGiantCore() {
		return this.gc;
	}
	
	@Override
	public String getPubName() {
		return this.pubName;
	}
	
	@Override
	public String getDir() {
		return this.dir;
	}
	
	@Override
	public Database getDB() {
		return this.db;
	}
	
	@Override
	public PermHandler getPermHandler() {
		return this.permHandler;
	}
	
	@Override
	public Eco getEcoHandler() {
		return null;
	}
	
	@Override
	public Messages getMsgHandler() {
		return this.msgHandler;
	}
	
	public Updater getUpdater() {
		return this.updater;
	}
	
	public static GiantPM getPlugin() {
		return GiantPM.plugin;
	}
}
