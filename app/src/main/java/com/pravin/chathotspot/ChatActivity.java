package com.pravin.chathotspot;

import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pravin.chathotspot.chat.ChatSend;
import com.pravin.chathotspot.chat.ConnectedDeviceModel;
import com.pravin.chathotspot.chat.ReceiverChatService;

import java.net.InetAddress;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    boolean isHost;
    Button btnSend;
    EditText edtText;
    LinearLayout linChat;
    ConnectedDeviceModel connectedDeviceModel;

    private Handler mHandler=new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(Message msg) {
            Log.e("handle",msg.obj+"");
            linChat.addView(addChatTextView(msg.obj.toString(),false));

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        linChat=(LinearLayout)findViewById(R.id.linChat);
        btnSend=(Button)findViewById(R.id.btnSend);
        edtText=(EditText)findViewById(R.id.edtText);
        if(getIntent()!=null){
            isHost=getIntent().getBooleanExtra("isHost",false);
            connectedDeviceModel= (ConnectedDeviceModel) getIntent().getSerializableExtra("device");
        }

        Log.e("isHost",isHost+"");


        btnSend.setOnClickListener(this);

        Intent intent1=new Intent(this, ReceiverChatService.class);
        intent1.putExtra("massenger",new Messenger(mHandler));
        startService(intent1);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        ChatSend chatSend=new ChatSend(ChatActivity.this,isHost);

        chatSend.sendFile(edtText.getText().toString(),
                connectedDeviceModel==null?"":connectedDeviceModel.getIpAddress());
        linChat.addView(addChatTextView(edtText.getText().toString(),true));
        edtText.setText("");
    }

    public View addChatTextView(String chat,boolean fromCurrnt){
        View view= LayoutInflater.from(ChatActivity.this).inflate(R.layout.layout_current_user_chat,null);
        TextView textView= (TextView) view.findViewById(R.id.txtChat);
        textView.setText(chat);
        if(!fromCurrnt){
            textView.setGravity(Gravity.LEFT);
        }else {
            textView.setGravity(Gravity.RIGHT);

        }
        return view;
    }


    private void hideKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}
