package com.pravin.chathotspot;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    Button btnHost,btnJoin;
    WifiManager wifiManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnHost=(Button)findViewById(R.id.btnHost);
        btnJoin=(Button)findViewById(R.id.btnJoin);

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
                startActivity(new Intent(MainActivity.this,HostActivity.class));
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,JoinActivity.class));

            }
        });
    }
}
