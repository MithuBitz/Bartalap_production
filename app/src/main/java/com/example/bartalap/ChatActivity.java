package com.example.bartalap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.example.bartalap.Chat.ChatObject;
import com.example.bartalap.Chat.MessageAdapter;
import com.example.bartalap.Chat.MessageObject;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mChat;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    ArrayList<MessageObject> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        messageList = new ArrayList<>();
        mChat = findViewById(R.id.message);
        mChat.setNestedScrollingEnabled(false);
        mChat.setHasFixedSize(false);

        mChatLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false);
        mChat.setLayoutManager(mChatLayoutManager);

        mChatAdapter = new MessageAdapter(messageList);
        mChat.setAdapter(mChatAdapter);
    }
}