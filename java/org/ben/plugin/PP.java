package org.ben.plugin;

import org.ben.plugin.command.PluginCommand;
import org.ben.plugin.command.PluginTabCompleter;
import org.ben.plugin.event.PPEvent;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.ben.plugin.io.*;
import org.ben.plugin.drive.Backup;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import java.util.ArrayList;

public class PP extends JavaPlugin {
    protected PluginCommand c = new PluginCommand();
    protected PP PlugRef = this;

    private BukkitTask autosave;
    private BukkitTask backupWarn;
    private BukkitTask backup;
    private BukkitTask compress;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PPEvent(), this);
        getServer().getConsoleSender().sendMessage(
                ChatColor.GREEN + "[pp success] yo the plugin is on");
        try {
            WriteFile.createFile();
            WriteFile.createStevenFile();
            WriteFile.createSimonFile();
            WriteFile.createWyleFile();
            getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "[pp info] The dataFile loaded was... " + WriteFile.dataFile.toString());
        } catch(Exception e) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[pp error] fatal error loading plugin pp");
        }
        getCommand("playtime").setExecutor(c);
        getCommand("playtime").setTabCompleter(new PluginTabCompleter());

        //start the backup service
        autosave = new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<PlayerTime> onlinePlayers = new ArrayList<>(PPEvent.online.values());
                getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "[pp info] autosaving...");
                for(PlayerTime p : onlinePlayers) {
                    try {
                        WriteFile.updateEntry(p);
                        ConsoleCommandSender sender = getServer().getConsoleSender();
                        Bukkit.dispatchCommand(sender, "save-all");
                    } catch(Exception e) {
                        e.printStackTrace();
                        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[pp error] autosave failed for one or more players, will reattempt in 30 minutes");
                        break;
                    }

                    getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[pp success] autosave sucessful");
                }
            }
        }.runTaskTimer(this, 20L * 1800L, 20L * 1800L);

        backupWarn = new BukkitRunnable() {
            @Override
            public void run() {
                getServer().broadcastMessage(ChatColor.GOLD + "pp 5 minute warning");
                //getServer().broadcastMessage(ChatColor.GOLD + "all progress will be saved but it may freeze for 10-20 seconds during the upload");
            }
        }.runTaskTimer(this, 20L * 1500L, 20L * 86400L); //this, 20L * 1500L, 20L * 86400L

        compress = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(getServer().getConsoleSender(), "save-all");
                getServer().broadcastMessage(ChatColor.RED + "PP TIME!!!!");
                Bukkit.dispatchCommand(getServer().getConsoleSender(), "save-off");
                Backup.compress(PlugRef);
                //getServer().broadcastMessage(ChatColor.GOLD + "all progress will be saved but it may freeze for 10-20 seconds during the upload");
            }
        }.runTaskTimer(this, 20L * 1770L, 20L * 86400L);

        backup = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    //Bukkit.dispatchCommand(getServer().getConsoleSender(), "save-all");
                    //getServer().broadcastMessage(ChatColor.RED + "pp time");
                   //getServer().broadcastMessage(ChatColor.RED + "but the server may freeze for 10-20 seconds during the upload");

                    Backup.backup(PlugRef);
                } catch(Exception e) {
                    //getServer().broadcastMessage(ChatColor.RED + e.getClass().toString() + e.getMessage());
                }
            }
        }.runTaskTimerAsynchronously(this, 20L * 1800, 20L * 86400L); //this, 20L * 1800L, 20L * 86400L
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(
                ChatColor.RED + "[PP] yo the plugin is not on");
    }
}
