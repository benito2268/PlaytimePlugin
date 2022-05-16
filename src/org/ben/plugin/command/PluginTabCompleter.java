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

import java.util.ArrayList;
import java.util.List;

import org.ben.plugin.io.ParseFile;
import org.ben.plugin.io.WriteFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class PluginTabCompleter implements TabCompleter{

    @Override
    public List<String> onTabComplete(CommandSender arg0, Command arg1, String alieses ,String[] args) {
        List<String> toRet = new ArrayList<>();
        if(args.length == 1) {
            try {
                toRet = ParseFile.getNamesInFile(WriteFile.dataFile);
                toRet.add("~info");
                toRet.add("~score");
                return toRet;
            } catch(Exception e) {
                return toRet;
            }
        }
        return toRet;
    }
}
