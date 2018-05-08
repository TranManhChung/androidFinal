package com.example.duy.demotab.GiaoDienChat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
//    private Bitmap ownerAvatar;
//    private Bitmap responAvatar;

    private String ownerAvatar="";
    private String responAvatar="";

    public ChatAdapter(int layout, Context context, List<Message> data, String ownerAvatar, String responserAvatar) {
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
        private ImageView leftMessImage;

        private ImageView rightImageView;
        private TextView rightTextView;
        private ImageView rightMessImage;
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
            holder.rightMessImage=(ImageView)view.findViewById(R.id.rightMessImage);

            holder.leftImageView=(ImageView)view.findViewById(R.id.imgAvatarLeft);
            holder.leftTextView=(TextView)view.findViewById(R.id.txtMessageLeft);
            holder.leftMessImage=(ImageView)view.findViewById(R.id.leftMessImage);


            view.setTag(holder);
        }
        else {
            holder= (ViewHolder) view.getTag();
        }

        holder.rightImageView.setVisibility(View.GONE);
        holder.rightTextView.setVisibility(View.GONE);
        holder.rightMessImage.setVisibility(View.GONE);
        holder.leftImageView.setVisibility(View.GONE);
        holder.leftTextView.setVisibility(View.GONE);
        holder.leftMessImage.setVisibility(View.GONE);

        if(data.get(i).getName().equals("Toi")!=true){
            holder.leftImageView.setVisibility(View.VISIBLE);//avatar

            if(data.get(i).getType()==true){//Tin nhan la hinh
                holder.leftMessImage.setVisibility(View.VISIBLE);
                //Chuyen mess tu string sang bitmap
                Bitmap image=StringToBitMap(data.get(i).getMessage());
                holder.leftMessImage.setImageBitmap(image);
            }
            else{//Tinh nhan la text
                holder.leftTextView.setVisibility(View.VISIBLE);
                holder.leftTextView.setText(data.get(i).getMessage());
            }
            if (!ownerAvatar.equals("")) {
                Glide
                        .with(context)
                        .load(ownerAvatar)
                        .into(holder.leftImageView);
            }
            else holder.leftImageView.setImageResource(R.drawable.icon);
        }
        else {

            holder.rightImageView.setVisibility(View.VISIBLE);//avatar

            if(data.get(i).getType()==true){//Tin nhan la hinh
                holder.rightMessImage.setVisibility(View.VISIBLE);
                //Chuyen mess tu string sang bitmap
                Bitmap image=StringToBitMap(data.get(i).getMessage());
                holder.rightMessImage.setImageBitmap(image);
            }
            else{//Tinh nhan la text
                holder.rightTextView.setVisibility(View.VISIBLE);
                holder.rightTextView.setText(data.get(i).getMessage());
            }

            holder.rightTextView.setText(data.get(i).getMessage());
            if (!responAvatar.equals("")) {
                Glide
                        .with(context)
                        .load(responAvatar)
                        .into(holder.rightImageView);
            }
            else holder.rightImageView.setImageResource(R.drawable.icon);
        }

        return view;
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
