package me.Chqmpignon.Selectors.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.entity.Entity;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import me.Chqmpignon.Selectors.api.Target;

import java.util.ArrayList;

public class Me extends VanillaCommand {

    public Me(String name) {
        super(name, "%nukkit.command.me.description", "%commands.me.usage");
        this.setPermission("nukkit.command.me");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("action ...", CommandParamType.RAWTEXT, false)});
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length == 0) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        } else {
            String name;
            if (sender instanceof Player) {
                name = ((Player)sender).getDisplayName();
            } else {
                name = sender.getName();
            }

            String msg = "";

            for(int i = 0; i < args.length; ++i) {
                String arg = args[i];

                String parsedMsg = "";
                ArrayList<Entity> entities = Target.parse(arg, sender);
                if(entities.size() != 0) {
                    for (int j = 0; j < entities.size(); j++) {
                        if (j == entities.size() - 2) {
                            parsedMsg = parsedMsg + entities.get(j).getName() + " and ";
                        } else {
                            if (j == entities.size() - 1) {
                                parsedMsg = parsedMsg + entities.get(j).getName();
                            } else {
                                parsedMsg = parsedMsg + entities.get(j).getName() + ", ";
                            }
                        }
                    }
                }else{
                    parsedMsg = arg;
                }

                msg = msg + parsedMsg + " ";
            }

            if (msg.length() > 0) {
                msg = msg.substring(0, msg.length() - 1);
            }

            sender.getServer().broadcastMessage(new TranslationContainer("chat.type.emote", new String[]{name, TextFormat.WHITE + msg}));
            return true;
        }
    }
}
