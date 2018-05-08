package com.example.duy.demotab.GiaoDienChat;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duy.demotab.R;

import java.util.List;

/**
 * Created by Tran Manh Chung on 4/23/2018.
 */

public class ChatAdapter extends BaseAdapter {
//    private int leftLayout;
//    private int rightLayout;
    private int layout;
    private Context context;
    private List<Message> data;
    private Bitmap ownerAvatar;
    private Bitmap responAvatar;

    public ChatAdapter(int layout, Context context, List<Message> data, Bitmap ownerAvatar, Bitmap responserAvatar) {
       this.layout=layout;
       this.context = context;
       this.data = data;
       this.ownerAvatar = ownerAvatar;
       this.responAvatar = responserAvatar;
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

    class ViewHolder{
        private ImageView leftImageView;
        private TextView leftTextView;
        private ImageView rightImageView;
        private TextView rightTextView;
    }

    private ViewHolder holder;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            holder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view=inflater.inflate(layout,null);
            holder.rightImageView=(ImageView)view.findViewById(R.id.imgAvatarRight);
            holder.rightTextView=(TextView)view.findViewById(R.id.txtMessageRight);
            holder.leftImageView=(ImageView)view.findViewById(R.id.imgAvatarLeft);
            holder.leftTextView=(TextView)view.findViewById(R.id.txtMessageLeft);

            view.setTag(holder);
        }
        else {
            holder= (ViewHolder) view.getTag();
        }

        holder.rightImageView.setVisibility(View.GONE);
        holder.rightTextView.setVisibility(View.GONE);
        holder.leftImageView.setVisibility(View.GONE);
        holder.leftTextView.setVisibility(View.GONE);

        if(data.get(i).getName().equals("Toi")!=true){
            holder.leftImageView.setVisibility(View.VISIBLE);
            holder.leftTextView.setVisibility(View.VISIBLE);

            holder.leftTextView.setText(data.get(i).getMessage());
//            holder.leftImageView.setImageResource(data.get(i).getAvatar());
            holder.leftImageView.setImageBitmap(ownerAvatar);
        }
        else {
            holder.rightImageView.setVisibility(View.VISIBLE);
            holder.rightTextView.setVisibility(View.VISIBLE);

            holder.rightTextView.setText(data.get(i).getMessage());
//            holder.rightImageView.setImageResource(data.get(i).getAvatar());
            holder.leftImageView.setImageBitmap(responAvatar);
        }

        return view;
    }
}
