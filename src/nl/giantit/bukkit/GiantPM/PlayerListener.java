package nl.giantit.bukkit.GiantPM;

import nl.giantit.bukkit.GiantPM.Misc.Messager;

//import java.util.logging.Level;
//import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChatEvent;

/**
 *
 * @author Giant
 */
public class PlayerListener implements Listener {
	
	private GiantPM plugin;
	
	public PlayerListener (GiantPM plugin) {
		this.plugin = plugin;
	}
	
	public boolean containsIgnoreCase(ArrayList <String> l, String s){
		Iterator<String> it = l.iterator();
		while(it.hasNext()){
			if(it.next().equalsIgnoreCase(s))
				return true;
		}
		return false;
	 } 
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		if(!event.isCancelled()) {
			String message = event.getMessage();
			if(message.contains(": ")) {
				String[] tmp = message.split(": ");
				if(tmp.length > 0) {
					Player player = event.getPlayer();
					Messager.savePlayer(player);
					
					int msged = 0;

					String msg = "";

					int i = 0;
					for(String part : tmp) {
						i++;
						if(i == 1)
							continue;

						msg += part;
					}

					String[] users = new String[]{};
					if(tmp[0].contains(", ")) {
						users = tmp[0].split(", ");
					}else{
						users = new String[]{tmp[0]};
					}
					
					ArrayList<String> reply = new ArrayList<String>();
					reply.add("r");
					reply.add("re");
					reply.add("rep");
					reply.add("repl");
					reply.add("reply");

					if(containsIgnoreCase(reply, tmp[0])) {
						Player target = (Player) plugin.Storage.get(player.getName());
						if(target == null) {
							Messager.say("&6Sorry no one to reply to! :(");
							event.setCancelled(true);
						}else{
							plugin.Storage.set(target.getName(), player, true);
							Messager.say(target, "&2" + player.getName() + " &6whispers&f: &3" + msg);
							Messager.say("&6Sending private message reply to&f: &2" + target.getName());
							Messager.say("&6message&f: &3" + msg);
							event.setCancelled(true);
						}
					}else{
						ArrayList<Player> receiverts = new ArrayList<Player>();
						Boolean broken = false;

						for(String user : users) {
							if(user.contains(" ")) {
								broken = true;
								break;
							}

							user = Messager.clean(user);

							if(plugin.Server.getPlayer(user) == null)
								continue;

							receiverts.add(plugin.Server.getPlayer(user));


							/*Player receiver = plugin.Server.getPlayer(user);
							if(receiver != null) {
								Messager.say(receiver, "&2" + player.getName() + " &6whispers&f: &3" + msg);
								msged++;
							}*/
						}

						if(false == broken) {
							String receivers = "";
							for(Player receiver : receiverts) {
								if(receiver != null) {
									if(receivers.length() > 0) 
										receivers += "&f, &2";
									receivers += receiver.getName();

									plugin.Storage.set(receiver.getName(), player, true);

									Messager.say(receiver, "&2" + player.getName() + " &6whispers&f: &3" + msg);
									msged++;
								}
							}

							if(msged > 0) {
								//String receivers = receiverts.toString();
								receivers = receivers.replace(", ", "&f, &2");
								Messager.say("&6Sending private message to&f: &2" + receivers);
								Messager.say("&6message&f: &3" + msg);
								event.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
}
