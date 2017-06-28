package com.pravin.chathotspot;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pravin.chathotspot.chat.ConnectedDeviceModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class HostActivity extends AppCompatActivity {
    WifiManager wifiManager;
    Button btnProceedToChat;
    RecyclerView recylerDevicesConnected;
    List<ConnectedDeviceModel> connectedDeviceModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        btnProceedToChat=(Button)findViewById(R.id.btnProceedToChat);
        recylerDevicesConnected=(RecyclerView)findViewById(R.id.recylerDevicesConnected);
        recylerDevicesConnected.setLayoutManager(new LinearLayoutManager(this));
        connectedDeviceModels=new ArrayList<>();
        btnProceedToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HostActivity.this,ChatActivity.class);
                intent.putExtra("isHost",true);
                startActivity(intent);
            }
        });


        /*wifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(false);
        }

        WifiConfiguration netConfig = new WifiConfiguration();

        netConfig.SSID = "aaaaaa";
       // netConfig.preSharedKey="123";
        // netConfig.BSSID="HOTSPOT";
        // Creates Hotspot
        netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//        netConfig.providerFriendlyName="PRAVIN";
        // netConfig.networkId=1111;
        try{
            Method setWifiApMethod = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            boolean apstatus=(Boolean) setWifiApMethod.invoke(wifiManager, netConfig,true);

            Method isWifiApEnabledmethod = wifiManager.getClass().getMethod("isWifiApEnabled");
            while(!(Boolean)isWifiApEnabledmethod.invoke(wifiManager)){

            };
            Method getWifiApStateMethod = wifiManager.getClass().getMethod("getWifiApState");
            int apstate=(Integer)getWifiApStateMethod.invoke(wifiManager);
            Method getWifiApConfigurationMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
            netConfig=(WifiConfiguration)getWifiApConfigurationMethod.invoke(wifiManager);
            Log.e("CLIENT", "\nSSID:"+netConfig.SSID+"\nPassword:"+netConfig.preSharedKey+"\n FQDN "+netConfig.FQDN+"\n");

        } catch (Exception e) {
            Log.e(this.getClass().toString(), "", e);
        }*/


//        getListOfConnectedDevice();
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.e("Start","Start");
                Tools.showProgress(HostActivity.this);
            }

            @Override
            protected Void doInBackground(Void... params) {
                //getfromLoop();
                getListOfConnectedDevice();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.e("Done","Done");
                Tools.hideProgress();
                recylerDevicesConnected.setAdapter(new ConnectedDevicesAdapter(HostActivity.this,connectedDeviceModels));
            }
        }.execute();

    }



    public void getListOfConnectedDevice() {
        /*Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {*/
                BufferedReader br = null;
                boolean isFirstLine = true;

                try {
                    br = new BufferedReader(new FileReader("/proc/net/arp"));
                    String line;

                    while ((line = br.readLine()) != null) {
                        if (isFirstLine) {
                            isFirstLine = false;
                            continue;
                        }

                        String[] splitted = line.split(" +");

                        if (splitted != null && splitted.length >= 4) {

                            String ipAddress = splitted[0];
                            String macAddress = splitted[3];

                            boolean isReachable = InetAddress.getByName(
                                    splitted[0]).isReachable(500);  // this is network call so we cant do that on UI thread, so i take background thread.
                            if (isReachable) {
                                ConnectedDeviceModel connectedDeviceModel = new ConnectedDeviceModel();
                                connectedDeviceModel.setIpAddress(ipAddress);
                                connectedDeviceModel.setMacAddress(macAddress);
                                connectedDeviceModels.add(connectedDeviceModel);
                                Log.d("Device Information", ipAddress + " : "
                                        + macAddress);
                            }

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
       /* });
        thread.start();
    }*/

    public void getfromLoop(){
        try {
            NetworkInterface iFace = NetworkInterface
                    .getByInetAddress(InetAddress.getByName("192.168.43.1"));

            for (int i = 0; i <= 255; i++) {

                // build the next IP address
                String addr = "192.168.43.1";
                addr = addr.substring(0, addr.lastIndexOf('.') + 1) + i;
                Log.e("addr",addr);
                InetAddress pingAddr = InetAddress.getByName(addr);

                // 50ms Timeout for the "ping"
                if (pingAddr.isReachable(iFace, 200, 100)) {
                    Log.d("PING", pingAddr.getHostAddress());
                }
            }
        } catch (UnknownHostException ex) {
        } catch (IOException ex) {
        }
    }
}
