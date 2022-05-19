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

package org.ben.plugin.io;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;

public class ParseFile {
    protected static List<String> lines;

    public static void readFile(File f) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(f.getName()));
        lines = new ArrayList<String>();
        String line = "";
        while((line = br.readLine()) != null) {
            lines.add(line);
        }
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
}
