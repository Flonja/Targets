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
import java.util.Objects;

public class Tell extends VanillaCommand {

    public Tell(String name) {
        super(name, "%nukkit.command.tell.description", "%commands.message.usage", new String[]{"w", "msg"});
        this.setPermission("nukkit.command.tell");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("message")});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length < 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        } else {
            String name = args[0].toLowerCase();
            ArrayList<Entity> entities = Target.parse(name, sender);
            for(Entity entity : entities) {
                if(entity instanceof Player) {
                    Player player = (Player) entity;
                    if (player == null) {
                        sender.sendMessage(new TranslationContainer("commands.generic.player.notFound"));
                        return true;
                    } else if (Objects.equals(player, sender)) {
                        sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.message.sameTarget"));
                        return true;
                    } else {
                        String msg = "";

                        for(int i = 1; i < args.length; ++i) {
                            msg = msg + args[i] + " ";
                        }

                        if (msg.length() > 0) {
                            msg = msg.substring(0, msg.length() - 1);
                        }

                        String displayName = sender instanceof Player ? ((Player)sender).getDisplayName() : sender.getName();
                        sender.sendMessage("[" + sender.getName() + " -> " + player.getDisplayName() + "] " + msg);
                        player.sendMessage("[" + displayName + " -> " + player.getName() + "] " + msg);
                        return true;
                    }
                }
            }
        }
        return true;
    }
}
