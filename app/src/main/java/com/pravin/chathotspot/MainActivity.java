package com.pravin.chathotspot;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pravin.chathotspot.chat.ConnectedDeviceModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnHost,btnJoin;
    WifiManager wifiManager;
    List<ConnectedDeviceModel> connectedDeviceModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnHost=(Button)findViewById(R.id.btnHost);
        btnJoin=(Button)findViewById(R.id.btnJoin);
        connectedDeviceModels=new ArrayList<>();
        btnHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
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
                }

                new GetConnectedDevices().execute();

            }
        });


        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,JoinActivity.class));

            }
        });
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


    class GetConnectedDevices extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            Log.e("Start","Start");
            Tools.showProgress(MainActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            //getfromLoop();
            getListOfConnectedDevice();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Log.e("Done","Done");
            Tools.hideProgress();
            if(connectedDeviceModels.size()>0){
                startActivity(new Intent(MainActivity.this,HostActivity.class));
            }else {
                Toast.makeText(MainActivity.this, "No Device is Connected Yet", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
