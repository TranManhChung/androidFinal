package com.example.duy.demotab.Other;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

/**
 * Created by lenovo on 21-02-2018.
 */

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
    private static String thisDeviceName;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
//    private String thisDeviceName = "";
//    private MainActivity mActivity;
    private DashBoard mDadboard;

    public WiFiDirectBroadcastReceiver(WifiP2pManager mManager, WifiP2pManager.Channel mChannel, DashBoard dadboard)
    {
        this.mManager = mManager;
        this.mChannel = mChannel;
        this.mDadboard = dadboard;
    }

    public static String getWifiDeviceName() {
        return thisDeviceName;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        //get device wifi name
//        WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
        String action = intent.getAction();

        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            int state=intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);

            if(state==WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                Toast.makeText(context,"Wifi is ON",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context,"Wifi is OFF",Toast.LENGTH_SHORT).show();
            }
        }else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
            //do something
            if(mManager!=null)
            {
                mManager.requestPeers(mChannel,mDadboard.peerListListener);
            }
        }else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            //do something
            if(mManager==null)
            {
                return;
            }

            NetworkInfo networkInfo=intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if(networkInfo.isConnected())
            {
                mManager.requestConnectionInfo(mChannel,mDadboard.connectionInfoListener);
            }else {
//                mDadboard.connectionStatus.setText("Device Disconnected");
            }
        }else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            //get wifi device name
            WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            System.out.println("devicenAME "+ device.deviceName);
            thisDeviceName = device.deviceName;
        }

    }
}
