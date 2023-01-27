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

package org.ben.plugin.drive;

import java.io.File;
import java.text.DecimalFormat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.ConsoleCommandSender;
import org.ben.plugin.PP;
import org.ben.plugin.io.WriteFile;

public class Backup {
    public static final String OUTPUT_ZIP_FILE = "pp_worldsave.zip";
    public static final String INPUT_DIR = "C:\\vanilla_server_19\\world";
    public static final DecimalFormat df = new DecimalFormat();

    public static void backup(Player p) {
        df.setMaximumFractionDigits(2);
        //disable the server's autosave
        ConsoleCommandSender sender = p.getServer().getConsoleSender();
        //disable autosave so no funny thread collisions
        Bukkit.dispatchCommand(sender, "save-off");

        //zip the world folder
        File input = new File(INPUT_DIR);
        p.getServer().broadcastMessage(ChatColor.GOLD + "compressing files... about " + df.format((double)(WriteFile.getDirSizeBytes(input) / 1000000.0)) + "mb");
        long time = System.currentTimeMillis();
        Zip appZip = new Zip(INPUT_DIR);
        appZip.generateFileList(input);
        try {
            appZip.zipIt(OUTPUT_ZIP_FILE);
        } catch(Exception e) {
            //ouf
            p.getServer().broadcastMessage(ChatColor.RED + "a funny occured while compressing " + e.toString());
            p.getServer().broadcastMessage(ChatColor.RED + "note that if you're seeing this message the compression did not complete");
        }
        p.getServer().broadcastMessage(ChatColor.GREEN + "done! (" + (System.currentTimeMillis() - time) + "ms)");

        //restore autosave 
        Bukkit.dispatchCommand(sender, "save-on");

        //upload to google drive
        time = System.currentTimeMillis();
        p.getServer().broadcastMessage(ChatColor.GOLD + "uploading...");
        try {
            //delete old file
            DriveAPI.deleteBasic();

            //upload new file
            DriveAPI.uploadBasic();
        } catch(Exception e) {
            e.printStackTrace();
            p.getServer().broadcastMessage(ChatColor.RED + "yikes");
            return;
        }
        p.getServer().broadcastMessage(ChatColor.GREEN + "done! (" + (System.currentTimeMillis() - time) + "ms)");
    }

    public static void backup(PP p) {
        df.setMaximumFractionDigits(2);
        //disable the server's autosave
        ConsoleCommandSender sender = p.getServer().getConsoleSender();
        //disable autosave so no funny thread collisions
        //Bukkit.dispatchCommand(sender, "save-off");

        //zip the world folder
        File input = new File(INPUT_DIR);
        //p.getServer().broadcastMessage(ChatColor.GOLD + "compressing files... about " + df.format((double)(WriteFile.getDirSizeBytes(input) / 1000000.0)) + "mb");
        long time = System.currentTimeMillis();
        Zip appZip = new Zip(INPUT_DIR);
        appZip.generateFileList(input);
        try {
            appZip.zipIt(OUTPUT_ZIP_FILE);
        } catch(Exception e) {
            //ouf
            //p.getServer().broadcastMessage(ChatColor.RED + "an exception occured while compressing " + e.toString());
            //p.getServer().broadcastMessage(ChatColor.RED + "note that if you're seeing this message the compression did not complete");
        }
        //p.getServer().broadcastMessage(ChatColor.GREEN + "done! (" + (System.currentTimeMillis() - time) + "ms)");

        //restore autosave 
        //Bukkit.dispatchCommand(sender, "save-on");

        //upload to google drive
        time = System.currentTimeMillis();
        //p.getServer().broadcastMessage(ChatColor.GOLD + "uploading...");
        try {
            //delete old file
            DriveAPI.deleteBasic();

            //upload new file
            DriveAPI.uploadBasic();
        } catch(Exception e) {
            e.printStackTrace();
            //p.getServer().broadcastMessage(ChatColor.RED + "yikes");
            return;
        }
        //p.getServer().broadcastMessage(ChatColor.GREEN + "done! (" + (System.currentTimeMillis() - time) + "ms)");
    }
}
