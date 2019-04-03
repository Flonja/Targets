package me.Chqmpignon.Selectors.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.entity.Entity;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import me.Chqmpignon.Selectors.api.Target;

import java.util.ArrayList;

public class Title extends VanillaCommand {

    public Title(String name) {
        super(name, "%nukkit.command.title.description", "%nukkit.command.title.usage");
        this.setPermission("nukkit.command.title");

        this.commandParameters.clear();
        this.commandParameters.put("clear", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("clear", new String[]{"clear"})});
        this.commandParameters.put("reset", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("reset", new String[]{"reset"})});
        this.commandParameters.put("title", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("title", new String[]{"title"}), new CommandParameter("titleText", CommandParamType.STRING, false)});
        this.commandParameters.put("subtitle", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("subtitle", new String[]{"subtitle"}), new CommandParameter("titleText", CommandParamType.STRING, false)});
        this.commandParameters.put("actionbar", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("actionbar", new String[]{"actionbar"}), new CommandParameter("titleText", CommandParamType.STRING, false)});
        this.commandParameters.put("times", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("times", new String[]{"times"}), new CommandParameter("fadeIn", CommandParamType.INT, false), new CommandParameter("stay", CommandParamType.INT, false), new CommandParameter("fadeOut", CommandParamType.INT, false)});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }

        ArrayList<Entity> entities = Target.parse(args[0], sender);
        for(Entity entity : entities) {
            if (entity instanceof Player) {
                Player player = (Player) entity;

                if (player == null) {
                    sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"));
                    return true;
                }

                if (args.length == 2) {
                    switch (args[1].toLowerCase()) {
                        case "clear":
                            player.clearTitle();
                            sender.sendMessage(new TranslationContainer("nukkit.command.title.clear", player.getName()));
                            break;
                        case "reset":
                            player.resetTitleSettings();
                            sender.sendMessage(new TranslationContainer("nukkit.command.title.reset", player.getName()));
                            break;
                        default:
                            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                            return false;
                    }
                } else if (args.length == 3) {
                    switch (args[1].toLowerCase()) {
                        case "title":
                            player.sendTitle(args[2]);
                            sender.sendMessage(new TranslationContainer("nukkit.command.title.title",
                                    TextFormat.clean(args[2]), player.getName()));
                            break;
                        case "subtitle":
                            player.setSubtitle(args[2]);
                            sender.sendMessage(new TranslationContainer("nukkit.command.title.subtitle", TextFormat.clean(args[2]), player.getName()));
                            break;
                        default:
                            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                            return false;
                    }
                } else if (args.length == 5) {
                    if (args[1].toLowerCase().equals("times")) {
                        try {
                            sender.sendMessage(new TranslationContainer("nukkit.command.title.times.success",
                                    args[2], args[3], args[4], player.getName()));
                        } catch (NumberFormatException exception) {
                            sender.sendMessage(new TranslationContainer(TextFormat.RED + "%nukkit.command.title.times.fail"));
                        }
                    } else {
                        sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                        return false;
                    }
                } else {
                    sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                    return false;
                }
            }
        }

        return true;
    }
}
