package com.example.duy.demotab.GiaoDienDanhSachThietBiCungMang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duy.demotab.R;
import com.example.duy.demotab.Storage.User;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Tran Manh Chung on 4/24/2018.
 */

public class OnlineAdapter extends BaseAdapter {
    private int layout;
    private Context context;
    private List<User> data;

    public OnlineAdapter(int layout, Context context, List<User> data) {
        this.layout = layout;
        this.context = context;
        this.data = data;
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
        private ImageView imgOnline;
        private TextView txtName;
        private TextView txtAgeOnline;
        private TextView txtSexOnline;
    }
    ViewHolder holder;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){

            holder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(layout,null);
            //anh xa
            holder.imgOnline=(ImageView)view.findViewById(R.id.imgOnline);
            holder.txtName=(TextView)view.findViewById(R.id.txtNameOnline);
            holder.txtAgeOnline=(TextView)view.findViewById(R.id.txtAgeOnline);
            holder.txtSexOnline=(TextView)view.findViewById(R.id.txtSexOnline);

            view.setTag(holder);
        }
        else {
            holder= (ViewHolder) view.getTag();
        }

        //set value from data
//        System.out.println("data" + data.get(1).name);
        if (data!=null) {
            holder.txtSexOnline.setText( data.get(i).sex == 1 ? "Nữ" : "Nam");
            holder.txtAgeOnline.setText(Integer.toString(data.get(i).age));
            holder.txtName.setText(data.get(i).name);
            URL url = null;
            Bitmap bmp = null;
            try {
                url = new URL(data.get(i).avatar);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bmp!=null) {
                holder.imgOnline.setImageBitmap(bmp);
            }
            else holder.imgOnline.setImageResource(R.drawable.icon);
        }
        else {
            //default value;
            holder.txtSexOnline.setText("Nam");
            holder.txtAgeOnline.setText("21 tuoi");
            holder.txtName.setText("Trần Mạnh Chung");
            holder.imgOnline.setImageResource(R.drawable.icon);
        }
        return view;
    }
}
