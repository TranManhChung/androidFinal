package com.example.duy.demotab;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.duy.demotab.Other.DashBoard;

public class SplashScreen extends AppCompatActivity {
    //Button btnDangKy;
    WifiManager wifiManager;

    private boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if( wifiInfo.getNetworkId() == -1 ){
                return false; // Not connected to an access point
            }
            return true; // Connected to an access point
        }
        else {
            return false; // Wi-Fi adapter is OFF
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        Thread myThread= new Thread() {
            @Override
            public void run() {
                try {
                    //check if wifi is connected
                    //if not then just sleep for  second
                    do {
                        sleep(3000);
                    }while (!checkWifiOnAndConnected());

                    Intent intent = new Intent(SplashScreen.this, DashBoard.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        myThread.start();

    }



    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
