package com.example.bistro_chat;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class contact_container extends AppCompatActivity {
    EditText search;
    ImageButton back;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference reference = database.getReference("user");
    RecyclerView userRv;
    ArrayList<contact> contactList;
    EditText searchBar;
    contact_rv adapter;
    contact a;
    connvariable c;
    profile p = new profile();
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_container);
        mDatabaseHelper = new DatabaseHelper(this);
        MyRecevier myRecevier=new MyRecevier();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(myRecevier,intentFilter);
        searchBar = findViewById(R.id.search_bar);
        userRv = findViewById(R.id.userRv);
        contactList = new ArrayList<>();
        if(c.c.getConn()==1)
        {
            getdata();
        }
        else
        {
            Cursor cur= mDatabaseHelper.getAllData();
            while (cur.moveToNext()){
                contact cont=new contact();
                cont.setName(cur.getString(1));
                cont.setLast_name(cur.getString(2));
                cont.setNumber(cur.getString(3));
                cont.setGender(cur.getString(4));
                cont.setAge(cur.getString(5));
                cont.setId(cur.getString(6));
                cont.setEmail(cur.getString(7));
                cont.setBio(cur.getString(8));
                cont.setBirthday(cur.getString(9));
                cont.setMsg(cur.getString(10));
                cont.setTime(cur.getString(11));
                cont.setPicture(cur.getString(12));
                contactList.add(cont);
            }

        }

        adapter = new contact_rv(contactList, this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(contact_container.this);
        userRv.setLayoutManager(manager);
        userRv.setAdapter(adapter);
        configure_navigation_Button();
        search = findViewById(R.id.search_bar);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
                adapter.notifyDataSetChanged();

            }
        });



    }

    private void getdata() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!profile.myData.getEmail().equals(snapshot.getValue(contact.class).getEmail())) {
                        contactList.add(snapshot.getValue(contact.class));
                        //AddData(snapshot.getValue(contact.class));
                    } else {
                        p.setMyData(snapshot.getValue(contact.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void configure_navigation_Button() {
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                startActivity(new Intent(contact_container.this, chat_container.class));
            }
        });

    }

    private void filter(String string) {
        ArrayList<contact> filteredList = new ArrayList<>();
        for (contact item : contactList) {
            if (item.getName().toLowerCase().contains(string.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

    public void AddData(contact c) {
        boolean result = mDatabaseHelper.insertData(c);
        if (result) {
            Toast.makeText(contact_container.this, "data inserted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(contact_container.this, "Error inserting data", Toast.LENGTH_SHORT).show();
        }
    }
}