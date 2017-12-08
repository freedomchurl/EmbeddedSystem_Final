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
        this.icon = imageArray[icon];
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
        this.deviceIP = deviceIP;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
