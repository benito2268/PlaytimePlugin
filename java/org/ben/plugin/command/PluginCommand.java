package org.ben.plugin.command;

import org.bukkit.command.CommandExecutor;
import org.ben.plugin.event.PPEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ben.plugin.io.ParseFile;
import org.ben.plugin.drive.Backup;
import org.ben.plugin.io.PlayerTime;
import org.ben.plugin.io.WriteFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PluginCommand implements CommandExecutor {
    protected Random rand = new Random();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {return false;}
        Player p = (Player)sender;
        if(command.getName().equalsIgnoreCase("playtime")) {
            if(args.length == 1) {
                if(args[0].equals("~changelog")) {
                    p.sendMessage("PlaytimePlugin version 1.7");
                    p.sendMessage("Last Updated: Tuesday, June 13th at 11:01PM");
                    p.sendMessage("View the code here: https://github.com/benito2268/PlaytimePlugin");
                } else if(args[0].equals("~about")) {
                    p.sendMessage(ChatColor.WHITE + "Plugin Information");
                    p.sendMessage("version: 061323.17.19");
                    p.sendMessage("...for minecraft 1.20.1");
                    p.sendMessage("api: org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT");
                    p.sendMessage("compiler: Java HotSpot 17.0.3 (64-bit)");
                } else if (args[0].equals("~score")) {
                    printScoreBoard(p);
                } else if(args[0].equals("~realscore")) {
                    for(int i = 0; i < 10; i++) {
                        if(i == 0) {
                            p.sendMessage(ChatColor.GOLD + "[number one pp] " + ChatColor.BLUE + "L3gob3rt has played 999h999m" + (999 - i) + "s");
                        } else {
                            p.sendMessage(ChatColor.GREEN + "[" + (i+1) + "] " + ChatColor.BLUE + "L3gob3rt has played 999h999m" + (999 - i) + "s");
                        }
                    }
                } else if(args[0].equals("~backup")) {
                    //TODO remove maybe?
                    p.getServer().broadcastMessage(ChatColor.RED + "depricated (ㆆ _ ㆆ)");
                    //Backup.backup(p);
                } else if(args[0].equalsIgnoreCase("T & T time")) {
                    //do the funny
                    //play the sound
                    p.getServer().dispatchCommand(p.getServer().getConsoleSender(), "/give @" + p.getName() +  " minecraft:player_head{display:{Name:\"{\\\"text\\\":\\\"Windows Bomb\\\"}\"},SkullOwner:{Id:[I;1896598852,1583497632,-1412315946,636904964],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY0ODYzYmU0YmVlMzQ1M2U3MTZkOGRiMDhjNzk2ZGE4YzM4YTk3OWNmZDNhMjgwZTlkOGQ3YjY3ZjcwIn19fQ==\"}]}}} 1");
                } else {
                    //two arg player message
                    try {
                        String toRet = getTwoArgPlayerMessage(args, p);
                        if(toRet.equals("FAILED")) {
                            return false;
                        } else {
                            p.sendMessage(toRet);
                        }
                    } catch(Exception e) {
                        p.sendMessage(ChatColor.RED + "[pp error] this player's pp was too large to load. ;)");
                    }
                }
            } else if(args.length == 0){
                String toSend = getOneArgPlayerMessage(p);
                p.sendMessage(toSend);
                int i = rand.nextInt(100);
                if(Integer.parseInt(toSend.split(" ")[4].substring(0, 1)) < 1) {
                    if(i < 25) {p.sendMessage(ChatColor.BOLD + "[pp] pp too small, time to grind more Minecraft!");}
                } else if(Integer.parseInt(toSend.split(" ")[4].substring(0, 1)) > 10) {
                    if(i < 25) {p.sendMessage(ChatColor.BOLD + "[pp] pp too large, time to touch some grass!");}
                }
            } else if(args.length == 2) {
                //configure settings
                if(args[0].equalsIgnoreCase("spam")) {
                    if(args[1].equalsIgnoreCase("yes")) {
                        PPEvent.online.get(p.getName()).hasSpamEnabled = true;
                        p.sendMessage(ChatColor.GREEN + "preferences updated");
                    } else if(args[1].equalsIgnoreCase("no")) {
                        p.sendMessage(ChatColor.RED + "preferences updated");
                        PPEvent.online.get(p.getName()).hasSpamEnabled = false;
                    }
                } else if(args[0].equalsIgnoreCase("restrain")) {
                    if(args[1].equalsIgnoreCase("no")) {
                        p.sendMessage(ChatColor.GREEN + "preferences updated");
                        PPEvent.online.get(p.getName()).hasDmEnabled = true;
                    } else if(args[1].equalsIgnoreCase("yes")) {
                        p.sendMessage(ChatColor.RED + "preferences updated");
                        PPEvent.online.get(p.getName()).hasDmEnabled = false;
                    }
                } else if(args[0].trim().equalsIgnoreCase("show")) {
                    ArrayList<Player> players = new ArrayList<>(p.getServer().getOnlinePlayers());
                    long millis = 0;
                    for(Player pp : players) {
                        if(pp.getName().equals(args[1].trim())) {
                            try {
                                if(PPEvent.online.containsKey(args[1].trim())) {
                                    if(PPEvent.online.get(pp.getName()).hasDmEnabled) {
                                        PlayerTime pt = PPEvent.online.get(p.getName());
                                        millis = pt.getTotalTime();
                                        pp.sendMessage(ChatColor.BLUE + "[you have mail] " + ChatColor.GOLD + p.getName() + " wants you to know that they have " + getTimeString(millis) + " pp");
                                        //extra write to be safe
                                        saveAll();
                                    } else {
                                        p.sendMessage(ChatColor.RED + "[pp restraining order] " + args[1].trim() + " does not want to see your pp :(");
                                    }
                                } else {
                                    p.sendMessage(ChatColor.RED + "[pp error] we are terribly sorry but either that play doesn't exist or they don't want to see your pp right now");
                                }
                            } catch(Exception e) {
                                p.sendMessage(ChatColor.RED + "[pp error] FileInputStream read failed -> try again or yell at ben");
                                return false;
                            }
                            break;
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
            } else {
                p.sendMessage(ChatColor.RED + "[pp error] bad syntax -> try " + command.getUsage());
            }
        }
        return true;
    }

    /**
     *
     * @param args
     * @param p
     * @return
     */
    public String getTwoArgPlayerMessage(String[] args, Player p) {
        PlayerTime temp = new PlayerTime(args[0].trim());
        boolean canRead = false;
        try {
            canRead = ParseFile.existsInFile(WriteFile.dataFile, temp);
        } catch(Exception e) {
            p.sendMessage(ChatColor.RED + "[pp error] FileInputStream read failed -> try again or yell at ben");
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
                saveAll();
            } else {
                pt = ParseFile.getPlayerTimeInFile(WriteFile.dataFile, args[0].trim());
                playtime = pt.getTotalTimeInFile();
            }
        } catch(Exception e) {
            p.sendMessage(ChatColor.RED + "[pp error] FileInputStream read failed -> try again or yell at ben");
            return "FAILED";
        }
        String message = ChatColor.BLUE + "[pp] " + args[0].trim() + " has played " + getTimeString(playtime);
        return message;
    }

    /**
     *
     * @param p
     * @return
     */
    public String getOneArgPlayerMessage(Player p) {
        PlayerTime pt = PPEvent.online.get(p.getName().trim());
        //extra write to be safe
        try {
            WriteFile.updateEntry(pt);
        } catch(Exception e) {
            //¯\_(ツ)_/¯ guess now's not the time
        }
        String message = ChatColor.BLUE + "[pp] You have played " + getTimeString(pt.getTotalTime());
        return message;
    }

    /**
     *
     * @param initiator
     */
    public void printScoreBoard(Player initiator) {
        ArrayList<PlayerTime> list = new ArrayList<>();
        try {
            list = (ArrayList<PlayerTime>)ParseFile.getAllPlayers(WriteFile.dataFile);
        } catch(Exception e) {
            e.printStackTrace();
            return;
        }
        saveAll();
        list.sort(new Comparator<PlayerTime>() {
            @Override
            public int compare(PlayerTime p1, PlayerTime p2) {
                if(p1.getTotalTimeInFile() < p2.getTotalTimeInFile()) {
                    return 1;
                } else if(p1.getTotalTimeInFile() > p2.getTotalTimeInFile()) {
                    return -1;
                } else {return 0;}
            }
        });
        for(int i = 0; i < list.size() ; i++) {
            if(i == 0) {
                initiator.sendMessage(ChatColor.GOLD + "[number one pp] " + ChatColor.WHITE + list.get(i).getName() + " " + ChatColor.BLUE + getTimeString(list.get(i).getTotalTimeInFile()));
            } else {
                initiator.sendMessage(ChatColor.BLUE + "[" + (i+1) + "] " + ChatColor.WHITE + list.get(i).getName() + " " + ChatColor.BLUE + getTimeString(list.get(i).getTotalTimeInFile()));
            }
        }
    }

    /*
    * converts millis to a string of the form HH-MM-SS
    */
    public static String getTimeString(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        return hours + "h " + minutes + "m " + seconds + "s";
    }

    /**
     * saves time for all online players
     */
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
