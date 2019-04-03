package me.Chqmpignon.Selectors.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import me.Chqmpignon.Selectors.api.Target;

import java.util.ArrayList;

public class Kick extends VanillaCommand {

    public Kick(String name) {
        super(name, "%nukkit.command.kick.description", "%commands.kick.usage");
        this.setPermission("nukkit.command.kick");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("reason", true)});
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length == 0) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        } else {
            String name = args[0];
            String reason = "";

            for(int i = 1; i < args.length; ++i) {
                reason = reason + args[i] + " ";
            }

            if (reason.length() > 0) {
                reason = reason.substring(0, reason.length() - 1);
            }

            ArrayList<Entity> entities = Target.parse(name, sender);
            for(Entity entity : entities) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;

                    if (player != null) {
                        player.kick(PlayerKickEvent.Reason.KICKED_BY_ADMIN, reason);
                        if (reason.length() >= 1) {
                            Command.broadcastCommandMessage(sender, new TranslationContainer("commands.kick.success.reason", new String[]{player.getName(), reason}));
                        } else {
                            Command.broadcastCommandMessage(sender, new TranslationContainer("commands.kick.success", player.getName()));
                        }
                    } else {
                        sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"));
                    }
                }
            }
        }
        return true;
    }
}
