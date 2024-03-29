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
        //first update the variable
        try {
            long l = ParseFile.getMillisByPlayerName(this.inFile, this.name);
            if(l >= 0) {
                this.inFileTime = l;
            }
        } catch (Exception e) {
            return -1L;
        }

        return this.inFileTime;
    }

    public void toggleSpamStatus() {
        this.hasSpamEnabled = !hasSpamEnabled;
    }

    public void toggleDmStatus() {
        this.hasDmEnabled = !hasDmEnabled;
    }
}
