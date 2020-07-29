package com.example.bartalap.Chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bartalap.R;

import com.example.bartalap.Chat.ChatListAdapter;
import com.example.bartalap.Chat.ChatObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>{
    ArrayList<ChatObject> ChatList;

    public ChatListAdapter(ArrayList<ChatObject> chatList) {
        this.ChatList = chatList;
    }

    @NonNull
    @Override
    public ChatListAdapter.ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutview.setLayoutParams(lp);

        ChatListAdapter.ChatListViewHolder rcv = new ChatListAdapter.ChatListViewHolder(layoutview);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ChatListViewHolder holder, final int position) {

        holder.mTitle.setText(ChatList.get(position).getChatId());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return ChatList.size();
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder{

        public TextView mTitle;

        //initialize the layout for the chat list
        public LinearLayout mLayout;

        public ChatListViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);

            mLayout = view.findViewById(R.id.layout);
        }
    }
}

