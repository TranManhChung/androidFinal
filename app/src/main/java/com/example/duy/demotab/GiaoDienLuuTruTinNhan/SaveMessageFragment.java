package com.example.duy.demotab.GiaoDienLuuTruTinNhan;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.duy.demotab.R;
import com.example.duy.demotab.Other.SendData;
import com.example.duy.demotab.Storage.AppDatabase;
import com.example.duy.demotab.Storage.ChatHistory;
import com.example.duy.demotab.Storage.User;

import java.util.ArrayList;
import java.util.List;

public class SaveMessageFragment extends ListFragment {

    private ListView listView;
    private HistoryAdapter adapter;
    private List<ChatHistory>data;
    private List<User> users;
    private View view;
    private SendData sendData;

    AppDatabase appDatabase;
    ChatHistory chatHistory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        //load database for app
        appDatabase = AppDatabase.getDatabase(getActivity());

        sendData= (SendData) getActivity();

        view=inflater.inflate(R.layout.fragment_save_message,container,false);
        data = appDatabase.chatHistoryDao().getListChatHistory()!= null ? appDatabase.chatHistoryDao().getListChatHistory() : null;
        users = appDatabase.userDao().getAllUser();

        if (data == null) data=new ArrayList<>();
        adapter=new HistoryAdapter(R.layout.layout_history,getActivity(),data, users);
        setListAdapter(adapter);

        return view;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
//        if (users.get(position).isOnline == 1) {
            sendData.SendDataTypeInt(position);
            sendData.ChooseUserId(users.get(position).id);
//        }
//        else {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setMessage("Cannot connect to an ")
//        }
    }
}
