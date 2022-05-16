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

public class PlayerTime {
    protected String name;
    protected String uuid;
    protected long loginTime;
    protected long totalTimeAtLogin;
    public File inFile;
    protected long inFileTime;

    public PlayerTime(String name, String uuid, File inFile) {
        this.name = name;
        this.uuid = uuid;
        this.loginTime = 0;
        this.totalTimeAtLogin = 0;
        this.inFile = inFile;
    }

    public PlayerTime(String name) {
        this.name = name;
        this.uuid = "UNKNOWN";
        this.loginTime = 0;
        this.totalTimeAtLogin = 0;
        this.inFile = null;
    }

    public PlayerTime(String name, String uuid, String inFileTime, File inFile) {
        this.name = name;
        this.uuid = uuid;
        this.inFile = inFile;
        this.inFileTime = Long.parseLong(inFileTime);
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public long getCurrTime() {
        return System.currentTimeMillis() - loginTime;
    }

    public void setTimeImmediate(long newTime) {
        loginTime = newTime;
    }

    public void login() throws Exception{
        this.loginTime = System.currentTimeMillis();
        String[] tokens = ParseFile.getLineInFile(this.inFile, this).split("~");
        this.totalTimeAtLogin = Integer.parseInt(tokens[2]);
    }

    public long getTotalTime() {
        return totalTimeAtLogin + (System.currentTimeMillis() - loginTime) - 5; //to accout for small delay running code
    }

    public long getTotalTimeInFile() {
        return this.inFileTime;
    }
}
