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

package org.ben.plugin.event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.ben.plugin.io.PlayerTime;
import org.ben.plugin.io.WriteFile;
import java.util.Hashtable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import java.util.Random;
import org.bukkit.ChatColor;
import org.ben.plugin.thread.JoinThread;

public class PPEvent implements Listener {
    public static Hashtable<String, PlayerTime> online = new Hashtable<>();

    @EventHandler
    public static synchronized void onPlayerJoin(PlayerJoinEvent p) throws Exception{
        try {
            JoinThread t = new JoinThread(p);
            t.getThread().interrupt();

        } catch(Exception e) {
            p.getPlayer().getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[pp threaded operation] warn: thread destroyed with errors");
        }
        p.getPlayer().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[pp threaded operation] completed with no errors, thread destroyed successfully");
    }

    @EventHandler
    public static synchronized void onPlayerQuit(PlayerQuitEvent p) throws Exception {
        Player player = p.getPlayer();
        PlayerTime pt = online.get(player.getName());
            WriteFile.updateEntry(pt);
        online.remove(player.getName());
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
}
