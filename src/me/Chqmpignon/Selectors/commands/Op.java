package me.Chqmpignon.Selectors.commands;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.entity.Entity;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import me.Chqmpignon.Selectors.api.Target;

import java.util.ArrayList;

public class Op extends VanillaCommand {

    public Op(String name) {
        super(name, "%nukkit.command.op.description", "%commands.op.description");
        this.setPermission("nukkit.command.op.give");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length == 0) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        } else {
            String name = args[0];

            ArrayList<Entity> entities = Target.parse(name, sender);
            for(Entity entity : entities) {
                if (entity instanceof Player) {
                    IPlayer player = sender.getServer().getOfflinePlayer(((Player) entity).getName());

                    Command.broadcastCommandMessage(sender, new TranslationContainer("commands.op.success", player.getName()));
                    if (player instanceof Player) {
                        ((Player) player).sendMessage(new TranslationContainer(TextFormat.GRAY + "%commands.op.message"));
                    }

                    player.setOp(true);
                }
            }
        }
        return true;
    }
}
