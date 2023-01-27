/**
 * a note:
 * the pp plugin is free software and comes with no warranty whatsoever. My claims of functionality
 * are purely a figment of your imagination. All rights belong to their respective owners.
 * 
 * Ha. there now you can't sue me when it doesn't work :)
 *
 * @author Ben Staehle
 * @date 8/13/22
 */

package org.ben.plugin.event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.ben.plugin.io.ParseFile;
import org.ben.plugin.io.PlayerTime;
import org.ben.plugin.io.WriteFile;
import java.util.Hashtable;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.player.PlayerShearEntityEvent;
import java.util.Arrays;

public class PPEvent implements Listener {
    public static Hashtable<String, PlayerTime> online = new Hashtable<>();

    @EventHandler
    public static synchronized void onPlayerJoin(PlayerJoinEvent p) throws Exception{
        Player joinTarget = p.getPlayer();
        try {
            PlayerTime pt = new PlayerTime(joinTarget.getName(), joinTarget.getUniqueId().toString(), WriteFile.dataFile);
            if(!ParseFile.existsInFile(WriteFile.dataFile, pt)) {
                WriteFile.newEntry(pt);
            }
            pt.login();
            PPEvent.online.put(pt.getName().trim(), pt);
        } catch(Exception e) {
            //ouf
        }
    }

    @EventHandler
    public static synchronized void onPlayerQuit(PlayerQuitEvent p) throws Exception {
        Player player = p.getPlayer();
        PlayerTime pt = online.get(player.getName());
        WriteFile.updateEntry(pt);
        online.remove(player.getName());
    }

