package nl.giantit.minecraft.giantpm.core.Updater;

import nl.giantit.minecraft.giantpm.GiantPM;
import nl.giantit.minecraft.giantpm.core.Updater.Config.confUpdate;

public class Updater {
	
	private final GiantPM plugin;
	
	public Updater(GiantPM plugin) {
		this.plugin = plugin;
	}
	
	public iUpdater getUpdater(UpdateType t) {
		switch(t) {
			case CONFIG:
				return new confUpdate();
			default:
				break;
		}
		
		return null;
	}
	
}
