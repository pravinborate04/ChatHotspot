package com.pravin.chathotspot.chat;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.net.InetAddress;

/**
 * Created by Pravin Borate on 27/6/17.
 */

public class SendChatService extends Service {


    private final String MASG = "MASG";
    private final String RECEIVER_IP = "RECEIVER_IP";
    private final String PORT = "PORT";

    private InetAddress receiverIP;
    private int port;
    private Messenger messenger;
    private String masg;
    private boolean isHost;
    private String ipReceiver;
    private final String IS_HOST="HOST";
    private final String IP_RECEIVER="ipReceiver";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle b = intent.getExtras();
        Log.e("Servcie","service");
        receiverIP = (InetAddress) b.get(RECEIVER_IP);
        port = (int) b.get(PORT);
        masg = (String) b.get(MASG);
        isHost=(boolean)b.get(IS_HOST);
        ipReceiver=(String)b.get(IP_RECEIVER);
        ChatSendThread chatSendThread=new ChatSendThread(receiverIP,port,masg,isHost,ipReceiver);
        chatSendThread.start();


        return START_REDELIVER_INTENT;
    }
}
