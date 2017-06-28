package com.pravin.chathotspot.chat;

import java.io.Serializable;

/**
 * Created by Pravin Borate on 28/6/17.
 */

public class ConnectedDeviceModel implements Serializable {

    private String macAddress;
    private String ipAddress;


    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }


    public String toString(){
        return "IP Address : "+ipAddress+"\n"
                +" Mac Address : "+macAddress;
    }
}
