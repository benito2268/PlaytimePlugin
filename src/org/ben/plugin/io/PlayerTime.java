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

public class PlayerTime {
    protected String name;
    protected String uuid;
    protected long loginTime;
    protected long totalTimeAtLogin;
    public File inFile;
    protected long inFileTime;
    public boolean hasSpamEnabled = true;
    public boolean hasDmEnabled = true;

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

    public long getLoginTime() {
        return this.loginTime;
    }

    public void setTimeImmediate(long newTime) {
        loginTime = newTime;
    }

    public synchronized void login() throws Exception{
        this.loginTime = System.currentTimeMillis();
        String[] tokens = ParseFile.getLineInFile(this.inFile, this).split("~");
        this.totalTimeAtLogin = Integer.parseInt(tokens[2]);
    }

    public long getTotalTime() {
        return totalTimeAtLogin + (System.currentTimeMillis() - loginTime);
    }

    public long getTotalTimeInFile() {
        return this.inFileTime;
    }

    public void toggleSpamStatus() {
        this.hasSpamEnabled = !hasSpamEnabled;
    }

    public void toggleDmStatus() {
        this.hasDmEnabled = !hasDmEnabled;
    }
}
