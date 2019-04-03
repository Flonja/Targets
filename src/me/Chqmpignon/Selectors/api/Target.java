package me.Chqmpignon.Selectors.api;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Target {

    public static String ALL_PLAYERS = "@a";
    public static String ALL_ENTITIES = "@e";
    public static String CLOSEST_PLAYER = "@p";
    public static String RANDOM_PLAYER = "@r";
    public static String YOURSELF = "@s";

    private static Object getRandomElement(List list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    public static ArrayList<Entity> parse(String selector, CommandSender sender) {
        ArrayList<Entity> entities = new ArrayList<>();
        if(selector.startsWith("@")) {
            if(selector.startsWith(ALL_PLAYERS)) {
                for(Player player : Server.getInstance().getOnlinePlayers().values()) {
                    entities.add(player);
                }
            }else if(selector.startsWith(ALL_ENTITIES)) {
                for(Level level : Server.getInstance().getLevels().values()) {
                    for(Entity entity : level.getEntities()) {
                        entities.add(entity);
                    }
                }
            }else if(selector.startsWith(CLOSEST_PLAYER)) {
                if(sender.isPlayer()) {
                    Player me = (Player) sender;

                    entities.add(me.getLevel().getNearbyEntities(me.getBoundingBox())[0]);
                }
            }else if(selector.startsWith(RANDOM_PLAYER)) {
                ArrayList<Player> all = new ArrayList<>();
                for(Player player : Server.getInstance().getOnlinePlayers().values()) {
                    all.add(player);
                }

                entities.add((Player) getRandomElement(all));
            }else if(selector.startsWith(YOURSELF)) {
                if(sender.isPlayer()) {
                    entities.add((Player) sender);
                }
            }
        }else{
            Player player = Server.getInstance().getPlayer(selector);
            if(player != null) {
                entities.add(player);
            }
        }

        return entities;
    }
}