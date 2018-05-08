package com.example.duy.demotab.GiaoDienChat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.duy.demotab.Other.SendData;
import com.example.duy.demotab.Other.SendDataFromActToFragment;
import com.example.duy.demotab.R;
import com.example.duy.demotab.Storage.AppDatabase;
import com.example.duy.demotab.Storage.ChatHistory;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by DUY on 5/4/2018.
 */

public class ChatFragment extends Fragment implements View.OnClickListener,SendDataFromActToFragment {
    private View view;
    private SendData sendData;
    private String id = "";
    private Button btnAdd;

    private String ownerAvatarUrl = "";
    private String respondAvatarUrl = "";
    private Bitmap ownerBitmap = null;
    private Bitmap responBitmap = null;
    private static final int GALLERY_REQUEST=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        sendData= (SendData) getActivity();
        view=inflater.inflate(R.layout.fragment_chat,container,false);

        appDatabase = AppDatabase.getDatabase(getActivity().getApplicationContext());

        //bây giờ chưa có cơ sở dữ liệu nên dùng itent gởi message qua, nhưng sau này có rồi thì chỉ gởi id qua thôi
//        Intent intent=getIntent();
//        id=intent.getStringExtra("ID");

        //fake user id for testing
//        id = "saya";

        AnhXa();
        Handle();

        return view;
    }

//    // rubbish
//    private Button buttonRub;
//    private EditText editTextRub;

    private ListView listView;
    private EditText editText;
    private Button button;
    private List<Message> data;
    private ChatAdapter adapter;
    //storage
    private ChatHistory chatHistory;
    private AppDatabase appDatabase;

    void AnhXa(){
        listView=(ListView)view.findViewById(R.id.listView);
        editText=(EditText)view.findViewById(R.id.edtMessage);
        button=(Button)view.findViewById(R.id.btnSend);
        btnAdd=(Button)view.findViewById(R.id.btnAdd);

//        buttonRub=(Button)view.findViewById(R.id.btnSendRub);
//        editTextRub=(EditText)view.findViewById(R.id.edtMessageRub);

        //get chat log by user id
        List<ChatHistory> listChatLogs = appDatabase.chatHistoryDao().getListChatHistory();
        if (listChatLogs.size() > 0) {
            for (ChatHistory ch : listChatLogs) {
                System.out.println(id +" "+ ch.userId);
                if (id.equals(ch.userId)) {
                    chatHistory = ch;
                }
            }
        }
        data = chatHistory != null ?
                chatHistory.getChatLog() :
                new ArrayList<Message>();
        GetOwnerChatAvatar();
        respondAvatarUrl = appDatabase.userDao().getUser(id)!=null ? appDatabase.userDao().getUser(id).avatar : "";
    }
    void Handle() {
//        Bitmap ownerBitmap = null;
//        Glide.with(getActivity()).asBitmap().load(ownerAvatarUrl).into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//
//            }
//        });

//        Thread thread = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try  {
//                    //Your code goes here
//                    URL url = new URL(ownerAvatarUrl);
//                    ownerBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    URL url2 = new URL(respondAvatarUrl);
//                    responBitmap = BitmapFactory.decodeStream(url2.openConnection().getInputStream());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        thread.start();
//        adapter=new ChatAdapter(R.layout.layout_chat,getActivity(),data, ownerBitmap, responBitmap);
        adapter=new ChatAdapter(R.layout.layout_chat,getActivity(),data, ownerAvatarUrl, respondAvatarUrl);

        listView.setAdapter(adapter);

        button.setOnClickListener(this);

        btnAdd.setOnClickListener(this);
//        buttonRub.setOnClickListener(this);
    }

    private Boolean GetOwnerChatAvatar() {
        SharedPreferences myFile = getActivity().getSharedPreferences("myFile1", Activity.MODE_PRIVATE);
        if( (myFile != null) && (myFile.contains("Name"))){
            String avatarOwner = myFile.getString("avatarUrl", "");
            if (!avatarOwner.equals("")) {
                ownerAvatarUrl = avatarOwner;
                return true;
            }
        }
        return false;

    }

    //call this to store data
    void SaveChatLog() {
        //translate data to json
        Gson gson = new Gson();
        String chatLogString = gson.toJson(data);

        //time of the lastest message
        //fake for testing purpose, change this to archive realtime
        String lastTime = "5h";

        //avoid data conflicted in database
        //if there are no chat exist, create new chat log with user id received
        if (chatHistory == null) {
            chatHistory = new ChatHistory(id, chatLogString, lastTime);
        }
        else chatHistory.insertChatLog(chatLogString); //insert new chat log if chatlog has been created

        System.out.println(chatHistory.userId + " " + id);

        appDatabase.chatHistoryDao().insertChatHistory(chatHistory);
    }

    boolean checkMessIsTextOrImage;//true - hinh/false - text

    @Override
    public void onClick(View view) {//gui tin nhan di
        switch (view.getId()){
            case R.id.btnSend:
                if(checkMessIsTextOrImage==true){//hinh
                    data.add(new Message(R.drawable.icon, "idCuaToi", "Toi",bitmapMess,true));
                    //Chuyen hinh tu bitmap sang string
                    sendData.SendDataTypeString(editText.getText().toString());
                }
                else{//text
                    data.add(new Message(R.drawable.icon, "idCuaToi", "Toi", editText.getText().toString(),false));
                    this.SaveChatLog();
                    sendData.SendDataTypeString(editText.getText().toString());
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.btnAdd:
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
                break;
        }

    }
    String bitmapMess="";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            try {
                Uri imageUri = data.getData(); //Lấy ra uri của image
                Bitmap bit = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                bitmapMess=BitMapToString(bit);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        if(requestCode==REQUEST_CODE_CAMERA &&resultCode==RESULT_OK&&data!=null){
//            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
//            imgAvatar.setImageBitmap(bitmap);
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    @Override
    public void SendDataTypeString(String mess) {//nhan tin nhan va show ra

        if(mess.length()>100){//la hinh
            data.add(new Message(R.drawable.icon,id,"Ai",mess,true));
        }
        else {
            data.add(new Message(R.drawable.icon,id,"Ai",mess,false));
            this.SaveChatLog();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void SendDataUserId(String id) {
        this.id = id;
    }
}
