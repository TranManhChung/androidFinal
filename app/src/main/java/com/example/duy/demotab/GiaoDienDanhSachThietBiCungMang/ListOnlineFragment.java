package com.example.duy.demotab.GiaoDienDanhSachThietBiCungMang;

import android.app.ListFragment;
import android.bluetooth.BluetoothAdapter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.duy.demotab.Other.SendData;
import com.example.duy.demotab.Other.WiFiDirectBroadcastReceiver;
import com.example.duy.demotab.R;
import com.example.duy.demotab.Storage.AppDatabase;
import com.example.duy.demotab.Storage.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tran Manh Chung on 4/24/2018.
 */

public class ListOnlineFragment extends ListFragment {
    private View view;
    private OnlineAdapter adapter;
//    private ArrayAdapter adapter;
    private SendData sendData;
    private User user;
    private AppDatabase appDatabase;

    private List<User> onlineUsers;

    public String getPhoneName() {
        String deviceName = WiFiDirectBroadcastReceiver.getWifiDeviceName();
        System.out.println("device "+ deviceName);
        return deviceName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Bundle bundle=getArguments();
//        String temp[]=bundle.getStringArray("LIST_ONLINE");

//        System.ou t.println("temp "+ temp[0]);

        sendData= (SendData) getActivity();
        view=inflater.inflate(R.layout.fragment_list_online,container,false);

        //fake data for users
        appDatabase = AppDatabase.getDatabase(getActivity());

//        GetData(urlGetdata);
        //GetUser(urlGetdata);
        onlineUsers = appDatabase.userDao().getUserOnline();
//        onlineUsers = new ArrayList<>();

        adapter=new OnlineAdapter(R.layout.layout_list_online,getActivity(),onlineUsers);
        setListAdapter(adapter);

//        for (String userId : temp) {
//            System.out.println("userId "+userId);
//            for (int i= 0; i< users.size(); i++) {
//                System.out.println("userId indentity "+users.get(i).id);
//                if (users.get(i).id.equals(userId)) {
//
//                    onlineUsers.add(users.get(i));
////                    System.out.println("user " + );
//                }
//            }
//        }

        adapter.notifyDataSetChanged();

//        adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,temp);
//        setListAdapter(adapter);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        sendData.SendDataTypeInt(position);
        sendData.ChooseUserId(onlineUsers.get(position).id);
    }

    private void GetUser(String url){
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Lỗi!",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);

    }

}
