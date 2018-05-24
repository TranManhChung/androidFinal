package com.example.duy.demotab.GiaoDienChat;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.duy.demotab.R;
import com.example.duy.demotab.Storage.User;

import org.w3c.dom.Text;

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
    private User responser = null;

    public ChatAdapter(int layout, Context context, List<Message> data, String ownerAvatar, String responserAvatar, User responser) {
        this.layout=layout;
        this.context = context;
        this.data = data;
        this.ownerAvatar = ownerAvatar;
        this.responAvatar = responserAvatar;
        this.responser = responser;
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
        private TextView rightTextView, txtNamePeople;
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
            holder.txtNamePeople=(TextView) view.findViewById(R.id.txtNamePeople);




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
            if (!ownerAvatar.equals("")) {
                Glide
                        .with(context)
                        .load(ownerAvatar)
                        .into(holder.rightImageView);
            }
            else holder.rightImageView.setImageResource(R.drawable.icon);
        }
        else {
            holder.rightImageView.setVisibility(View.VISIBLE);
            holder.rightTextView.setVisibility(View.VISIBLE);

            holder.rightTextView.setText(data.get(i).getMessage());
//            holder.rightImageView.setImageResource(data.get(i).getAvatar());
//            holder.leftImageView.setImageBitmap(responAvatar);
            holder.txtNamePeople.setText(responser.name);
            if (!responAvatar.equals("")) {
                Glide
                        .with(context)
                        .load(responAvatar)
                        .into(holder.leftImageView);
            }
            else holder.leftImageView.setImageResource(R.drawable.icon);
        }


        holder.leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog= new Dialog(context);
                dialog.setContentView(R.layout.dialog_custom);
                dialog.setTitle("Thông tin");

                TextView ten=(TextView)dialog.findViewById(R.id.txtName);
                TextView tuoi=(TextView)dialog.findViewById(R.id.txtAge);
                TextView gioiTinh=(TextView)dialog.findViewById(R.id.txtGioiTinh);
                ImageView avt=(ImageView)dialog.findViewById(R.id.imageView);


                ten.setText(responser.name);
                tuoi.setText(responser.age + "");
                gioiTinh.setText(responser.sex == 1 ? "Nam" : "Nữ");
                if (!responser.avatar.equals("")) {
                    Glide
                            .with(context)
                            .load(responAvatar)
                            .into(avt);
                }
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
        return view;
    }
}
