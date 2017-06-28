package com.pravin.chathotspot;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;

import com.pravin.chathotspot.chat.ConnectedDeviceModel;

import java.util.List;

/**
 * Created by Pravin Borate on 28/6/17.
 */

public class ConnectedDevicesAdapter extends RecyclerView.Adapter<ConnectedDevicesAdapter.ConnectedDeviceViewHolder> {

    Context mContext;
    LayoutInflater layoutInflater;
    List<ConnectedDeviceModel> connectedDeviceModels;


    public ConnectedDevicesAdapter(Context context, List<ConnectedDeviceModel> connectedDeviceModels){
        this.mContext=context;
        this.layoutInflater=LayoutInflater.from(mContext);
        this.connectedDeviceModels=connectedDeviceModels;
    }

    @Override
    public ConnectedDeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.connected_devices_row,parent,false);
        ConnectedDeviceViewHolder holder =new ConnectedDeviceViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ConnectedDeviceViewHolder holder, int position) {

        holder.txtConnectedDeviceIpAddress.setText(connectedDeviceModels.get(position).getIpAddress());
        holder.txtConnectedDeviceMacAddress.setText(connectedDeviceModels.get(position).getMacAddress());
        holder.pos=position;
    }

    @Override
    public int getItemCount() {
        return connectedDeviceModels.size();
    }

    class ConnectedDeviceViewHolder extends RecyclerView.ViewHolder{
        TextView txtConnectedDeviceIpAddress,txtConnectedDeviceMacAddress;
        int pos;
        LinearLayout connectedDeviceParentView;

        public ConnectedDeviceViewHolder(View itemView) {
            super(itemView);
            txtConnectedDeviceMacAddress=(TextView)itemView.findViewById(R.id.txtConnectedDeviceMacAddress);
            txtConnectedDeviceIpAddress=(TextView)itemView.findViewById(R.id.txtConnectedDeviceIpAddress);
            connectedDeviceParentView=(LinearLayout)itemView.findViewById(R.id.connectedDeviceParentView);

            connectedDeviceParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,ChatActivity.class);
                    intent.putExtra("isHost",true);
                    intent.putExtra("device",connectedDeviceModels.get(pos));
                    mContext.startActivity(intent);
                }
            });

        }
    }
}
