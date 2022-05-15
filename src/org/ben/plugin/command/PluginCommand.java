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

package org.ben.plugin.command;
import org.ben.plugin.event.PPEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ben.plugin.io.ParseFile;
import org.ben.plugin.io.PlayerTime;
import org.ben.plugin.io.WriteFile;
import org.bukkit.ChatColor;

public class PluginCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {return true;}
        Player p = (Player)sender;
        if(command.getName().equalsIgnoreCase("playtime")) {
            if(args.length == 1) {
                if(args[0].equals("~info")) {
                    p.sendMessage(ChatColor.GREEN + "[INFO]");
                    p.sendMessage(ChatColor.GREEN + "Name: PlaytimePlugin version 1.0");
                    p.sendMessage(ChatColor.GREEN + "Compatible With: Minecraft 1.18.0 and above");
                    p.sendMessage(ChatColor.GREEN + "Last Updated: Sunday, May 15, 2022 at 1:46PM");
                    p.sendMessage(ChatColor.GREEN + "View the code here: https://github.com/benito2268/PlaytimePlugin");
                } else if(args[0].equals("~debug")) {
                    p.sendMessage(ChatColor.DARK_PURPLE + "[DEBUG - SYSTEM INFO]");
                    p.sendMessage(ChatColor.DARK_PURPLE + "osname=" + System.getProperty("os-name"));
                    p.sendMessage(ChatColor.DARK_PURPLE+ "javaversion=" + System.getProperty("java.version"));
                    p.sendMessage(ChatColor.DARK_PURPLE + "runtime=" + System.getProperty("java.runtime.name") + " " + System.getProperty("java.runtime.version"));
                    p.sendMessage(ChatColor.DARK_PURPLE + "virutalmachine=" + System.getProperty("sun.management.compiler"));
                    p.sendMessage(ChatColor.DARK_PURPLE + "[DEBUG - PROGRAM]");
                    p.sendMessage(ChatColor.DARK_PURPLE + "you=" + p.getName());
                    p.sendMessage(ChatColor.DARK_PURPLE + "file=" + WriteFile.dataFile.toString());
                    p.sendMessage(ChatColor.DARK_PURPLE + "online=" + PPEvent.online.size());
                    p.sendMessage(ChatColor.DARK_PURPLE + "cmd=" + this.toString());
                } else {
                    PlayerTime temp = new PlayerTime(args[0].trim());
                boolean canRead = false;
                try {
                    canRead = ParseFile.existsInFile(WriteFile.dataFile, temp);
                } catch(Exception e) {
                    p.sendMessage(ChatColor.RED + "[PP] pp error: FileInputStream read failed -> try again or yell at L3gob3rt");
                    return false;
                }
                if(!canRead) {
                    p.sendMessage(ChatColor.RED + "[PP] pp error: player " + args[0] + " was not found in the server's history");
                    p.sendMessage(ChatColor.RED + "playtime can only be logged from the point the plugin was installed and forward (have they logged in since then?)");
                    return false;
                }
                PlayerTime pt = new PlayerTime("NOT_INITIALIZED");
                long playtime = 0;
                try {
                    if(PPEvent.online.containsKey(args[0].trim())) {
                        pt = PPEvent.online.get(args[0].trim());
                        playtime = pt.getTotalTime();
                        //extra write to be safe
                        try {
                            WriteFile.updateEntry(pt);
                        } catch(Exception e) {
                            //¯\_(ツ)_/¯ guess now's not the time
                        }
                    } else {
                        pt = ParseFile.getPlayerTimeInFile(WriteFile.dataFile, args[0].trim());
                        playtime = pt.getTotalTimeInFile();
                    }
                } catch(Exception e) {
                    p.sendMessage(ChatColor.RED + "[PP] pp error: FileInputStream read failed -> try again or yell at L3gob3rt");
                    return false;
                }
                final long seconds = (playtime / 1000) % 60;
                final long minutes = ((playtime / (1000*60)) % 60);
                final long hours = ((playtime / (1000*60*60)) % 24);
                String message = ChatColor.BLUE + "[PP] " + args[0].trim() + " has played " + hours + "h " + minutes + "m " + seconds + "s";
                p.sendMessage(message);
                }
                
                
            } else if(args.length == 0){
                PlayerTime pt = PPEvent.online.get(p.getName().trim());
                long playtime = pt.getTotalTime();
                final long seconds = (playtime / 1000) % 60;
                final long minutes = ((playtime / (1000*60)) % 60);
                final long hours = ((playtime / (1000*60*60)) % 24);
                //extra write to be safe
                try {
                    WriteFile.updateEntry(pt);
                } catch(Exception e) {
                    //¯\_(ツ)_/¯ guess now's not the time
                }
                String message = ChatColor.BLUE + "[PP] You have played " + hours + "h " + minutes + "m " + seconds + "s";
                p.sendMessage(message);
            } else {
                p.sendMessage(ChatColor.RED + "[PP] pp error: bad syntax -> try " + command.getUsage());
            }
        }
        return true;
    }
}
