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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PluginCommand implements CommandExecutor {
    protected Random rand = new Random();

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
                    p.sendMessage(ChatColor.DARK_PURPLE + "processorinfo=" + System.getProperty("os-arch") + " " + Runtime.getRuntime().availableProcessors() + " processors");
                    p.sendMessage(ChatColor.DARK_PURPLE + "javaversion=" + System.getProperty("java.version"));
                    p.sendMessage(ChatColor.DARK_PURPLE + "runtime=" + System.getProperty("java.runtime.name") + " " + System.getProperty("java.runtime.version"));
                    p.sendMessage(ChatColor.DARK_PURPLE + "virutalmachine=" + System.getProperty("sun.management.compiler"));
                    p.sendMessage(ChatColor.DARK_PURPLE + "[DEBUG - PROGRAM]");
                    p.sendMessage(ChatColor.DARK_PURPLE + "you=" + p.getName());
                    p.sendMessage(ChatColor.DARK_PURPLE + "file=" + WriteFile.dataFile.toString());
                    p.sendMessage(ChatColor.DARK_PURPLE + "online=" + PPEvent.online.size());
                    p.sendMessage(ChatColor.DARK_PURPLE + "cmd=" + this.toString());
                } else if (args[0].equals("~score")) {
                    try {
                        ArrayList<String> toReturn = new ArrayList<>();
                        saveAll();
                        for(String n : ParseFile.getNamesInFile(WriteFile.dataFile)) {
                            String toRet = getTwoArgPlayerMessage(new String[]{n}, p);
                            if(toRet.equals("FAILED")) {
                                return false;
                            } else {
                                toReturn.add(toRet);
                            }
                        }
                        toReturn.sort(new Comparator<String>() {
                            @Override
                            public int compare(String s1, String s2) {
                                String[] s1Arr = s1.split(" ");
                                String[] s2Arr = s2.split(" ");
                                String arg1 = s1Arr[4]+s1Arr[5]+s1Arr[6].trim();
                                String arg2 = s2Arr[4]+s2Arr[5]+s2Arr[6].trim();
                                if(arg1.compareTo(arg2) > 0) {
                                    return -1;
                                } else if(arg1.compareTo(arg2) < 0) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                        });
                        for(int i = 0; i < toReturn.size(); i++) {
                            if(i == 0) {
                                p.sendMessage(ChatColor.GOLD + "[NUMBER ONE PP] " + toReturn.get(i));
                            } else {
                                p.sendMessage(ChatColor.GREEN + "[" + (i+1) + "] " + toReturn.get(i));
                            }
                        }
                    } catch(Exception e) {
                        p.sendMessage("[pp error] couldn't this remaining data from file -> try again");
                    }
                } else if(args[0].equals("~realscore")) {
                    for(int i = 0; i < 10; i++) {
                        if(i == 0) {
                            p.sendMessage(ChatColor.GOLD + "[NUMBER ONE PP] " + ChatColor.BLUE + "L3gob3rt has played 999h999m" + (999 - i) + "s");
                        } else {
                            p.sendMessage(ChatColor.GREEN + "[" + (i+1) + "] " + ChatColor.BLUE + "L3gob3rt has played 999h999m" + (999 - i) + "s");
                        }
                    }
                } else {    
                   String toRet = getTwoArgPlayerMessage(args, p);
                   if(toRet.equals("FAILED")) {
                       return false;
                   } else {
                       p.sendMessage(toRet);
                   }
                }  
            } else if(args.length == 0){
                String toSend = getOneArgPlayerMessage(p);
                p.sendMessage(toSend);
                int i = rand.nextInt(100);
                if(Integer.parseInt(toSend.split(" ")[4].substring(0, 1)) < 1) {
                    if(i < 25) {p.sendMessage(ChatColor.BOLD + "[pp] pp too small, time to grind more Minecraft!");}
                } else if(Integer.parseInt(toSend.split(" ")[4].substring(0, 1)) > 10) {
                    if(i < 25) {p.sendMessage(ChatColor.BOLD + "[PP] pp too large, time to touch some grass!");}
                }
            } else {
                p.sendMessage(ChatColor.RED + "[pp error] bad syntax -> try " + command.getUsage());
            }
        }
        return true;
    }

    public String getTwoArgPlayerMessage(String[] args, Player p) {
        PlayerTime temp = new PlayerTime(args[0].trim());
        boolean canRead = false;
        try {
            canRead = ParseFile.existsInFile(WriteFile.dataFile, temp);
        } catch(Exception e) {
            p.sendMessage(ChatColor.RED + "[pp error] FileInputStream read failed -> try again or yell at L3gob3rt");
            return "FAILED";
        }
        if(!canRead) {
            p.sendMessage(ChatColor.RED + "[pp error] player " + args[0] + " was not found in the server's history");
            p.sendMessage(ChatColor.RED + "playtime can only be logged from the point the plugin was installed and forward (have they logged in since then?)");
            return "FAILED";
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
            p.sendMessage(ChatColor.RED + "[pp error] FileInputStream read failed -> try again or yell at L3gob3rt");
            return "FAILED";
        }
        final long hours = TimeUnit.MILLISECONDS.toHours(playtime);
        playtime -= TimeUnit.HOURS.toMillis(hours);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(playtime);
        playtime -= TimeUnit.MINUTES.toMillis(minutes);
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(playtime);
        String message = ChatColor.BLUE + "[PP] " + args[0].trim() + " has played " + hours + "h " + minutes + "m " + seconds + "s";
        return message;
    }

    public String getOneArgPlayerMessage(Player p) {
        PlayerTime pt = PPEvent.online.get(p.getName().trim());
        long playtime = pt.getTotalTime();
        final long hours = TimeUnit.MILLISECONDS.toHours(playtime);
        playtime -= TimeUnit.HOURS.toMillis(hours);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(playtime);
        playtime -= TimeUnit.MINUTES.toMillis(minutes);
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(playtime);;
        //extra write to be safe
        try {
            WriteFile.updateEntry(pt);
        } catch(Exception e) {
            //¯\_(ツ)_/¯ guess now's not the time
        }
        String message = ChatColor.BLUE + "[PP] You have played " + hours + "h " + minutes + "m " + seconds + "s";
        return message;
    }

    public static void saveAll() {
        ArrayList<PlayerTime> onlinePlayers = new ArrayList<>(PPEvent.online.values());
        for(PlayerTime p : onlinePlayers) {
            try {
                WriteFile.updateEntry(p);
            } catch(Exception e) {
                break;
            }
            }
    }
}
