package com.example.duy.demotab.Other;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.duy.demotab.Fragment.HinhAnh;
import com.example.duy.demotab.Fragment.TabAdapter;
import com.example.duy.demotab.GiaoDienChat.ChatFragment;
import com.example.duy.demotab.GiaoDienDanhSachThietBiCungMang.ListOnlineFragment;
import com.example.duy.demotab.R;
import com.example.duy.demotab.Storage.AppDatabase;
import com.example.duy.demotab.Storage.User;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.net.wifi.p2p.WifiP2pConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashBoard extends AppCompatActivity implements SendData{

    private ChatFragment chatFragment;
    private boolean checkInfoRegister=true;

    private Toolbar toolbar;
    private MaterialSearchView searchView;

    private GridView grTab;
    ArrayList<HinhAnh> arrayHinh;
    TabAdapter adapter;
    private FragmentTransaction transaction;
    private Fragment fragment=null;

    private String urlGetdata = "https://andrp2p.000webhostapp.com/plattform/getdata.php";
    private AppDatabase appDatabase;

    public String getPhoneName() {
        String deviceName = WiFiDirectBroadcastReceiver.getWifiDeviceName();
        System.out.println("device "+ deviceName);
        return deviceName;
    }

    private void GetData(String url){
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        if(object.getString("Model").compareTo(getPhoneName()) != 0){
//                            appDatabase.userDao().addUser(new User(object.getString("Model"), object.getString("Name"), object.getInt("Age"), object.getInt("Gender"), R.drawable.icon))
                            System.out.println("user avatar "+ object.getString("Ava"));
                            appDatabase.userDao().addUser(new User(object.getString("Model"), object.getString("Name"), object.getInt("Age"), object.getInt("Gender"), object.getString("Ava")));
                        }
                        //arrayUser.add(new User(object.getString("Model"), object.getString("Name"), object.getInt("Age"), object.getInt("Gender")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashBoard.this, "Lỗi load data !",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dadboard);

        appDatabase = AppDatabase.getDatabase(getApplicationContext());
//        if (appDatabase.userDao().getAllUser().get(0).id!="") {
//            System.out.println("init user [0] " + appDatabase.userDao().getAllUser().get(0).id);
//        }
        //clear old data
        appDatabase.userDao().ClearUser();
//        System.out.println("user [0] after clear data "+appDatabase.userDao().getAllUser().get(0).id);
        GetData(urlGetdata);
//        System.out.println("user [0] after get from server"+appDatabase.userDao().getAllUser().get(0).id);

        //https://www.youtube.com/watch?v=FZfjWXYm80k
//        toolbar=(Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Hello");
//        toolbar.setSubtitle("hello");
        //toolbar.setNavigationIcon(R.drawable.arrow);
//        toolbar.setLogo(R.drawable.arrow);
//        searchView=(MaterialSearchView)findViewById(R.id.search_view);

        grTab=(GridView)findViewById(R.id.grTab);
        arrayHinh= new ArrayList<>();

//        arrayHinh.add(new HinhAnh("hinh1",R.drawable.message));
        arrayHinh.add(new HinhAnh("hinh3",R.drawable.people));
        arrayHinh.add(new HinhAnh("hinh2",R.drawable.information4));

        adapter=new TabAdapter(getApplicationContext(),R.layout.dong_tab,arrayHinh);
        grTab.setAdapter(adapter);
        //mac dinh vo cai ben dui
        transaction= getFragmentManager().beginTransaction();
        fragment=new UpdateInfomationFragment();
        transaction.replace(R.id.frLayout,fragment);
        transaction.commit();
        //chon tab thay doi trang
        grTab.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(checkInfoRegister==true){
                    transaction=getFragmentManager().beginTransaction();
                    //check tab name, i: tab position
                    System.out.println("user [0] "+deviceNameArray==null&&i==0 && appDatabase.userDao().getAllUser().size()!=0);
                    if((deviceNameArray==null || appDatabase.userDao().getAllUser().size()==0)&& i==0){
                        i=-1;
                        Toast.makeText(DashBoard.this, "Hiện tại chưa có thiết bị nào gần bạn!", Toast.LENGTH_SHORT).show();
                    }
                    if(i>=0){
                        switch (i){

                            case 0:
                                Bundle bundle=new Bundle();
                                bundle.putStringArray("LIST_ONLINE",deviceNameArray);
                                fragment=new ListOnlineFragment();
                                fragment.setArguments(bundle);
                                break;
                            case 1:
                                fragment=new UpdateInfomationFragment();
                                break;
                        }
                        transaction.replace(R.id.frLayout,fragment);
                        transaction.commit();
                    }
                }
                else {
                    Toast.makeText(DashBoard.this, "Vui long dang ky thong tin truoc khi su dung", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //---------------------------------
        initialWork();
        exqListener();
        //----------------------------------

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem item=menu.findItem(R.id.search);
        searchView.setMenuItem(item);
        return true;

    }

    @Override
    public void SendDataTypeInt(int pos) {
      transaction=getFragmentManager().beginTransaction();
      chatFragment=new ChatFragment();
      transaction.replace(R.id.frLayout,chatFragment);
      transaction.commit();

      final WifiP2pDevice device=deviceArray[pos];
      WifiP2pConfig config=new WifiP2pConfig();
      config.deviceAddress=device.deviceAddress;
      mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
          @Override
          public void onSuccess() {
              Toast.makeText(getApplicationContext(), "Connected to " + device.deviceName, Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onFailure(int i) {
              Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_SHORT).show();
          }
      });

    }

    @Override
    public void SendDataTypeString(String mess) {
        String msg = mess;
        sendReceive.write(msg.getBytes());

    }

    @Override
    public void CheckInfomation(boolean value) {
        if(value==true){
            checkInfoRegister=true;
        }
    }

    @Override
    public void ChooseUserId(String userId) {
        chatFragment.SendDataUserId(userId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //----------------------------------------------------------------------


    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;

    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    List<WifiP2pDevice> peers=new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray=null;
    WifiP2pDevice[] deviceArray;

    static final int MESSAGE_READ=1;

    ServerClass serverClass;
    ClientClass clientClass;
    SendReceive sendReceive;

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences myFile = getSharedPreferences("myFile1",Activity.MODE_PRIVATE);
        if ( (myFile != null) && !(myFile.contains("Name")) ) {
            checkInfoRegister=false;

            transaction=getFragmentManager().beginTransaction();
            fragment=new UpdateInfomationFragment();
            transaction.replace(R.id.frLayout,fragment);
            transaction.commit();

            Toast.makeText(this, "please update your infomation", Toast.LENGTH_SHORT).show();
        }
    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what)
            {
                case MESSAGE_READ:
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempMsg=new String(readBuff,0,msg.arg1);
                    chatFragment.SendDataTypeString(tempMsg);
                    break;
            }
            return true;
        }
    });


    private void exqListener() {
                if(!wifiManager.isWifiEnabled())
                {
                    wifiManager.setWifiEnabled(true);
                }


                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(DashBoard.this, "Discovery Started", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int i) {
                                Toast.makeText(DashBoard.this, "Discovery Starting Failed", Toast.LENGTH_SHORT).show();
                            }
                });

    }

    public void disconnect() {
        if (mManager != null && mChannel != null) {
            mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
                @Override
                public void onGroupInfoAvailable(WifiP2pGroup group) {
                    if (group != null && mManager != null && mChannel != null
                            && group.isGroupOwner()) {
                        mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {

                            @Override
                            public void onSuccess() {
//                                Log.d(TAG, "removeGroup onSuccess -");
                            }

                            @Override
                            public void onFailure(int reason) {
//                                Log.d(TAG, "removeGroup onFailure -" + reason);
                            }
                        });
                    }
                }
            });
        }
    }


    private void initialWork() {

        wifiManager= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mManager= (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel=mManager.initialize(this,getMainLooper(),null);

        mReceiver=new WiFiDirectBroadcastReceiver(mManager, mChannel,this);

        mIntentFilter=new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    WifiP2pManager.PeerListListener peerListListener=new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if(!peerList.getDeviceList().equals(peers))
            {

                peers.clear();
                peers.addAll(peerList.getDeviceList());

                deviceNameArray=new String[peerList.getDeviceList().size()];
                deviceArray=new WifiP2pDevice[peerList.getDeviceList().size()];
                int index=0;

                for(WifiP2pDevice device : peerList.getDeviceList())
                {
                    deviceNameArray[index]=device.deviceName;
                    deviceArray[index]=device;

                    Toast.makeText(DashBoard.this, deviceNameArray[index], Toast.LENGTH_SHORT).show();
                    index++;
                }

            }

            if(peers.size()==0)
            {
                Toast.makeText(getApplicationContext(),"No Device Found",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };

    WifiP2pManager.ConnectionInfoListener connectionInfoListener=new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress groupOwnerAddress=wifiP2pInfo.groupOwnerAddress;

            if(wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner)
            {
//                connectionStatus.setText("Host");
                serverClass=new ServerClass();
                serverClass.start();
                System.out.println("serverCreated");
            }else if(wifiP2pInfo.groupFormed)
            {
//                connectionStatus.setText("Client");
                clientClass=new ClientClass(groupOwnerAddress);
                clientClass.start();
                System.out.println("clientCreated");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(!wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(true);
        }
        registerReceiver(mReceiver,mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mManager.cancelConnect(mChannel,new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(DashBoard.this, "Discovery Started", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(int i) {
//                Toast.makeText(DashBoard.this, "Discovery Starting Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//        this.disconnect();
        unregisterReceiver(mReceiver);
//        if(wifiManager.isWifiEnabled())
//        {
//            wifiManager.setWifiEnabled(false);
//        }
//        wifiManager.setWifiEnabled(true);

//        finish();
    }


    public class ServerClass extends Thread{
        Socket socket;
        ServerSocket serverSocket;

        @Override
        public void run() {
            try {
                serverSocket=new ServerSocket(8888);
                socket=serverSocket.accept();
                sendReceive=new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendReceive extends Thread{
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public SendReceive(Socket skt)
        {
            socket=skt;
            try {
                inputStream=socket.getInputStream();
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            byte[] buffer=new byte[1024];
            int bytes;

            while (socket!=null)
            {
                try {
                    bytes=inputStream.read(buffer);
                    if(bytes>0)
                    {
                        handler.obtainMessage(MESSAGE_READ,bytes,-1,buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ClientClass extends Thread{
        Socket socket;
        String hostAdd;

        public  ClientClass(InetAddress hostAddress)
        {
            hostAdd=hostAddress.getHostAddress();
            socket=new Socket();
        }

        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(hostAdd,8888),500);
                sendReceive=new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