    /**
     * yikes
     */
    @EventHandler
    public static void onStevenOrSimonVariousNoScopeEvent(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
            Arrow a = (Arrow)e.getDamager();
            if(a.getShooter() instanceof Skeleton) {
                Player steven = (Player)e.getEntity();
                if(steven.getName().equalsIgnoreCase("Steven1468")) {
                    try{
                        int i = ParseFile.readStevenFile(WriteFile.stevenFile) + 1;
                        if(i % 100 == 0 && i != 0 && i != 1000) {
                            e.getEntity().getServer().broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "[special pp] Congratulations Steven! you have been hit by your " + i + "th skeleton");
                            e.getEntity().getServer().broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "To commemorate this momentous occasion you get to have the arrow!");
                            ItemStack item = new ItemStack(Material.ARROW);
                            ItemMeta arrowMeta = item.getItemMeta();
                            arrowMeta.setDisplayName(ChatColor.GOLD + ChatColor.MAGIC.toString() + "oljsl" + ChatColor.RESET + i + ChatColor.GOLD.toString() + ChatColor.MAGIC.toString() + "dkfjs");
                            item.setItemMeta(arrowMeta);
                            steven.getInventory().addItem(item);
                        } else if(i == 1000) {
                            e.getEntity().getServer().broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "[special pp] Congratulations Steven! you have been hit by your " + i + "th skeleton");
                            e.getEntity().getServer().broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "I know you've been working hard for all those arrows so here's an extra special gift!");
                            ItemStack itemB = new ItemStack(Material.BOW);
                            ItemMeta bowMeta = itemB.getItemMeta();
                            bowMeta.setDisplayName("mesothelioma");
                            bowMeta.setUnbreakable(true);
                            bowMeta.setLore(Arrays.asList(ChatColor.LIGHT_PURPLE + "If you or a loved one was","diagnosed with Mesothelioma ","you may be entitled to","financial compensation.", "Mesothelioma is a rare cancer","linked to asbestos exposure. ","Exposure to asbestos in the Navy,","shipyards, mills, heating, ","construction or the automotive","industries may put you at risk.","Please don't wait, call","1-800-99 LAW USA today ","for a free legal consultation and","financial information packet.","Mesothelioma patients","call now! 1-800-99 LAW USA"));
                            itemB.setItemMeta(bowMeta);
                            steven.getInventory().addItem(itemB);
                        } else {
                            e.getEntity().getServer().broadcastMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "oh no! Steven has been hit by a skeleton!");
                            e.getEntity().getServer().broadcastMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "Congratulate him at " + (int)e.getEntity().getLocation().getX() + ", " + (int)e.getEntity().getLocation().getY() + ", " + (int)e.getEntity().getLocation().getZ());
                            e.getEntity().getServer().broadcastMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "this brings the total to " + String.valueOf(i) + " spooky scary skeletons!");
                        }
                        WriteFile.stevenEntry(i);
                    } catch(Exception ex) {
                        e.getEntity().getServer().broadcastMessage(ChatColor.RED + "uh oh! Steven got hit by a skeleton, and broke the plugin rip");
                    }
                }
            }
        } else if (e.getEntity() instanceof Player && e.getDamager() instanceof Guardian) {
            Player simon = (Player)e.getEntity();
            if(simon.getName().equalsIgnoreCase("Teddy__Burr")) { //Teddy__Burr
                try {
                    int i = ParseFile.readStevenFile(WriteFile.simonFile) + 1;
                    if(i % 100 == 0 && i != 0 && i != 100) {
                        e.getEntity().getServer().broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "[special pp] Congratulations Simon! you have been hit by your " + i + "th guardian");
                        e.getEntity().getServer().broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "To commemorate this momentous occasion you get to have their rarest drop! fishy");
                        ItemStack item = new ItemStack(Material.TROPICAL_FISH);
                        ItemMeta fishMeta = item.getItemMeta();
                        fishMeta.setDisplayName(ChatColor.GOLD + ChatColor.MAGIC.toString() + "oljsl" + ChatColor.RESET + "le fishe " + i + ChatColor.GOLD.toString() + ChatColor.MAGIC.toString() + "dkfjs");
                        item.setItemMeta(fishMeta);
                        simon.getInventory().addItem(item);
                        
                    } else if(i == 100) {
                        e.getEntity().getServer().broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "[special pp] Congratulations Simon! you have been hit by your " + i + "th guardian");
                        e.getEntity().getServer().broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "100 is a lot of guardians! so I figured you should have a special pp prize");
                        ItemStack itemG = new ItemStack(Material.GUARDIAN_SPAWN_EGG);
                        ItemStack itemE = new ItemStack(Material.ELDER_GUARDIAN_SPAWN_EGG);
                        ItemMeta gMeta = itemG.getItemMeta();
                        ItemMeta eMeta = itemE.getItemMeta();
                        eMeta.setDisplayName("don't talk to me or");
                        gMeta.setDisplayName("my son ever again");
                        itemG.setItemMeta(gMeta);
                        itemE.setItemMeta(eMeta);
                        simon.getInventory().addItem(itemE);
                        simon.getInventory().addItem(itemG);
                    } else {
                        e.getEntity().getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "yikes! Simon has been hit by a guardian!");
                        e.getEntity().getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "help him celebrate at " + (int)e.getEntity().getLocation().getX() + ", " + (int)e.getEntity().getLocation().getY() + ", " + (int)e.getEntity().getLocation().getZ());
                        e.getEntity().getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "this brings the total to " + String.valueOf(i) + " slippery slimey guardians!");
                    }
                    WriteFile.simonEntry(i);
                } catch(Exception ex) {
                    e.getEntity().getServer().broadcastMessage(ChatColor.RED + "yikes! Simon got hit by a guardian, and broke the plugin rip");
                }  
            }
        }
    }

    @EventHandler
    public static void onBlockPlaceEvent(BlockPlaceEvent b) {
        if(b.getPlayer().getName().equals("Xererain")) {
            if(b.getBlock().getType() == Material.BAMBOO_SAPLING) {
            Random r = new Random();
            int rand = r.nextInt(0, 10);
                if(rand == 1) {
                    try {
                        int i = ParseFile.readStevenFile(WriteFile.wyleFile) + 1;
                        b.getPlayer().getServer().broadcastMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "zoinks! Xererain is voilating someone with bamboo! better go check your base!");
                        b.getPlayer().getServer().broadcastMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "for the love of god someone please stop him at " + (int)b.getPlayer().getLocation().getX() + ", " + (int)b.getPlayer().getLocation().getY() + ", " + (int)b.getPlayer().getLocation().getZ());
                        b.getPlayer().getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "we estimate the impact at " + String.valueOf(i * 10) + " boisterous baneful bamboo!");
                        WriteFile.wyleEntry(i);
                    } catch (Exception e) {
                        return;
                    }
                } 
            }
        }
    }

    @EventHandler
    public static void onPlayerEggThrowEvent(PlayerEggThrowEvent e) throws Exception {
        Player player = e.getPlayer();
        Random r = new Random();
        int i = r.nextInt(100);
        if(i == 1) {
            player.sendMessage(ChatColor.RED + "[bad pp] you egg - lose 10 pp");
            PPEvent.online.get(player.getName()).setTimeImmediate(PPEvent.online.get(player.getName()).getLoginTime() + 10);
        }
    }

    @EventHandler
    public static void onPlayerBedEnterEvent(PlayerBedEnterEvent e) {
        Player player = e.getPlayer();
        Random r = new Random();
        int i = r.nextInt(100);
        if(i == 1) {
            player.sendMessage(ChatColor.GREEN + "[good pp] you find 10 pp under the pillow - gain 10 pp");
            PPEvent.online.get(player.getName()).setTimeImmediate(PPEvent.online.get(player.getName()).getLoginTime() - 10);
        }
    }

    @EventHandler
    public static void onPlayerHarvestEvent(PlayerHarvestBlockEvent e) {
        Player player = e.getPlayer();
        Random r = new Random();
        int i = r.nextInt(10000);
        if(i == 1) {
            player.sendMessage(ChatColor.GREEN + "[good pp] you discover a shiny rock, it's radioactive, it's 10 pp - gain 10 pp (you are insanely lucky)");
            PPEvent.online.get(player.getName()).setTimeImmediate(PPEvent.online.get(player.getName()).getLoginTime() - 10);
        }
    }

    @EventHandler
    public static void onPlayerResourcePackStatusEvent(PlayerResourcePackStatusEvent e) {
        Player player = e.getPlayer();
        Random r = new Random();
        int i = r.nextInt(100);
        if(i == 1) {
            player.sendMessage(ChatColor.RED + "[bad pp] you turn on your x-ray pack, a bird swoops down and takes something from your pocket, it stole 10 pp - lose 10 pp");
            PPEvent.online.get(player.getName()).setTimeImmediate(PPEvent.online.get(player.getName()).getLoginTime() + 10);
        }
    }

    @EventHandler
    public static void onVehicleExitEvent(VehicleExitEvent e) {
        Player player;
        if(e.getExited() instanceof Player) {
            player = (Player)e.getExited();
        } else {return;}
        Random r = new Random();
        int i = r.nextInt(100);
        if(i == 1) {
            player.sendMessage(ChatColor.RED + "[bad pp] you parked your car in the handicap space, shame on you - lose 10 pp");
            PPEvent.online.get(player.getName()).setTimeImmediate(PPEvent.online.get(player.getName()).getLoginTime() + 10);
        }
    }

    @EventHandler
    public static void onPlayerShearEntityEvent(PlayerShearEntityEvent e) {
        Player player = e.getPlayer();
        Random r = new Random();
        int i = r.nextInt(100);
        if(i == 1) {
            player.sendMessage(ChatColor.RED + "[bad pp] the animal did not like you shaving it, it bites you, you don't feel good anymore - lose 10 pp");
            PPEvent.online.get(player.getName()).setTimeImmediate(PPEvent.online.get(player.getName()).getLoginTime() + 10);
        }
    }
}
