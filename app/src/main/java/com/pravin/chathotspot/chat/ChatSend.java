package com.pravin.chathotspot.chat;

import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Messenger;
import android.text.format.Formatter;
import android.util.Log;

import com.pravin.chathotspot.file_share.SenderService;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

import javax.security.auth.login.LoginException;

/**
 * Created by Pravin Borate on 26/6/17.
 */

public class ChatSend
{
    String masg;
    private final WifiManager manager;
    private final DhcpInfo dhcp;
    private int port;
    Context context;
    private final String MASG = "MASG";
    private final String RECEIVER_IP = "RECEIVER_IP";
    private final String PORT = "PORT";
    private final String IS_HOST="HOST";
    private final String IP_RECEIVER="ipReceiver";

    private boolean isHost;


    public ChatSend(Context context,boolean isHost){
        this.context=context;
        this.isHost=isHost;
        manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        dhcp = manager.getDhcpInfo();
        Log.e("dhcp",dhcp.toString());
        try {
            Log.e("getWIFI",getWifiIp()+"");
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    private InetAddress getReceiverIP() {
        final String address;
        address = Formatter.formatIpAddress(dhcp.gateway); // gateway - default gateway IP address
        InetAddress receiverIP = null;
        Log.e("HOST",address);
        try {

            receiverIP = InetAddress.getByName(address);

                Log.i("FileSender","Receiver IP : " + receiverIP.toString());

        } catch (Exception e) {
                Log.e("FileSender","Cannot find receiver's IP. Error : " + e.toString());
        }

        return receiverIP;
    }

    public void sendFile(String masg,String ipAddress) {



        this.port = 2225;

        InetAddress receiverIP = getReceiverIP();
        getMyIP();
        Intent i;
        i = new Intent(context,SendChatService.class);

        i.putExtra(RECEIVER_IP,receiverIP);
        i.putExtra(PORT,port);
        i.putExtra(MASG,masg);
        i.putExtra(IS_HOST,isHost);
        i.putExtra(IP_RECEIVER,ipAddress);



        context.startService(i);
    }


    private InetAddress getMyIP() {
        if(isHost){
            InetAddress myIP = null;
            myIP=intToInetAddress(dhcp.gateway);
            Log.i("FileSender","My IP : " + myIP.toString());
            return myIP;
        }else {
            final String address = Formatter.formatIpAddress(dhcp.ipAddress); // ipAddress - IP address of my device, assigned through dhcp
            InetAddress myIP = null;

            try {

                myIP = InetAddress.getByName(address);

                Log.i("FileSender","My IP : " + myIP.toString());

            } catch (Exception e) {
                Log.e("FileSender","Cannot find my own IP. Error : " + e.toString());
            }

            return myIP;
        }

    }


    public InetAddress intToInetAddress(int hostAddress) {
        byte[] addressBytes = {(byte) (0xff & hostAddress),
                (byte) (0xff & (hostAddress >> 8)),
                (byte) (0xff & (hostAddress >> 16)),
                (byte) (0xff & (hostAddress >> 24))};

        try {
            return InetAddress.getByAddress(addressBytes);
        } catch (UnknownHostException e) {
            throw new AssertionError();
        }
    }


    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("ERROR", ex.toString());
        }
        return null;
    }


    private String getWifiIp() throws SocketException {
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            if (intf.isLoopback()) {
                continue;
            }
            if (intf.isVirtual()) {
                continue;
            }
            if (!intf.isUp()) {
                continue;
            }
            if (intf.isPointToPoint()) {
                continue;
            }
            if (intf.getHardwareAddress() == null) {
                continue;
            }
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                 enumIpAddr.hasMoreElements(); ) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (inetAddress.getAddress().length == 4) {
                    Log.e("check",inetAddress.toString());
                    //return inetAddress.getHostAddress();
                }
            }
        }
        return null;
    }
}
