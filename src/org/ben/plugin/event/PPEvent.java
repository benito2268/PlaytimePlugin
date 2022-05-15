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

package org.ben.plugin.event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.ben.plugin.io.ParseFile;
import org.ben.plugin.io.PlayerTime;
import org.ben.plugin.io.WriteFile;
import java.util.Hashtable;
import org.bukkit.entity.Player;

public class PPEvent implements Listener {
    public static Hashtable<String, PlayerTime> online = new Hashtable<>();

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent p) throws Exception{
        Player player = p.getPlayer();
        PlayerTime pt = new PlayerTime(player.getName(), player.getUniqueId().toString(), WriteFile.dataFile);
        if(!ParseFile.existsInFile(pt.inFile, pt)) {
            WriteFile.newEntry(pt);
        }
        pt.login();
        online.put(pt.getName(), pt);
    }

    @EventHandler
    public static void onPlayerQuit(PlayerQuitEvent p) throws Exception {
        Player player = p.getPlayer();
        PlayerTime pt = online.get(player.getName());
            WriteFile.updateEntry(pt);
        online.remove(player.getName());
    }
}
