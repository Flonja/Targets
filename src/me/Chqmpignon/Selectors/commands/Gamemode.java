package me.Chqmpignon.Selectors.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
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

public class Gamemode extends VanillaCommand {

    public Gamemode(String name) {
        super(name, "%nukkit.command.gamemode.description", "%commands.gamemode.usage", new String[]{"gm"});
        this.setPermission("nukkit.command.gamemode.survival;nukkit.command.gamemode.creative;nukkit.command.gamemode.adventure;nukkit.command.gamemode.spectator;nukkit.command.gamemode.other");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("mode", CommandParamType.INT, false), new CommandParameter("player", CommandParamType.TARGET, true)});
        this.commandParameters.put("byString", new CommandParameter[]{new CommandParameter("mode", new String[]{"survival", "s", "creative", "c", "adventure", "a", "spectator", "spc", "view", "v"}), new CommandParameter("player", CommandParamType.TARGET, true)});
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        } else {
             int gameMode = Server.getGamemodeFromString(args[0]);
             if (gameMode == -1) {
                 sender.sendMessage("Unknown game mode");
                 return true;
             } else {
                 CommandSender target = sender;
                 if (args.length > 1) {
                     if (!sender.hasPermission("nukkit.command.gamemode.other")) {
                         sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                         return true;
                     }

                     ArrayList<Entity> entities = Target.parse(args[1], sender);
                     for (Entity entity : entities) {
                         if (entity instanceof Player) {
                             target = (Player) entity;

                             if (target == null) {
                                 sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"));
                                 return true;
                             }

                             if (gameMode == 0 && !sender.hasPermission("nukkit.command.gamemode.survival") || gameMode == 1 && !sender.hasPermission("nukkit.command.gamemode.creative") || gameMode == 2 && !sender.hasPermission("nukkit.command.gamemode.adventure") || gameMode == 3 && !sender.hasPermission("nukkit.command.gamemode.spectator")) {
                                 sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                                 return true;
                             } else {
                                 if (!((Player) target).setGamemode(gameMode)) {
                                     sender.sendMessage("Game mode update for " + ((CommandSender) target).getName() + " failed");
                                 } else if (target.equals(sender)) {
                                     Command.broadcastCommandMessage(sender, new TranslationContainer("commands.gamemode.success.self", Server.getGamemodeString(gameMode)));
                                 } else {
                                     ((CommandSender) target).sendMessage(new TranslationContainer("gameMode.changed", new String[]{Server.getGamemodeString(gameMode)}));
                                     Command.broadcastCommandMessage(sender, new TranslationContainer("commands.gamemode.success.other", new String[]{Server.getGamemodeString(gameMode), ((CommandSender) target).getName()}));
                                 }
                                 return true;
                             }
                         }
                     }
                 }else{
                     if (gameMode == 0 && !sender.hasPermission("nukkit.command.gamemode.survival") || gameMode == 1 && !sender.hasPermission("nukkit.command.gamemode.creative") || gameMode == 2 && !sender.hasPermission("nukkit.command.gamemode.adventure") || gameMode == 3 && !sender.hasPermission("nukkit.command.gamemode.spectator")) {
                         sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                         return true;
                     } else {
                         if (!((Player) target).setGamemode(gameMode)) {
                             sender.sendMessage("Game mode update for " + ((CommandSender) target).getName() + " failed");
                         } else if (target.equals(sender)) {
                             Command.broadcastCommandMessage(sender, new TranslationContainer("commands.gamemode.success.self", Server.getGamemodeString(gameMode)));
                         } else {
                             ((CommandSender) target).sendMessage(new TranslationContainer("gameMode.changed", new String[]{Server.getGamemodeString(gameMode)}));
                             Command.broadcastCommandMessage(sender, new TranslationContainer("commands.gamemode.success.other", new String[]{((CommandSender) target).getName(), Server.getGamemodeString(gameMode)}));
                         }
                         return true;
                     }
                 }
             }
             return true;
        }
    }
}
