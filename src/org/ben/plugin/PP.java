/**
 * COPYRIGHT DISCLAIMER:
 * 
 * This file is part of PlaytimePlugin.
 * 
 * PlaytimePlugin is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 * PlaytimePlugin is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with PlaytimePlugin. If not, see <https://www.gnu.org/licenses/>.
 *
 * @author Ben Staehle
 * @date 5/15/22
 */

package org.ben.plugin;

import org.ben.plugin.command.PluginCommand;
import org.ben.plugin.command.PluginTabCompleter;
import org.ben.plugin.event.PPEvent;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.ben.plugin.io.*;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;

public class PP extends JavaPlugin{
    protected PluginCommand c = new PluginCommand();
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PPEvent(), this);
        getServer().getConsoleSender().sendMessage(
            ChatColor.GREEN + "[pp success] yo the plugin is on");
        try {
            WriteFile.createFile();
            getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "[pp info] The dataFile loaded was... " + WriteFile.dataFile.toString());
            getServer().getConsoleSender().sendMessage(WriteFile.dataFile.getName());
        } catch(Exception e) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[pp error] fatal error loading plugin pp");
        }
        getCommand("playtime").setExecutor(c);
        getCommand("playtime").setTabCompleter(new PluginTabCompleter());

        ScheduledExecutorService autosave = Executors.newSingleThreadScheduledExecutor();
        autosave.scheduleAtFixedRate(() -> {
            ArrayList<PlayerTime> onlinePlayers = new ArrayList<>(PPEvent.online.values());
            getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "[pp info] autosaving...");
            for(PlayerTime p : onlinePlayers) {
                try {
                    WriteFile.updateEntry(p);
                } catch(Exception e) {
                    getServer().getConsoleSender().sendMessage(ChatColor.RED + "[pp error] autosave failed for one or more players, will reattempt in 30 minutes");
                    break;
                }
            }
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[pp success] autosave sucessful");
        }, 300, 1800, TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(
            ChatColor.RED + "[PP] yo the plugin is not on");
    }
}
