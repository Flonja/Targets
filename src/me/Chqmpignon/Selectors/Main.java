package me.Chqmpignon.Selectors;

import cn.nukkit.command.Command;
import cn.nukkit.command.defaults.*;
import cn.nukkit.plugin.PluginBase;

import cn.nukkit.utils.Config;
import me.Chqmpignon.Selectors.commands.*;

import java.util.Iterator;

public class Main extends PluginBase {

    private Config c;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        c = getConfig();

        Iterator var1 = this.getServer().getCommandMap().getCommands().values().iterator();

        while(var1.hasNext()) {
            Command command = (Command)var1.next();
            command.unregister(this.getServer().getCommandMap());
        }

        this.getServer().getCommandMap().getCommands().values().clear();

        registerCustomCommands();
        //registerCommands();
    }

    private void register(String fallback, Command command) {
        this.getServer().getCommandMap().register(fallback, command);
    }

    private void registerCommands() {
        this.register("nukkit", new VersionCommand("version"));
        this.register("nukkit", new PluginsCommand("plugins"));
        this.register("nukkit", new SeedCommand("seed"));
        this.register("nukkit", new HelpCommand("help"));
        this.register("nukkit", new StopCommand("stop"));
        this.register("nukkit", new TellCommand("tell"));
        this.register("nukkit", new DefaultGamemodeCommand("defaultgamemode"));
        this.register("nukkit", new BanCommand("ban"));
        this.register("nukkit", new BanIpCommand("ban-ip"));
        this.register("nukkit", new BanListCommand("banlist"));
        this.register("nukkit", new PardonCommand("pardon"));
        this.register("nukkit", new PardonIpCommand("pardon-ip"));
        this.register("nukkit", new SayCommand("say"));
        this.register("nukkit", new MeCommand("me"));
        this.register("nukkit", new ListCommand("list"));
        this.register("nukkit", new DifficultyCommand("difficulty"));
        this.register("nukkit", new KickCommand("kick"));
        this.register("nukkit", new OpCommand("op"));
        this.register("nukkit", new DeopCommand("deop"));
        this.register("nukkit", new WhitelistCommand("whitelist"));
        this.register("nukkit", new SaveOnCommand("save-on"));
        this.register("nukkit", new SaveOffCommand("save-off"));
        this.register("nukkit", new SaveCommand("save-all"));
        this.register("nukkit", new GiveCommand("give"));
        this.register("nukkit", new EffectCommand("effect"));
        this.register("nukkit", new EnchantCommand("enchant"));
        this.register("nukkit", new ParticleCommand("particle"));
        this.register("nukkit", new GamemodeCommand("gamemode"));
        this.register("nukkit", new GameruleCommand("gamerule"));
        this.register("nukkit", new KillCommand("kill"));
        this.register("nukkit", new SpawnpointCommand("spawnpoint"));
        this.register("nukkit", new SetWorldSpawnCommand("setworldspawn"));
        this.register("nukkit", new TeleportCommand("tp"));
        this.register("nukkit", new TimeCommand("time"));
        this.register("nukkit", new TitleCommand("title"));
        this.register("nukkit", new ReloadCommand("reload"));
        this.register("nukkit", new WeatherCommand("weather"));
        this.register("nukkit", new XpCommand("xp"));
        this.register("nukkit", new StatusCommand("status"));
        this.register("nukkit", new GarbageCollectorCommand("gc"));
        this.register("nukkit", new TimingsCommand("timings"));
        this.register("nukkit", new DebugPasteCommand("debugpaste"));
    }

    private void registerCustomCommands() {
        if (c.get("commands.tell", true)) {
            this.register("nukkit", new Tell("tell"));                               // CUSTOM
        }else{
            this.register("nukkit", new TellCommand("tell"));
        }

        if (c.get("commands.ban", false)) {
            this.register("nukkit", new Ban("ban"));                                 // CUSTOM
        }else{
            this.register("nukkit", new BanCommand("ban"));
        }

        if (c.get("commands.say", true)) {
            this.register("nukkit", new Say("say"));                                 // CUSTOM
        }else{
            this.register("nukkit", new SayCommand("say"));
        }

        if (c.get("commands.me", true)) {
            this.register("nukkit", new Me("me"));                                   // CUSTOM
        }else{
            this.register("nukkit", new MeCommand("me"));
        }

        if (c.get("commands.kick", true)) {
            this.register("nukkit", new Kick("kick"));                               // CUSTOM
        }else{
            this.register("nukkit", new KickCommand("kick"));
        }

        if (c.get("commands.op", false)) {
            this.register("nukkit", new Op("op"));                                   // CUSTOM
        }else{
            this.register("nukkit", new OpCommand("op"));
        }

        if (c.get("commands.give", true)) {
            this.register("nukkit", new Give("give"));                               // CUSTOM
        }else{
            this.register("nukkit", new GiveCommand("give"));
        }

        if (c.get("commands.effect", true)) {
            this.register("nukkit", new Effect("effect"));                           // CUSTOM
        }else{
            this.register("nukkit", new EffectCommand("effect"));
        }

        if (c.get("commands.enchant", true)) {
            this.register("nukkit", new Enchant("enchant"));                         // CUSTOM
        }else{
            this.register("nukkit", new EnchantCommand("enchant"));
        }

        if (c.get("commands.gamemode", true)) {
            this.register("nukkit", new Gamemode("gamemode"));                       // CUSTOM
        }else{
            this.register("nukkit", new GamemodeCommand("gamemode"));
        }

        if(c.get("commands.kill", true)) {
            this.register("nukkit", new Kill("kill"));                               // CUSTOM
        }else{
            this.register("nukkit", new KillCommand("kill"));
        }

        if(c.get("commands.spawnpoint", true)) {
            this.register("nukkit", new Spawnpoint("spawnpoint"));                   // CUSTOM
        }else{
            this.register("nukkit", new SpawnpointCommand("spawnpoint"));
        }

        if(c.get("commands.teleport", true)) {
            this.register("nukkit", new Teleport("tp"));                             // CUSTOM
        }else{
            this.register("nukkit", new TeleportCommand("tp"));
        }

        if(c.get("commands.title", true)) {
            this.register("nukkit", new Title("title"));                             // CUSTOM
        }else{
            this.register("nukkit", new TitleCommand("title"));
        }

        if(c.get("commands.xp", true)) {
            this.register("nukkit", new Xp("xp"));                                   // CUSTOM
        }else{
            this.register("nukkit", new XpCommand("xp"));
        }

        this.register("nukkit", new DefaultGamemodeCommand("defaultgamemode"));
        this.register("nukkit", new VersionCommand("version"));
        this.register("nukkit", new PluginsCommand("plugins"));
        this.register("nukkit", new SeedCommand("seed"));
        this.register("nukkit", new HelpCommand("help"));
        this.register("nukkit", new StopCommand("stop"));
        this.register("nukkit", new BanIpCommand("ban-ip"));
        this.register("nukkit", new BanListCommand("banlist"));
        this.register("nukkit", new PardonCommand("pardon"));
        this.register("nukkit", new PardonIpCommand("pardon-ip"));
        this.register("nukkit", new ListCommand("list"));
        this.register("nukkit", new DifficultyCommand("difficulty"));
        this.register("nukkit", new DeopCommand("deop"));
        this.register("nukkit", new WhitelistCommand("whitelist"));
        this.register("nukkit", new SaveOnCommand("save-on"));
        this.register("nukkit", new SaveOffCommand("save-off"));
        this.register("nukkit", new SaveCommand("save-all"));
        this.register("nukkit", new ParticleCommand("particle"));
        this.register("nukkit", new GameruleCommand("gamerule"));
        this.register("nukkit", new SetWorldSpawnCommand("setworldspawn"));
        this.register("nukkit", new TimeCommand("time"));
        this.register("nukkit", new ReloadCommand("reload"));
        this.register("nukkit", new WeatherCommand("weather"));
        this.register("nukkit", new StatusCommand("status"));
        this.register("nukkit", new GarbageCollectorCommand("gc"));
        this.register("nukkit", new TimingsCommand("timings"));
        this.register("nukkit", new DebugPasteCommand("debugpaste"));
    }
}
