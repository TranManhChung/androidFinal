package com.example.duy.demotab.Other;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.duy.demotab.R;
import com.example.duy.demotab.Storage.AppDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class
UpdateInfomationFragment extends Fragment {

    private ImageView imgAvatar, imgCamera, imgGallery;
    private EditText edtName;
    private EditText edtAge;
    private RadioButton rdMale;
    private RadioButton rdFemale;
    private Button btnUpdate;
    private View view;
    private boolean flag;
    private String urlUpdate = "https://andrp2p.000webhostapp.com/plattform/update.php";
    private String urlInsert = "https://andrp2p.000webhostapp.com/plattform/insert.php";
    int REQUEST_CODE_CAMERA =123;
    private static final int GALLERY_REQUEST=1;

    //firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    private AppDatabase appDatabase;

    //avatar
    private Uri filePath;
    private String imgUrl="";


    private SendData sendData;

    public String getPhoneName() {
        String deviceName = WiFiDirectBroadcastReceiver.getWifiDeviceName();
        System.out.println("device "+ deviceName);
        return deviceName;
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ getPhoneName());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
//                            System.out.println("url: "+ taskSnapshot.getDownloadUrl());
                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            imgUrl = downloadUrl.toString();
//                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        sendData= (SendData) getActivity();

        appDatabase = AppDatabase.getDatabase(getActivity());
        //firebase
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        view=inflater.inflate(R.layout.activity_change_infomation,container,false);
        Toast.makeText(getActivity(), this.getPhoneName(), Toast.LENGTH_SHORT).show();

        AnhXa();


        if (checkWifiOnAndConnected()) {
            btnUpdate.setEnabled(true);
        }
        flag = GetAvailableInfomationAndShow();



        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
            }
        });


        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });




        Handle();
        return view;
    }

    private Boolean GetAvailableInfomationAndShow() {
        SharedPreferences myFile = getActivity().getSharedPreferences("myFile1",Activity.MODE_PRIVATE);
        if( (myFile != null) && (myFile.contains("Name"))){
            String temp = myFile.getString("Name", "***");
            edtName.setText(temp);
            temp = myFile.getString("Age", "***");
            edtAge.setText(temp);
            temp = myFile.getString("Sex", "***");
            if(temp.equals("Nam")==true){
                rdMale.setChecked(true);
            }
            else {
                rdFemale.setChecked(true);
            }
            String avatarOwner = myFile.getString("avatarUrl", "");
            if (!avatarOwner.equals("")) {
                Glide
                        .with(getActivity().getApplicationContext())
                        .load(avatarOwner)
                        .into(imgAvatar);
            }
            return true;
        }
        return false;

    }



    private boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            filePath = data.getData();
            Uri imageUri = data.getData(); //Lấy ra uri của image
            imgAvatar.setImageURI(imageUri); //Set image lựa chọn theo uri
            uploadImage();

        }
        if(requestCode==REQUEST_CODE_CAMERA &&resultCode==RESULT_OK&&data!=null){
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            imgAvatar.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void Handle() {

        if(edtName.getText().toString().length()>0&&edtAge.getText().toString().length()>0&&
                (rdFemale.isChecked()==true||rdMale.isChecked()==true)){
            btnUpdate.setEnabled(true);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtName.getText().toString().length()>0&&edtAge.getText().toString().length()>0&&
                        (rdFemale.isChecked()==true||rdMale.isChecked()==true)){
                    SharedPreferences myFile1 = getActivity().getSharedPreferences("myFile1",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor myEditor = myFile1.edit();
                    String temp = edtName.getText().toString();
                    myEditor.putString("Name", temp);
                    myEditor.commit();
                    temp=edtAge.getText().toString();
                    myEditor.putString("Age", temp);
                    myEditor.commit();

                    temp="Nam";
                    if(rdFemale.isChecked()){
                        temp="Nữ";
                    }
                    myEditor.putString("Sex", temp);
                    if (!imgUrl.equals("")) {
                        myEditor.putString("avatarUrl", imgUrl);
                    }
                    myEditor.commit();
                    if (!flag) {
                        AddUser(urlInsert);
                    } else {
                        UpdateInfo(urlUpdate);
                    }



                    sendData.CheckInfomation(true);
//                    btnUpdate.setEnabled(false);
                }
                else {
                    Toast.makeText(getActivity(), "Bạn điền thông tin chưa đầy đủ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AddUser(String url){
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(getActivity(),"Đăng ký thành công!",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(),"Lỗi!",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Lỗi kết nối!",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nameUser", edtName.getText().toString().trim());
                params.put("ageUser", edtAge.getText().toString().trim());
                if (rdMale.isChecked()){
                    params.put("genderUser", "1");  //
                }
                if (rdFemale.isChecked()){
                    params.put("genderUser", "0");
                }
                params.put("modelUser", getPhoneName().toString().trim());
                if (!imgUrl.equals("")) {
                    params.put("avaUser", imgUrl.trim());
                } else params.put("avaUser", "");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void UpdateInfo(String url){
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(getActivity(),"Cập nhật thành công!",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(),"Lỗi!",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Xảy ra lỗi!",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nameUser", edtName.getText().toString().trim());
                params.put("ageUser", edtAge.getText().toString().trim());
                if (rdMale.isChecked()){
                    params.put("genderUser", "1");
                }
                if (rdFemale.isChecked()){
                    params.put("genderUser", "0");
                }
                params.put("modelUser", getPhoneName().trim());
                if (!imgUrl.equals("")) {
                    params.put("avaUser", imgUrl.trim());
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    void AnhXa(){
        imgAvatar=(ImageView)view.findViewById(R.id.imgAvatarInfomation);
        imgCamera=(ImageView)view.findViewById(R.id.imgCamera);
        imgGallery=(ImageView)view.findViewById(R.id.imgGallery);
        edtName=(EditText)view.findViewById(R.id.edtNameInfomation);
        edtAge=(EditText)view.findViewById(R.id.edtAgeInfomation);
        rdMale=(RadioButton)view.findViewById(R.id.rdMaleInfomation);
        rdFemale=(RadioButton)view.findViewById(R.id.rdFemaleInfomation);
        btnUpdate=(Button)view.findViewById(R.id.btnUpdateInfomation);
    }
}
