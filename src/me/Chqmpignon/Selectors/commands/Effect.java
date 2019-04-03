package me.Chqmpignon.Selectors.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.entity.Entity;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.potion.InstantEffect;
import cn.nukkit.utils.ServerException;
import cn.nukkit.utils.TextFormat;
import me.Chqmpignon.Selectors.api.Target;

import java.util.ArrayList;
import java.util.Iterator;

public class Effect extends Command {

    public Effect(String name) {
        super(name, "%nukkit.command.effect.description", "%commands.effect.usage");
        this.setPermission("nukkit.command.effect");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("effect", CommandParamType.STRING, false), new CommandParameter("seconds", CommandParamType.INT, true), new CommandParameter("amplifier", true), new CommandParameter("hideParticle", true, new String[]{"true", "false"})});
        this.commandParameters.put("clear", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("clear", new String[]{"clear"})});
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length < 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        } else {
            ArrayList<Entity> entities = Target.parse(args[0], sender);
            for(Entity entity : entities) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;

                    if (player == null) {
                        sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"));
                        return true;
                    } else if (!args[1].equalsIgnoreCase("clear")) {
                        cn.nukkit.potion.Effect effect;
                        try {
                            effect = cn.nukkit.potion.Effect.getEffect(Integer.parseInt(args[1]));
                        } catch (ServerException | NumberFormatException var12) {
                            try {
                                effect = cn.nukkit.potion.Effect.getEffectByName(args[1]);
                            } catch (Exception var11) {
                                sender.sendMessage(new TranslationContainer("commands.effect.notFound", args[1]));
                                return true;
                            }
                        }

                        int duration = 300;
                        int amplification = 0;
                        if (args.length >= 3) {
                            try {
                                duration = Integer.valueOf(args[2]);
                            } catch (NumberFormatException var10) {
                                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                                return true;
                            }

                            if (!(effect instanceof InstantEffect)) {
                                duration *= 20;
                            }
                        } else if (effect instanceof InstantEffect) {
                            duration = 1;
                        }

                        if (args.length >= 4) {
                            try {
                                amplification = Integer.valueOf(args[3]);
                            } catch (NumberFormatException var9) {
                                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                                return true;
                            }
                        }

                        if (args.length >= 5) {
                            String v = args[4].toLowerCase();
                            if (v.matches("(?i)|on|true|t|1")) {
                                effect.setVisible(false);
                            }
                        }

                        if (duration == 0) {
                            if (!player.hasEffect(effect.getId())) {
                                if (player.getEffects().size() == 0) {
                                    sender.sendMessage(new TranslationContainer("commands.effect.failure.notActive.all", player.getDisplayName()));
                                } else {
                                    sender.sendMessage(new TranslationContainer("commands.effect.failure.notActive", new String[]{effect.getName(), player.getDisplayName()}));
                                }

                                return true;
                            }

                            player.removeEffect(effect.getId());
                            sender.sendMessage(new TranslationContainer("commands.effect.success.removed", new String[]{effect.getName(), player.getDisplayName()}));
                        } else {
                            effect.setDuration(duration).setAmplifier(amplification);
                            player.addEffect(effect);
                            Command.broadcastCommandMessage(sender, new TranslationContainer("%commands.effect.success", new String[]{effect.getName(), String.valueOf(effect.getAmplifier()), player.getDisplayName(), String.valueOf(effect.getDuration() / 20)}));
                        }
                    } else {
                        Iterator var5 = player.getEffects().values().iterator();

                        while (var5.hasNext()) {
                            cn.nukkit.potion.Effect effect = (cn.nukkit.potion.Effect) var5.next();
                            player.removeEffect(effect.getId());
                        }

                        sender.sendMessage(new TranslationContainer("commands.effect.success.removed.all", player.getDisplayName()));
                        return true;
                    }
                }
            }
            return true;
        }
    }
}
