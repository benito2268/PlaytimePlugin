package org.ben.plugin;
/**
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

import org.ben.plugin.command.PluginCommand;
import org.ben.plugin.command.PluginTabCompleter;
import org.ben.plugin.event.PPEvent;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.ben.plugin.io.*;

public class PP extends JavaPlugin{
    protected PluginCommand c = new PluginCommand();
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PPEvent(), this);
        getServer().getConsoleSender().sendMessage(
            ChatColor.GREEN + "[PP] yo the plugin is on");
        try {
            WriteFile.createFile();
            getServer().getConsoleSender().sendMessage("The dataFile loaded was... " + WriteFile.dataFile.toString());
            getServer().getConsoleSender().sendMessage(WriteFile.dataFile.getName());
        } catch(Exception e) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[PP] fatal error loading plugin pp");
        }
        getCommand("playtime").setExecutor(c);
        getCommand("playtime").setTabCompleter(new PluginTabCompleter());
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(
            ChatColor.RED + "[PP] yo the plugin is not on");
    }
}