package nl.giantit.bukkit.GiantPM.Misc;

import java.util.HashMap;

/**
 *
 * @author Giant
 */
public class Storage {
	
	HashMap<String, Object> stored = new HashMap<String, Object>();
	
	
	public Storage() {
		
	}
	
	public void set(String item, Object object) {
		Object test = stored.get(item);
		if(test == null) {
			stored.put(item, object);
		}
	}
	
	public void set(String item, Object object, Boolean overwrite) {
		Object test = stored.get(item);
		if(test == null) {
			stored.put(item, object);
		}else if(true == overwrite) {
			stored.put(item, object);
		}
	}
	
	public Object get(String item) {
		Object object = stored.get(item);
		if(object != null) {
			return object;
		}
		return null;
	}
}
