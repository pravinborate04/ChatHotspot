package com.pravin.chathotspot.chat;

import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.pravin.chathotspot.file_share.FileSender;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Pravin Borate on 27/6/17.
 */

public class ChatSendThread extends Thread {

    private InetAddress receiverIP;
    private int port;
    private String msg;
    private Messenger messenger;
    private Socket senderSocket;
    private int PKT_SIZE = 60*1024;
    private boolean isHost;
    private String ipReceiver;

    public ChatSendThread(InetAddress receiverIP, int port, String msg,boolean isHost,String ipReceiver){
        this.receiverIP = receiverIP;
        this.port = port;
        this.msg=msg;
        this.isHost=isHost;
        this.ipReceiver=ipReceiver;
    }

    @Override
    public void run() {
        super.run();
        try {

            if (isHost){
                Log.e("Sending",ipReceiver);
                senderSocket = new Socket(ipReceiver,port);

            }else {
                Log.e("Sending",receiverIP.toString());
                senderSocket = new Socket(receiverIP,port);
            }

            DataOutputStream out = new DataOutputStream(senderSocket.getOutputStream());
            out.writeUTF(msg);
            //DataInputStream din = new DataInputStream(new FileInputStream(msg));

            // Send File Size
            long fileSize = msg.length();
            out.writeLong(fileSize);

            int totalLength = 0;
            int length = 0;
            byte[] sendData = new byte[PKT_SIZE];

            long startTime = System.currentTimeMillis();
            long tempSize=fileSize;

            // Send the file data
         /*   while ((length = din.read(sendData)) != -1) {
                out.write(sendData, 0, length);
                totalLength += length;
            }*/

            Log.e("String Sent","Sent");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
