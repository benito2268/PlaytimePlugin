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
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileOutputStream;

public class WriteFile {
    protected static boolean created = false;
    protected static boolean stevenCreated = false;
    protected static boolean simonCreated = false;
    protected static boolean wyleCreated = false;
    public static File dataFile;
    public static File stevenFile;
    public static File simonFile;
    public static File wyleFile;

    public static File createFile() throws Exception {
        dataFile = new File("playertime.pp");
        if(dataFile.createNewFile()) {created = true;}
        return dataFile;
    }

    public static File createStevenFile() throws Exception {
        stevenFile = new File("steven.pp");
        if(stevenFile.createNewFile()) {stevenCreated = true;}
        return stevenFile;
    }

    public static File createSimonFile() throws Exception{
        simonFile = new File("simon.pp");
        if(simonFile.createNewFile()) {simonCreated = true;}
        return simonFile;
    }

    public static File createWyleFile() throws Exception {
        wyleFile = new File("wyle.pp");
        if(wyleFile.createNewFile()) {wyleCreated = true;}
        return wyleFile;
    }

    public static synchronized void newEntry(PlayerTime p) throws Exception {
        if(!dataFile.exists()) {throw new FileNotFoundException("fatal: system could not find the file specified " + dataFile.getName());}
        BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile.getName(), true));
        if(!ParseFile.existsInFile(dataFile, p)) {
            bw.write(p.getName() + "~" + p.getUuid() + "~" + "0\n");
        }
        bw.flush();
        bw.close();
    }

    public static void stevenEntry(int hits) throws Exception{
        if(!stevenFile.exists()) {throw new FileNotFoundException("fatal: system could not find the file specified " + stevenFile.getName());}
        FileOutputStream fout = new FileOutputStream(stevenFile);
        fout.write(String.valueOf(hits).getBytes());
        fout.flush();
        fout.close();
    }

    public static void simonEntry(int hits) throws Exception {
        if(!simonFile.exists()) {throw new FileNotFoundException("fatal: system could not find the file specified " + simonFile.getName());}
        FileOutputStream fout = new FileOutputStream(simonFile);
        fout.write(String.valueOf(hits).getBytes());
        fout.flush();
        fout.close();
    }

    public static void wyleEntry(int atrocities) throws Exception {
        if(!wyleFile.exists()) {throw new FileNotFoundException("fatal: system could not find the file specified " + wyleFile.getName());}
        FileOutputStream fout = new FileOutputStream(wyleFile);
        fout.write(String.valueOf(atrocities).getBytes());
        fout.flush();
        fout.close();
    }

    public static void updateEntry(PlayerTime p) throws Exception {
        if(!dataFile.exists()) {throw new FileNotFoundException("fatal: system could not find the file specified " + dataFile.getName());}
        BufferedReader br = new BufferedReader(new FileReader(dataFile.getName()));
        StringBuffer inputBuffer = new StringBuffer();
        String line = "";
        while((line = br.readLine()) != null) {
            inputBuffer.append(line);
            inputBuffer.append("\n");
        }
        br.close();
        //split by newlines
        String[] args = inputBuffer.toString().split("\n");
        for(int i = 0; i < args.length; i++) {
            if(args[i].contains(p.getName()) || args[i].contains(p.getUuid())) {
                args[i] = p.getName() + "~" + p.getUuid() + "~" + String.valueOf(p.getTotalTime());
                break;
            }
        }
        //re-write the buffer to the file
        String outputStr = "";
        for(String s : args) {
            outputStr += s + "\n";
        }
        FileOutputStream fout = new FileOutputStream(dataFile);
        fout.write(outputStr.getBytes());
        fout.flush();
        fout.close();
    }
    
    public static long getDirSizeBytes(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += getDirSizeBytes(file);
        }
        return length;
    }
}
