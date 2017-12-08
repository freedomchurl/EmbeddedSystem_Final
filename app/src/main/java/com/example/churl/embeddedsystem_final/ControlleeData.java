package com.example.churl.embeddedsystem_final;

/**
 * Created by churl on 2017-12-08.
 */

public class ControlleeData {

    String deviceName;
    String deviceIP;
    int icon;
    boolean isReadMode;

    private int [] imageArray = {R.drawable.bulb,R.drawable.crane,
            R.drawable.handshake, R.drawable.piston,
            R.drawable.team, R.drawable.remote};

    String textData = "";
    String dotData = "";
    int SegData = 0;
    int [][] full = new int[4][3];
    boolean [] led = new boolean[8];


    boolean selected = false;

    ControlleeData()
    {

    }


    ControlleeData(String devName, String ip, int icon, String isReadMode)
    {
        this.deviceIP = ip;
        if(isReadMode.equals("TRUE"))
        {
            this.isReadMode = true;
        }
        else
            this.isReadMode = false;

        this.deviceName = devName;
        if(icon>=0)
            this.icon = imageArray[icon];
        else
            this.icon = imageArray[0];
    }

    public int getIcon() {
        return icon;
    }

    public String getDeviceIP() {
        return deviceIP;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceIP(String deviceIP) {
        this.deviceIP = new String(deviceIP);
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = new String(deviceName);
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int[][] getFull() {
        return full;
    }

    public String getDotData() {
        return dotData;
    }

    public int getSegData() {
        return SegData;
    }

    public String getTextData() {
        return textData;
    }

    public boolean[] getLed() {
        return led;
    }

    public void setLed(boolean[] led) {
        this.led = new boolean[8];
        for(int i=0;i<8;i++)
            this.led[i] = led[i];
    }


    public void setDotData(String dotData) {
        this.dotData = new String(dotData);
    }

    public void setFull(int[][] full) {

        this.full = new int[4][3];
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<3;j++)
            {
                this.full[i][j] = full[i][j];
            }
        }

    }

    public void setReadMode(boolean readMode) {
        isReadMode = readMode;
    }

    public void setSegData(int segData) {
        SegData = segData;
    }

    public void setTextData(String textData) {
        this.textData = new String(textData);
    }

    public String getData() {
        String allMessage = "DATA:\nTEXT:" + this.getTextData() + "\n" + "DOTM:" + this.getDotData() + "\n" + "SEGM:" + this.getSegData() + "\n" + "FULL:";

        int[][] get = this.getFull();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                allMessage = allMessage + String.format("%03d//", get[i][j]);
            }
        }

        allMessage += "\n";

        if (this.isReadMode == true) {
            allMessage += "MODE:TRUE\n";
        } else {
            allMessage += "MODE:FALSE\n";
        }

        allMessage += "LED8:";

        for (int i = 0; i < 8; i++) {
            if (this.getLed()[i] == true) {
                allMessage = allMessage + "1";
            } else {
                allMessage += "0";
            }
        }
        return allMessage;
    }
}
