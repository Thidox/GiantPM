package nl.giantit.minecraft.giantpm.core;

import nl.giantit.minecraft.giantpm.GiantPM;
import nl.giantit.minecraft.giantpm.core.Updater.UpdateType;
import nl.giantit.minecraft.giantpm.core.Updater.Config.confUpdate;

import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 *
 * @author Giant
 */
public class Config {
	
	private static Config instance = null;

	private final GiantPM plugin;
	private YamlConfiguration c;
	private File file;
	private final double version = 0.2;

	private Config(GiantPM p) {
		this.plugin = p;
	}
	
	public boolean isLoaded() {
		return null != c;
	}
	
	public void loadConfig(File file) {
		this.file = file;
		this.c = YamlConfiguration.loadConfiguration(this.file);
		
		double v = this.getDouble(plugin.getName() + ".global.version");
		if(v < this.version) {
			confUpdate cU = (confUpdate) plugin.getUpdater().getUpdater(UpdateType.CONFIG);
			File oconfigFile = new File(plugin.getDir(), "conf.yml." + v + ".bak");
			try {
				Files.copy(file, oconfigFile);
				cU.Update(v, c);
				
				this.c = YamlConfiguration.loadConfiguration(new File(plugin.getDir(), "conf.yml"));
			}catch(IOException e) {
				plugin.getLogger().severe("Failed to update config file!");
				if(c.getBoolean("GiantTitle.global.debug", true) == true) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void reload() {
		this.c = null;
		this.c = YamlConfiguration.loadConfiguration(this.file);
		
		double v = this.getDouble(plugin.getName() + ".global.version");
		if(v < this.version) {
			confUpdate cU = (confUpdate) plugin.getUpdater().getUpdater(UpdateType.CONFIG);
			File oconfigFile = new File(plugin.getDir(), "conf.yml." + v + ".bak");
			try {
				Files.copy(file, oconfigFile);
				cU.Update(v, c);
				
				this.c = YamlConfiguration.loadConfiguration(new File(plugin.getDir(), "conf.yml"));
			}catch(IOException e) {
				plugin.getLogger().severe("Failed to update config file!");
				if(c.getBoolean("GiantTitle.global.debug", true) == true) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String getString(String setting) {
		return this.getString(setting, "");
	}
	
	public String getString(String setting, String def) {
		return this.c.getString(setting, def);
	}
	
	public List<String> getStringList(String setting) {
		return this.c.getStringList(setting);
	}
	
	public boolean getBoolean(String setting) {
		return this.c.getBoolean(setting, false);
	}
	
	public boolean getBoolean(String setting, boolean b) {
		return null != this.c ? this.c.getBoolean(setting, b) : b;
	}
	
	public int getInt(String setting) {
		return this.getInt(setting, 0);
	}
	
	public int getInt(String setting, int def) {
		return this.c.getInt(setting, def);
	}
	
	public double getDouble(String setting) {
		return this.getDouble(setting, 0);
	}
	
	public double getDouble(String setting, double def) {
		return this.c.getDouble(setting, def);
	}
	
	public long getLong(String setting) {
		return this.getLong(setting, 0);
	}
	
	public long getLong(String setting, long def) {
		return this.c.getLong(setting, def);
	}

	public HashMap<String, String> getMap(String setting) {
		HashMap<String, String> m = new HashMap<String, String>();
		Set<String> t = this.c.getConfigurationSection(setting).getKeys(false);
		if(t == null) {
			plugin.getLogger().log(Level.SEVERE, "Section " + setting + " was not found in the conf.yml! It might be broken...");
		}
		
		for(String i : t) {
			m.put(i, String.valueOf(this.c.get(setting + "." + i)));
		}
		
		return m;
	}
	
	public static Config Obtain(GiantPM p) {
		if(Config.instance == null)
			Config.instance = new Config(p);
		
		return Config.instance;
	}
	
	public static Config Obtain() {
		if(Config.instance == null)
			Config.instance = new Config(GiantPM.getPlugin());
		
		return Config.instance;
	}
	
	public static void Kill() {
		if(Config.instance != null)
			Config.instance = null;
	}
}
