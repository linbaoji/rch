package com.rch.adatper;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.entity.NotifInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/9/4.
 */

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.MyViewHolder>{
    Context context;
    List<NotifInfo> list;
    SharedPreferences sharedPreferences;
    NotifInfo notifInfo;
    public NotifAdapter(Context context, List<NotifInfo> list) {
        this.context=context;
        this.list=list;
        sharedPreferences=context.getSharedPreferences("User",context.MODE_PRIVATE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notif_item,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        notifInfo= list.get(position);

        holder.notif_data.setText(notifInfo.getCreateTime());
        holder.notif_title.setText(notifInfo.getNoticeTitle());

        holder.notif_time.setText(notifInfo.getCreateTime());
        holder.notif_content.setText(notifInfo.getContent());
        String notifId=sharedPreferences.getString("notifId","");
        holder.notif_unread.setVisibility(View.VISIBLE);
        if(notifId.isEmpty())
            holder.notif_unread.setVisibility(View.VISIBLE);
        else if(notifId.indexOf(",")==-1)
        {
            if(notifInfo.getId().equals(notifId)) {
                holder.notif_unread.setVisibility(View.INVISIBLE);
            }
        }else
        {
            String[] len=notifId.split(",");
            for (int i=0;i<len.length;i++)
            {
                if(notifInfo.getId().equals(len[i])) {
                    holder.notif_unread.setVisibility(View.INVISIBLE);
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView notif_data,notif_title,notif_time,notif_content;
        LinearLayout notif_unread;
        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            notifInfo= list.get(getPosition());

            notifyDataSetChanged();
        }
    }

    private void isCheckUnread(NotifInfo object)
    {
        String notifId=sharedPreferences.getString("notifId","");
        int notifCount=sharedPreferences.getInt("notifCount",0);
        if(notifId.isEmpty())
            saveNotifMsgCount(object.getId(),notifCount+1);
        else if(notifId.indexOf(",")==-1)
        {
           if(!object.getId().equals(notifId)) {
               notifId+=","+object.getId();
               saveNotifMsgCount(notifId, notifCount + 1);
           }
        }
        else{
            String[] len=notifId.split(",");
            for (int i=0;i<len.length;i++)
            {
                if(object.getId().equals(len[i]))
                   break;
                if((i+1)==len.length)
                {
                    notifId+=","+object.getId();
                    saveNotifMsgCount(notifId, notifCount + 1);
                }
            }
        }
       // SharedPreferences sharedPreferences=context.getSharedPreferences("User",context.MODE_PRIVATE);
        //return sharedPreferences.getInt("notifCount",0);
    }

    private void saveNotifMsgCount(String notifId,int notifCount)
    {
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("notifId",notifId);
        editor.putInt("notifCount",notifCount);
        editor.commit();
    }

    public void updateNotifListData(List<NotifInfo> list)
    {
        this.list=list;
        notifyDataSetChanged();
    }
}
