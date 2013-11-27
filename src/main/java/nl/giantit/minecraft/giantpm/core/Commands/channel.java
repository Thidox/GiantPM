package nl.giantit.minecraft.giantpm.core.Commands;

import nl.giantit.minecraft.giantpm.GiantPM;
import nl.giantit.minecraft.giantpm.core.Tools.Channel.Channel;
import nl.giantit.minecraft.giantcore.Misc.Heraut;
import nl.giantit.minecraft.giantcore.Misc.Messages;
import nl.giantit.minecraft.giantcore.Misc.Misc;
import nl.giantit.minecraft.giantcore.perms.Permission;

import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 *
 * @author Giant
 */
public class channel {

    private static Messages mH = GiantPM.getPlugin().getMsgHandler();
    private static Permission perm = GiantPM.getPlugin().getPermHandler().getEngine();

    public static void setStatus(Player p, String[] args) {
        if (perm.has(p, "giantpm.commands.state")) {
            if (Channel.inChannel(p)) {
                Channel c = Channel.getChannel(Channel.getPlayerChannelName(p));
                if (c.isOwner(p)) {
                    if (args.length >= 1) {
                        String type = "invite";

                        for (String arg : args) {
                            if (Misc.isEitherIgnoreCase(arg, "state", "s")) {
                                continue;
                            }

                            type = arg;
                            break;
                        }

                        if (Misc.isAnyIgnoreCase(type, "public", "pub", "pu", "0")) {
                            c.setStatus("0");
                        } else if (Misc.isAnyIgnoreCase(type, "invite", "inv", "i", "1")) {
                            c.setStatus("1");
                        } else if (Misc.isAnyIgnoreCase(type, "private", "priv", "pr", "2")) {
                            c.setStatus("2");
                        } else {
                            HashMap<String, String> data = new HashMap<String, String>();
                            data.put("state", type);

                            Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "channelStatusInvalid", data));
                        }
                    }
                } else {
                    Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "notChannelOwner"));
                }
            } else {
                Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "notInChannel"));
            }
        } else {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("command", "state");
            Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "noPermissions", data));
        }
    }

    public static void getMembers(Player p, String[] args) {
        if (perm.has(p, "giantpm.commands.members")) {
            if (Channel.inChannel(p)) {
                Channel c = Channel.getChannel(Channel.getPlayerChannelName(p));
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("member", c.getMembersString());

                Heraut.say(p, mH.getMsg(Messages.msgType.MAIN, "channelMemberList", data));
            } else {
                Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "notInChannel"));
            }
        } else {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("command", "members");
            Heraut.say(p, mH.getMsg(Messages.msgType.ERROR, "noPermissions", data));
        }
    }
}
