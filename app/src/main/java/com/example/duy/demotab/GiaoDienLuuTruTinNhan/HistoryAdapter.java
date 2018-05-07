package com.example.duy.demotab.GiaoDienLuuTruTinNhan;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.duy.demotab.GiaoDienChat.Message;
import com.example.duy.demotab.R;
import com.example.duy.demotab.Storage.ChatHistory;
import com.example.duy.demotab.Storage.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tran Manh Chung on 4/23/2018.
 */

public class HistoryAdapter extends BaseAdapter {
    private int layout;
    private Context context;
    private List<ChatHistory> data;
    private List<User> users;

    public HistoryAdapter(int layout, Context context, List<ChatHistory> data, List<User> users) {
        this.layout = layout;
        this.context = context;
        this.data = data;
        this.holder = holder;
        this.users = users;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        private ImageView imgAvatar;
        private TextView txtTime;
        private TextView txtNameUser;
        private TextView txtBriefMessage;
    }

    ViewHolder holder;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null){
            holder=new ViewHolder();

            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(layout,null);
            holder.imgAvatar=(ImageView)view.findViewById(R.id.imgAvatarHistory);
            holder.txtBriefMessage=(TextView)view.findViewById(R.id.txtBriefMessage);
            holder.txtTime=(TextView)view.findViewById(R.id.txtTimeHistory);
            holder.txtNameUser=(TextView)view.findViewById(R.id.txtUserNameHistory);

            view.setTag(holder);
        }
        else {
            holder= (ViewHolder) view.getTag();
        }
        //set value from data

        String userId = data.get(i).userId;
//        holder.imgAvatar.setImageResource(users.get(i).avatar);
        holder.txtNameUser.setText(users.get(i).name);
        holder.txtTime.setText(data.get(i).getTime());
        //lastest message
        holder.txtBriefMessage.setText(processFromNoramlMessageToBriefMessage(data.get(i).getLatestMessage()));
        return view;
    }
    private String processFromNoramlMessageToBriefMessage(String message){
        if(message.length()>30){
           return message.substring(0,30)+"...";
        }
        return message;
    }
}
