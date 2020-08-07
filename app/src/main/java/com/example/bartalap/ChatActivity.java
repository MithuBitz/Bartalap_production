package com.example.bartalap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bartalap.Chat.ChatObject;
import com.example.bartalap.Chat.MediaAdapter;
import com.example.bartalap.Chat.MessageAdapter;
import com.example.bartalap.Chat.MessageObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mChat, mMedia;
    private RecyclerView.Adapter mChatAdapter, mMediaAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager, mMediaLayoutManager;

    ArrayList<MessageObject> messageList;

    String chatID;

    DatabaseReference mChatDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatID = getIntent().getExtras().getString("chatID");

        mChatDb = FirebaseDatabase.getInstance().getReference().child("chat").child(chatID);

        Button mSend = findViewById(R.id.send);
        Button mAddMedia = findViewById(R.id.addMedia);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        mAddMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        initializeMessage();
        initializeMedia();

        //Dispaly the recieved message
        getChatMessages();
    }

    private void getChatMessages() {

        mChatDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    String text = "",
                            creatorID = "";

                    if (snapshot.child("text").getValue() != null)
                        text = snapshot.child("text").getValue().toString();
                    if (snapshot.child("creator").getValue() != null)
                        creatorID = snapshot.child("creator").getValue().toString();

                    MessageObject mMessage = new MessageObject(snapshot.getKey(),creatorID, text);
                    messageList.add(mMessage);
                    mChatLayoutManager.scrollToPosition(messageList.size()-1);
                    mChatAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage() {
        EditText mMessage = findViewById(R.id.message);

        if (!mMessage.getText().toString().isEmpty()) {
            DatabaseReference newMessageDb = mChatDb.push();

            Map newMessageMap = new HashMap<>();
            newMessageMap.put("text", mMessage.getText().toString());
            newMessageMap.put("creator", FirebaseAuth.getInstance().getUid());

            newMessageDb.updateChildren(newMessageMap);

        }
        mMessage.setText(null);
    }

    private void initializeMessage() {
        messageList = new ArrayList<>();
        mChat = findViewById(R.id.messageList);
        mChat.setNestedScrollingEnabled(false);
        mChat.setHasFixedSize(false);

        mChatLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false);
        mChat.setLayoutManager(mChatLayoutManager);

        mChatAdapter = new MessageAdapter(messageList);
        mChat.setAdapter(mChatAdapter);
    }

    int PICK_IMAGE_INTENT = 1;
    ArrayList<String> mediaUriList = new ArrayList<>();
    private void initializeMedia() {
        messageList = new ArrayList<>();
        mMedia = findViewById(R.id.mediaList);
        mMedia.setNestedScrollingEnabled(false);
        mMedia.setHasFixedSize(false);

        mMediaLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL,false);
        mMedia.setLayoutManager(mMediaLayoutManager);

        mMediaAdapter = new MediaAdapter(getApplicationContext(),mediaUriList);
        mMedia.setAdapter(mMediaAdapter);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        //Use Extra to select multiple images
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //Set the action which the user wants to do
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture(s)"), PICK_IMAGE_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_INTENT){
                //If user select only one image
                if (data.getClipData() == null) {
                    mediaUriList.add(data.getData().toString());
                } else {
                    //When user select multiple images
                    for (int i= 0; i < data.getClipData().getItemCount(); i++) {
                        //Add each an every image uri into the list
                        mediaUriList.add(data.getClipData().getItemAt(i).getUri().toString());
                    }
                }
                mMediaAdapter.notifyDataSetChanged();
            }
        }
    }
}