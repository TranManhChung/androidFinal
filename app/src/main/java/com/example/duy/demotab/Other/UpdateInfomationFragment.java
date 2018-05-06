package com.example.duy.demotab.Other;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import com.example.duy.demotab.R;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class UpdateInfomationFragment extends Fragment {

    private ImageView imgAvatar;
    private EditText edtName;
    private EditText edtAge;
    private RadioButton rdMale;
    private RadioButton rdFemale;
    private Button btnUpdate;
    private View view;
    private String urlUpdate = "https://andrp2p.000webhostapp.com/plattform/update.php";
    int REQUEST_CODE_CAMERA =123;

    private SendData sendData;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        sendData= (SendData) getActivity();

        view=inflater.inflate(R.layout.activity_change_infomation,container,false);
        Toast.makeText(getActivity(), Build.MODEL, Toast.LENGTH_SHORT).show();

        AnhXa();
        GetAvailableInfomationAndShow();

        //thay đổi avata
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,REQUEST_CODE_CAMERA );
            }
        });

        Handle();
        return view;
    }

    private void GetAvailableInfomationAndShow() {
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
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    myEditor.commit();
                    UpdateInfo(urlUpdate);

                    sendData.CheckInfomation(true);
                }
                else {
                    Toast.makeText(getActivity(), "Bạn điền thông tin chưa đầy đủ", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                params.put("modelUser", Build.MODEL.toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    void AnhXa(){
        imgAvatar=(ImageView)view.findViewById(R.id.imgAvatarInfomation);
        edtName=(EditText)view.findViewById(R.id.edtNameInfomation);
        edtAge=(EditText)view.findViewById(R.id.edtAgeInfomation);
        rdMale=(RadioButton)view.findViewById(R.id.rdMaleInfomation);
        rdFemale=(RadioButton)view.findViewById(R.id.rdFemaleInfomation);
        btnUpdate=(Button)view.findViewById(R.id.btnUpdateInfomation);
    }
}
