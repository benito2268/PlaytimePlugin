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

package org.ben.plugin.io;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;

public class ParseFile {
    protected static List<String> lines;

    protected static void readFile(File f) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(f.getName()));
        lines = new ArrayList<String>();
        String line = "";
        while((line = br.readLine()) != null) {
            lines.add(line);
        }
    }

    public static int readStevenFile(File f) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(f.getName()));
        String line = "";
        while((line = br.readLine()) != null) {
            br.close();
            return Integer.parseInt(line);
        }
        return -1;
    }

    public static synchronized boolean existsInFile(File f, PlayerTime p) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(f.getName()));
        String line = "";
        while((line = br.readLine()) != null) {
            if(line.contains(p.getName()) || line.contains(String.valueOf(p.getUuid()))) {
                br.close();
                return true;
            }
        }
        br.close();
        return false;
    }

    public static String getLineInFile(File f, PlayerTime p) throws Exception {
        if(lines == null || lines.size() == 0) {
            readFile(f);
        }
        BufferedReader br = new BufferedReader(new FileReader(f.getName()));
        String line = "";
        while((line = br.readLine()) != null) {
            if(line.contains(p.getName()) || line.contains(String.valueOf(p.getUuid()))) {
                br.close();
                return line;
            }
        }
        br.close();
        return null;
    }

    public static PlayerTime getPlayerTimeInFile(File f, String name) throws Exception{
        if(lines == null || lines.size() == 0) {
            readFile(f);
        }
        BufferedReader br = new BufferedReader(new FileReader(f.getName()));
        String line = "";
        while((line = br.readLine()) != null) {
            if(line.contains(name)) {
                br.close();
                String[] args = line.split("~");
                return new PlayerTime(args[0].trim(), args[1].trim(), args[2].trim(), f);
            }
        }
        br.close();
        return null;
    }

    public static long getMillisByPlayerName(File f, String playername) throws Exception {
        if(lines == null || lines.size() == 0) {
            readFile(f);
        }
        BufferedReader br = new BufferedReader(new FileReader(f.getName()));
        String line = "";
        while((line = br.readLine()) != null) {
            if(line.contains(playername)) {
                br.close();
                String[] args = line.split("~");
                return Long.parseLong(args[2].trim());
            }
        }
        br.close();
        return -1L;
    }

    public static List<String> getNamesInFile(File f) throws Exception{
        List<String> toRet = new ArrayList<String>(); 
        if(lines == null || lines.size() == 0) {
            readFile(f);
        }
        BufferedReader br = new BufferedReader(new FileReader(f.getName()));
        String line = "";
        while((line = br.readLine()) != null) {
            String[] args = line.split("~");
            toRet.add(args[0]);
        }
        br.close();
        return toRet;
    }

    public static List<String> getLines() {
        if(lines.size() == 0) {
            try {
                readFile(WriteFile.dataFile);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return lines;
    }

    public static List<PlayerTime> getAllPlayers(File f) throws Exception{
        if(lines == null || lines.size() == 0) {
            readFile(f);
        }
        BufferedReader br = new BufferedReader(new FileReader(f.getName()));
        String line = "";
        ArrayList<PlayerTime> toReturn = new ArrayList<>();
        while((line = br.readLine()) != null) {
            String[] args = line.split("~");
            toReturn.add(new PlayerTime(args[0].trim(), args[1].trim(), args[2].trim(), f));  
        }
        br.close();
        return toReturn;
    }
}
