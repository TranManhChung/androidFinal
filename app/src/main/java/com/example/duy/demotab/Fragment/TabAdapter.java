package com.example.duy.demotab.Fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.duy.demotab.R;

import java.util.List;

/**
 * Created by DUY on 4/30/2018.
 */

public class TabAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<HinhAnh> listHinh;

    public TabAdapter(Context context, int layout, List<HinhAnh> listTab) {
        this.context = context;
        this.layout = layout;
        this.listHinh = listTab;
    }

    @Override
    public int getCount() {
        return listHinh.size();
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
        ImageView imgHinh;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            holder= new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(layout, null);
            holder.imgHinh=(ImageView)view.findViewById(R.id.hinhTab);
            view.setTag(holder);
        }
        else {
            holder= (ViewHolder) view.getTag();
        }

        HinhAnh tab=listHinh.get(i);
        holder.imgHinh.setImageResource(tab.getHinh());

        return view;
    }
}
