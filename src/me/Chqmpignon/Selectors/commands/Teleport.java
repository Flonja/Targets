package me.Chqmpignon.Selectors.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Location;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.utils.TextFormat;
import me.Chqmpignon.Selectors.api.Target;

import java.util.ArrayList;

public class Teleport extends VanillaCommand {

    public Teleport(String name) {
        super(name, "%nukkit.command.tp.description", "%commands.tp.usage");
        this.setPermission("nukkit.command.teleport");
        this.commandParameters.clear();
        this.commandParameters.put("->Player", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
        this.commandParameters.put("Player->Player", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("target", CommandParamType.TARGET, false)});
        this.commandParameters.put("Player->Pos", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("blockPos", CommandParamType.POSITION, false)});
        this.commandParameters.put("->Pos", new CommandParameter[]{new CommandParameter("blockPos", CommandParamType.POSITION, false)});
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        ArrayList<Entity> origins = new ArrayList<>();

        if (!(sender instanceof Player)) {
            sender.sendMessage(new TranslationContainer("commands.generic.ingame"));
            return true;
        }

        if (args.length >= 1 && args.length <= 6) {
            ArrayList<Entity> entities = Target.parse(args[0], sender);
            if(entities.size() != 0) {
                for (Entity entity : entities) {
                    Entity player = entity;

                    if (args.length != 1 && args.length != 3) {
                        if (player == null) {
                            sender.sendMessage(TextFormat.RED + "Can't find player " + args[0]);
                            return true;
                        }

                        if (args.length == 2) {
                            if (Target.parse(args[1], sender) == null) {
                                sender.sendMessage(TextFormat.RED + "Can't find player " + args[1]);
                                return true;
                            } else {
                                origins.addAll(Target.parse(args[1], sender));
                            }
                        }
                    } else {
                        if (args.length == 1) {
                            origins.addAll(Target.parse(args[1], sender));
                            if (Target.parse(args[1], sender) == null) {
                                sender.sendMessage(TextFormat.RED + "Can't find player " + args[0]);
                                return true;
                            }
                        }
                    }

                    if (args.length < 3) {
                        for (Entity origin : origins) {
                            player.teleport(origin, PlayerTeleportEvent.TeleportCause.COMMAND);
                            Command.broadcastCommandMessage(sender, new TranslationContainer("commands.tp.success", new String[]{player.getName(), origin.getName()}));
                        }
                        return true;
                    } else if (player.getLevel() == null) {
                        sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                        return true;
                    } else {
                        teleport(args, sender, player);
                    }
                }
            }else{
                teleport(args, sender, (Player) sender);
            }
        } else {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        return true;
    }

    private boolean teleport(String[] args, CommandSender sender, Entity player) {
        int pos;
        if (args.length != 4 && args.length != 6) {
            pos = 0;
        } else {
            pos = 1;
        }

        double x;
        double y;
        double z;
        double yaw;
        double pitch;
        try {
            pos = pos + 1;
            x = Double.parseDouble(args[pos].replace("~", "" + player.x));
            y = Double.parseDouble(args[pos++].replace("~", "" + player.y));
            z = Double.parseDouble(args[pos++].replace("~", "" + player.z));
            yaw = player.getYaw();
            pitch = player.getPitch();
        } catch (NumberFormatException var18) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }

        if (y < 0.0D) {
            y = 0.0D;
        }

        if (y > 256.0D) {
            y = 256.0D;
        }

        if (args.length == 6 || args.length == 5 && pos == 3) {
            yaw = (double) Integer.parseInt(args[pos++]);
            pitch = (double) Integer.parseInt(args[pos++]);
        }

        player.teleport(new Location(x, y, z, yaw, pitch, player.getLevel()), PlayerTeleportEvent.TeleportCause.COMMAND);
        Command.broadcastCommandMessage(sender, new TranslationContainer("commands.tp.success.coordinates", new String[]{player.getName(), String.valueOf(NukkitMath.round(x, 2)), String.valueOf(NukkitMath.round(y, 2)), String.valueOf(NukkitMath.round(z, 2))}));
        return true;
    }
}
