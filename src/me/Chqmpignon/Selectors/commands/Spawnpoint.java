package me.Chqmpignon.Selectors.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.entity.Entity;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.TextFormat;
import me.Chqmpignon.Selectors.api.Target;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Spawnpoint extends VanillaCommand {

    public Spawnpoint(String name) {
        super(name, "%nukkit.command.spawnpoint.description", "%commands.spawnpoint.usage");
        this.setPermission("nukkit.command.spawnpoint");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("blockPos", CommandParamType.POSITION, true)});
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(new TranslationContainer("commands.generic.ingame"));
                    return true;
                }

                spawnpoint(args, sender, (Player) sender);
            } else {
                ArrayList<Entity> entities = Target.parse(args[0], sender);
                for(Entity entity : entities) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;

                        if (player == null) {
                            sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"));
                            return true;
                        }

                        spawnpoint(args, sender, player);
                    }
                }
            }

            return true;
        }
    }

    private boolean spawnpoint(String[] args, CommandSender sender, Player target) {
        Level level = target.getLevel();
        DecimalFormat round2 = new DecimalFormat("##0.00");
        if (args.length == 4) {
            if (level != null) {
                int x;
                int y;
                int z;
                try {
                    x = Integer.parseInt(args[1]);
                    y = Integer.parseInt(args[2]);
                    z = Integer.parseInt(args[3]);
                } catch (NumberFormatException var11) {
                    sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                    return true;
                }

                if (y < 0) {
                    y = 0;
                }

                if (y > 256) {
                    y = 256;
                }

                target.setSpawn(new Position((double)x, (double)y, (double)z, level));
                Command.broadcastCommandMessage(sender, new TranslationContainer("commands.spawnpoint.success", new String[]{target.getName(), round2.format((long)x), round2.format((long)y), round2.format((long)z)}));
                return true;
            }
        } else if (args.length <= 1) {
            if (sender instanceof Player) {
                Position pos = (Position)sender;
                target.setSpawn(pos);
                Command.broadcastCommandMessage(sender, new TranslationContainer("commands.spawnpoint.success", new String[]{target.getName(), round2.format(pos.x), round2.format(pos.y), round2.format(pos.z)}));
                return true;
            }

            sender.sendMessage(new TranslationContainer("commands.generic.ingame"));
            return true;
        }

        sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
        return true;
    }
}
