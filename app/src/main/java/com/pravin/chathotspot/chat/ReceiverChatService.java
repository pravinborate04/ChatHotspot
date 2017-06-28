package com.pravin.chathotspot.chat;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.Nullable;

/**
 * Created by Pravin Borate on 26/6/17.
 */

public class ReceiverChatService extends Service {

    private Messenger messenger;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent==null){
            return super.onStartCommand(intent, flags, startId);
        }
        Bundle b = intent.getExtras();

        messenger = (Messenger) b.get("massenger");
        new ReceiveChatThread(messenger).start();

        return super.onStartCommand(intent, flags, startId);


    }
}
