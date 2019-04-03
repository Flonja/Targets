package me.Chqmpignon.Selectors.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import me.Chqmpignon.Selectors.api.Target;

import java.util.ArrayList;

public class Give extends VanillaCommand {

    public Give(String name) {
        super(name, "%nukkit.command.give.description", "%nukkit.command.give.usage");
        this.setPermission("nukkit.command.give");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("item", false, "itemType"), new CommandParameter("amount", CommandParamType.INT, true), new CommandParameter("meta", CommandParamType.INT, true), new CommandParameter("tags...", CommandParamType.RAWTEXT, true)});
        this.commandParameters.put("toPlayerById", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("item ID", CommandParamType.INT, false), new CommandParameter("amount", CommandParamType.INT, true), new CommandParameter("tags...", CommandParamType.RAWTEXT, true)});
        this.commandParameters.put("toPlayerByIdMeta", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("item ID:meta", CommandParamType.RAWTEXT, false), new CommandParameter("amount", CommandParamType.INT, true), new CommandParameter("tags...", CommandParamType.RAWTEXT, true)});
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length < 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        } else {
            Item item;
            try {
                item = Item.fromString(args[1]);
            } catch (Exception var8) {
                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }

            try {
                item.setCount(Integer.parseInt(args[2]));
            } catch (Exception var7) {
                item.setCount(item.getMaxStackSize());
            }

            ArrayList<Entity> entities = Target.parse(args[0], sender);
            for (Entity entity : entities) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;

                    if (player != null) {
                        if (item.getId() == 0) {
                            sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.give.item.notFound", args[1]));
                            return true;
                        } else {
                            player.getInventory().addItem(new Item[]{item.clone()});
                            Command.broadcastCommandMessage(sender, new TranslationContainer("%commands.give.success", new String[]{item.getName() + " (" + item.getId() + ":" + item.getDamage() + ")", String.valueOf(item.getCount()), player.getName()}));
                            return true;
                        }
                    } else {
                        sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"));
                        return true;
                    }
                }
            }
            return true;
        }
    }
}
