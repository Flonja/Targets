package me.Chqmpignon.Selectors.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.entity.Entity;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import me.Chqmpignon.Selectors.api.Target;

import java.util.ArrayList;

public class Xp extends Command {

    public Xp(String name) {
        super(name, "%nukkit.command.xp.description", "%commands.xp.usage");
        this.setPermission("nukkit.command.xp");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("amount|level", CommandParamType.INT, false), new CommandParameter("player", CommandParamType.TARGET, true)});
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else {
            String amountString;
            String playerName;
            if (!(sender instanceof Player)) {
                if (args.length != 2) {
                    sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                    return true;
                }

                amountString = args[0];
                playerName = args[1];

                ArrayList<Entity> entities = Target.parse(playerName, sender);
                for (Entity entity : entities) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        xp(sender, amountString, player);
                    }
                }
            } else if (args.length == 1) {
                amountString = args[0];
                xp(sender, amountString, (Player) sender);
            } else {
                if (args.length != 2) {
                    sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                    return true;
                }

                amountString = args[0];
                playerName = args[1];

                ArrayList<Entity> entities = Target.parse(playerName, sender);
                for (Entity entity : entities) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        xp(sender, amountString, player);
                    }
                }
            }
        }
        return true;
    }

    private boolean xp(CommandSender sender, String amountString, Player player) {
        if (player == null) {
            sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"));
            return true;
        } else {
            boolean isLevel = false;
            if (amountString.endsWith("l") || amountString.endsWith("L")) {
                amountString = amountString.substring(0, amountString.length() - 1);
                isLevel = true;
            }

            int amount;
            try {
                amount = Integer.parseInt(amountString);
            } catch (NumberFormatException var10) {
                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }

            if (isLevel) {
                int newLevel = player.getExperienceLevel();
                newLevel += amount;
                if (newLevel > 24791) {
                    newLevel = 24791;
                }

                if (newLevel < 0) {
                    player.setExperience(0, 0);
                } else {
                    player.setExperience(player.getExperience(), newLevel);
                }

                if (amount > 0) {
                    sender.sendMessage(new TranslationContainer("commands.xp.success.levels", new String[]{String.valueOf(amount), player.getName()}));
                } else {
                    sender.sendMessage(new TranslationContainer("commands.xp.success.levels.minus", new String[]{String.valueOf(-amount), player.getName()}));
                }

                return true;
            } else if (amount < 0) {
                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            } else {
                player.addExperience(amount);
                sender.sendMessage(new TranslationContainer("commands.xp.success", new String[]{String.valueOf(amount), player.getName()}));
                return true;
            }
        }
    }
}