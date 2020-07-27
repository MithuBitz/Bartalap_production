package com.example.bartalap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class findUserActivity extends AppCompatActivity {


    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;

    ArrayList<UserObject> userList, contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        userList = new ArrayList<>();
        contactList = new ArrayList<>();

        initializeRecyclerView();

        getContactList();
    }

    private void getContactList() {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null,null);

        //Lookup the contacts to get the name and phone number
        while (phones.moveToNext()) {
            //Get the name and number from the current cursor
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            UserObject mContacts = new UserObject(name,phone);
            contactList.add(mContacts);
            //mUserListAdapter.notifyDataSetChanged(); // delete this line because we dont want to change the data here

            //Function is used to fetch the database for available user
            getUserDetails(mContacts);
        }
    }

    private void getUserDetails(UserObject mContacts) {
        //Create a refference of the data on the database which want to fetch
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user");
        //Fetched data order by phone number
        Query query = mUserDB.orderByChild("phone").equalTo(mContacts.getPhone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String phone = "",
                            name = "";
                    // Get the user information from the database in an efficiant way
                    //and loop through to all the user of the database
                    for(DataSnapshot childSnapshot : snapshot.getChildren()){

                        //If the child data phone is exist than get the value from the database
                        if (childSnapshot.child("phone").getValue() != null) // if we dont use not null than app is crash when the value is null
                            phone = childSnapshot.child("phone").getValue().toString();

                        //If the child data phone is exist than get the value from the database
                        if (childSnapshot.child("name").getValue() != null) // if we dont use not null than app is crash when the value is null
                            phone = childSnapshot.child("name").getValue().toString();

                        //Create an UserObject from the data which is fetched from the database
                        UserObject mUser = new UserObject(name,phone);
                        //Create a userList from the data
                        userList.add(mUser);
                        mUserListAdapter.notifyDataSetChanged(); //This is nessecery
                        return;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getCountryISO() {
        String iso = null;

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if (telephonyManager.getNetworkCountryIso() != null)
            if (!telephonyManager.getNetworkCountryIso().toString().equals(""))
                iso = telephonyManager.getNetworkCountryIso().toString();

        return CountryToPhonePrefix.getPhone(iso);
    }

    private void initializeRecyclerView() {
        mUserList = findViewById(R.id.userList);
        mUserList.setNestedScrollingEnabled(false);
        mUserList.setHasFixedSize(false);

        mUserListLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false);
        mUserList.setLayoutManager(mUserListLayoutManager);

        mUserListAdapter = new UserListAdapter(userList);
        mUserList.setAdapter(mUserListAdapter);
    }
}