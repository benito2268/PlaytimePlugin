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
                toRet.add("restrain");
                toRet.add("spam");
                toRet.add("~changelog");
                toRet.add("~score");
                return toRet;
            } catch(Exception e) {
                return toRet;
            }
        } else if(args.length == 2) {
            try {
                if(args[1].equals("restrain") || args[1].equals("spam")) {
                    toRet.add("yes");
                    toRet.add("no");
                } else {
                    toRet = ParseFile.getNamesInFile(WriteFile.dataFile);
                }
            } catch(Exception e) {
                return toRet;
            }
            return toRet;
        }
        return toRet;
    }
}
