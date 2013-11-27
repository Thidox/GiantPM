package nl.giantit.minecraft.giantpm.core.Commands;

import nl.giantit.minecraft.giantpm.GiantPM;
import nl.giantit.minecraft.giantpm.core.Tools.Muter.Muter;
import nl.giantit.minecraft.giantpm.core.Tools.Channel.*;
import nl.giantit.minecraft.giantcore.Misc.Heraut;
import nl.giantit.minecraft.giantcore.Misc.Messages;
import nl.giantit.minecraft.giantcore.Misc.Misc;
import nl.giantit.minecraft.giantcore.perms.Permission;

import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;

/**
 *
 * @author Giant
 */
public class join {

    private static Messages mH = GiantPM.getPlugin().getMsgHandler();
    private static Permission perm = GiantPM.getPlugin().getPermHandler().getEngine();

    public static void join(Player p, String[] args) {
        if (perm.has(p, "giantpm.commands.join")) {
            if (args.length >= 1) {
                String name = "";
                for (String arg : args) {
                    if (Misc.isEitherIgnoreCase(arg, "join", "j")) {
                        continue;
                    }

                    name = arg;
                    break;
                }

                Player r = GiantPM.getPlugin().getServer().getPlayer(name);
                if (r == null) {
                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("player", name);

                    Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "playerUnfindable", data));
                    return;
                }

                Muter m = Muter.getMuter((OfflinePlayer) p);
                if (!m.isMuted((OfflinePlayer) r)) {
                    if (Channel.inChannel(r)) {
                        Channel c = Channel.getChannel(Channel.getPlayerChannelName(r));
                        ChannelResponse cR = c.joinChannel(p);
                        switch (cR) {
                            case CHANNELISPRIVATE:
                                Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "channelIsPrivate"));
                                return;
                            case CHANNELJOINED:
                                Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "channelJoined"));
                        }
                    } else {
                        HashMap<String, String> data = new HashMap<String, String>();
                        data.put("player", name);

                        Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "playerNotInChannel", data));
                    }
                } else {
                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("player", name);

                    Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "joiningMutedPlayer", data));
                }
            } else {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("type", "join");

                Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "noPlayerPassed"));
            }
        } else {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("command", "join");
            Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "noPermission", data));
        }
    }

    public static void part(Player p, String[] args) {
        if (perm.has(p, "giantpm.commands.part")) {
            if (Channel.inChannel(p)) {
                Channel c = Channel.getChannel(Channel.getPlayerChannelName(p));
                c.leaveChannel(p);
            } else {
                Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "notInChannel"));
            }
        } else {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("command", "part");
            Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "noPermissions", data));
        }
    }
}
