package me.Chqmpignon.Selectors.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;
import cn.nukkit.utils.TextFormat;
import me.Chqmpignon.Selectors.api.Target;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringJoiner;

public class Kill extends VanillaCommand {

    public Kill(String name) {
        super(name, "%nukkit.command.kill.description", "%nukkit.command.kill.usage", new String[]{"suicide"});
        this.setPermission("nukkit.command.kill.self;nukkit.command.kill.other");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, true)});
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length >= 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        } else if (args.length == 1) {
            if (!sender.hasPermission("nukkit.command.kill.other")) {
                sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                return true;
            } else {
                EntityDamageEvent ev;

                ArrayList<Entity> entities = Target.parse(args[0], sender);
                for(Entity entity : entities) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;

                        ev = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, 1000.0F);
                        sender.getServer().getPluginManager().callEvent(ev);
                        if (ev.isCancelled()) {
                            return true;
                        }

                        player.setLastDamageCause(ev);
                        player.setHealth(0.0F);
                        Command.broadcastCommandMessage(sender, new TranslationContainer("commands.kill.successful", player.getName()));
                    }else{
                        entity.close();
                        sender.sendMessage(new TranslationContainer("commands.kill.successful", entity.getName()));
                    }
                }
                return true;
            }
        } else if (sender instanceof Player) {
            if (!sender.hasPermission("nukkit.command.kill.self")) {
                sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                return true;
            } else {
                EntityDamageEvent ev = new EntityDamageEvent((Player)sender, EntityDamageEvent.DamageCause.SUICIDE, 1000.0F);
                sender.getServer().getPluginManager().callEvent(ev);
                if (ev.isCancelled()) {
                    return true;
                } else {
                    ((Player)sender).setLastDamageCause(ev);
                    ((Player)sender).setHealth(0.0F);
                    sender.sendMessage(new TranslationContainer("commands.kill.successful", sender.getName()));
                    return true;
                }
            }
        } else {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
    }
}
