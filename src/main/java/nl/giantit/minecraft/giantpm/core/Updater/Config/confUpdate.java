package nl.giantit.minecraft.giantpm.core.Updater.Config;

import nl.giantit.minecraft.giantpm.GiantPM;
import nl.giantit.minecraft.giantpm.core.Updater.iUpdater;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

public class confUpdate implements iUpdater {
	
	private void export(File file, FileConfiguration c) {
		try {
			InputStream iS = new ByteArrayInputStream(c.saveToString().replace("\n", "\r\n").replace("  ", "    ").getBytes("UTF-8"));
			GiantPM.getPlugin().extract(file, iS);
		}catch(UnsupportedEncodingException e) {
			GiantPM.getPlugin().getLogger().severe("Failed to update config file!");
			if(c.getBoolean("GiantTitle.global.debug", true) == true) {
				GiantPM.getPlugin().getLogger().log(Level.INFO, e.getMessage(), e);
			}
		}
	}
	
	public void Update(double curV, FileConfiguration c) {
		
	}
	
}
