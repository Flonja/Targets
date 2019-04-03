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
import me.Chqmpignon.Selectors.api.Target;

import java.util.ArrayList;
import java.util.Date;

public class Ban extends VanillaCommand {

    public Ban(String name) {
        super(name, "%nukkit.command.ban.player.description", "%commands.ban.usage");
        this.setPermission("nukkit.command.ban.player");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("reason", CommandParamType.STRING, true)});
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

            for (int i = 1; i < args.length; ++i) {
                reason = reason + args[i] + " ";
            }

            if (reason.length() > 0) {
                reason = reason.substring(0, reason.length() - 1);
            }

            ArrayList<Entity> entities = Target.parse(name, sender);
            for(Entity entity : entities) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;

                    sender.getServer().getNameBans().addBan(player.getName(), reason, (Date) null, sender.getName());
                    if (player != null) {
                        player.kick(PlayerKickEvent.Reason.NAME_BANNED, !reason.isEmpty() ? "Banned by admin. Reason: " + reason : "Banned by admin");
                    }

                    Command.broadcastCommandMessage(sender, new TranslationContainer("%commands.ban.success", player != null ? player.getName() : name));
                }
            }
        }
        return true;
    }
}
