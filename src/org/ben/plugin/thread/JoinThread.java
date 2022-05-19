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
 * @date 5/19/22
 */

package org.ben.plugin.thread;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import org.ben.plugin.event.PPEvent;
import org.ben.plugin.io.*;
import org.bukkit.ChatColor;

public class JoinThread implements Runnable{

    protected Player joinTarget;
    protected Thread t;

    public JoinThread(PlayerJoinEvent e) {
        this.joinTarget = e.getPlayer();
        t = new Thread(this, joinTarget.getName() + "joinThread");
        this.joinTarget.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[pp threaded operation] begin concurrent excecution");
        t.start();
    }

    @Override
    public void run() {
        try {
            PlayerTime pt = new PlayerTime(joinTarget.getName(), joinTarget.getUniqueId().toString(), WriteFile.dataFile);
            if(!ParseFile.existsInFile(pt.inFile, pt)) {
                WriteFile.newEntry(pt);
            }
            pt.login();
            PPEvent.online.put(pt.getName(), pt);
        } catch(Exception e) {
            this.joinTarget.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[pp threaded operation] target player join failed. No time is being kept for " + joinTarget.getName());
        }   
    }

    public Thread getThread() {
        return this.t;
    }
}
