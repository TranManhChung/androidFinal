package com.example.duy.demotab;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.duy.demotab.Other.Dadboard;

public class SplashScreen extends AppCompatActivity {
    //Button btnDangKy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        //Button btnDangKy=(Button)findViewById(R.id.btnDangKy);


        Thread myThread= new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(SplashScreen.this, Dadboard.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        myThread.start();

//        btnDangKy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent= new Intent(SplashScreen.this, formDangKy.class);
//                startActivity(intent);
//            }
//        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
