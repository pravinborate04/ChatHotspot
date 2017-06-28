package com.pravin.chathotspot.chat;

import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Pravin Borate on 26/6/17.
 */

public class ReceiveChatThread extends Thread
{
    Socket communicationSocket;
    Messenger messenger;
    @Override
    public void run() {
        super.run();
      getMassage();
    }

    public ReceiveChatThread(Messenger messenger){
        this.messenger=messenger;
    }


    public void getMassage(){
        ServerSocket serverSocket=null;
        Message message;
        try {
            serverSocket=new ServerSocket(2225);
            Log.e("Listenning","to port 2225");
            communicationSocket=serverSocket.accept();
            Log.e("connected","to port 2225");
            DataInputStream in = new DataInputStream(communicationSocket.getInputStream());

            message=Message.obtain();
            message.obj=in.readUTF();
            messenger.send(message);

            Log.e("received masg",in.readUTF());
            communicationSocket.close();
            serverSocket.close();
            getMassage();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            try {

                if(serverSocket!=null)
                    serverSocket.close();

            } catch (IOException ioe) {
                Log.e("SenderThread","Error in closing sockets. Error : " + ioe.toString());
                ioe.printStackTrace();
            }
        }
    }


    public String readFullyAsString(InputStream inputStream, String encoding)
            throws IOException {
        return readFully(inputStream).toString(encoding);
    }

    public byte[] readFullyAsBytes(InputStream inputStream)
            throws IOException {
        return readFully(inputStream).toByteArray();
    }

    private ByteArrayOutputStream readFully(InputStream inputStream)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos;
    }
}
