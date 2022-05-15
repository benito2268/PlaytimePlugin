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
    public static File dataFile;

    public static File createFile() throws Exception {
        dataFile = new File("playertime.pp");
        if(dataFile.createNewFile()) {created = true;}
        return dataFile;
    }

    public static void newEntry(PlayerTime p) throws Exception {
        if(!dataFile.exists()) {throw new FileNotFoundException("fatal: system could not find the file specified");}
        BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile.getName(), true));
        if(!ParseFile.existsInFile(dataFile, p)) {
            bw.write(p.getName() + "~" + p.getUuid() + "~" + "0\n");
        }
        bw.close();
    }

    public static void updateEntry(PlayerTime p) throws Exception {
        if(!dataFile.exists()) {throw new FileNotFoundException("fatal: system could not find the file specified");}
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
        fout.close();
    }
}
