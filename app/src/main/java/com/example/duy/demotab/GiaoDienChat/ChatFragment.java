package com.example.duy.demotab.GiaoDienChat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by DUY on 5/4/2018.
 */

public class ChatFragment extends Fragment implements View.OnClickListener,SendDataFromActToFragment {
    private View view;
    private SendData sendData;
    private String id = "";

    private String ownerAvatarUrl = "";
    private String respondAvatarUrl = "";
    private Bitmap ownerBitmap = null;
    private Bitmap responBitmap = null;
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
        try {
            Bitmap ownerBitmaptemp = Glide.
                    with(getActivity()).
                    asBitmap().
                    load(ownerAvatarUrl).
                    into(-1, -1). // Width and height
                    get();
            ownerBitmap = ownerBitmaptemp;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            Bitmap responBitmaptemp = Glide.
                        with(getActivity()).
                        asBitmap().
                        load(respondAvatarUrl).
                        into(-1, -1). // Width and height
                        get();
            responBitmap = responBitmaptemp;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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
        adapter=new ChatAdapter(R.layout.layout_chat,getActivity(),data, ownerBitmap, responBitmap);
        listView.setAdapter(adapter);

        button.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        data.add(new Message(R.drawable.icon, "idCuaToi", "Toi", editText.getText().toString()));
        this.SaveChatLog();
        adapter.notifyDataSetChanged();
        sendData.SendDataTypeString(editText.getText().toString());

    }

    @Override
    public void SendDataTypeString(String mess) {
        data.add(new Message(R.drawable.icon,id,"Ai",mess));
        this.SaveChatLog();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void SendDataUserId(String id) {
        this.id = id;
    }
}
