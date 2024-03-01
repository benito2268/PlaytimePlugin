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
import java.util.Enumeration;

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

    public static void filterBroadcast(org.bukkit.Server server, String text) {
        Enumeration<String> e = PPEvent.online.keys();
        while(e.hasMoreElements()) {
            String name = e.nextElement();
            if(PPEvent.online.get(name).hasSpamEnabled) {
                server.getPlayer(name).sendMessage(text);
            }
        }
    }
}
