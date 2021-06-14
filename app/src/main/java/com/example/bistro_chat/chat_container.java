package com.example.bistro_chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class chat_container extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ImageButton navigation_button;

    DatabaseHelper mDatabaseHelper;
    CircleImageView iimg;
    Button view_all_contacts;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    final DatabaseReference reference=database.getReference("user");
    FirebaseDatabase database2=FirebaseDatabase.getInstance();
    final DatabaseReference reference2=database2.getReference("chatting");
    RecyclerView chatRv;
    contact recc;
    ArrayList<contact> contactList;
    ArrayList<message> allmessages;
    EditText searchBar;
    chat_rv adapter;
    connvariable c;
    DrawerLayout drawerLayout;
    private FirebaseAuth auth;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_container);
        mDatabaseHelper=new DatabaseHelper(this);
        MyRecevier myRecevier=new MyRecevier();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(myRecevier,intentFilter);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view_user_home);
        auth=FirebaseAuth.getInstance();
        setNavigationViewListener();
        searchBar = findViewById(R.id.search_bar);
        contactList = new ArrayList<>();
        allmessages= new ArrayList<>();
        if(c.c.getConn()==1)
        {
            getdata();
        }
        else {
            Cursor curs = mDatabaseHelper.getAllmessages();
            while (curs.moveToNext()) {
                message mess = new message();
                mess.setSender(curs.getString(1));
                mess.setReceiver(curs.getString(2));
                mess.setMsg(curs.getString(3));
                mess.setTime(curs.getString(4));

                allmessages.add(mess);
            }

            Cursor cur = mDatabaseHelper.getAllData();
            while (cur.moveToNext()) {
                contact cont = new contact();
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
                if (!(cur.getString(10).equals("") )){
                    contactList.add(cont);}
            }
        }
        recyclerview_configuration();
        configure_navigation_Button();
        configure_view_all_contacts_Button();
        searchBar.addTextChangedListener(new TextWatcher() {
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
        //see_profile();
        upload_undelivered_messages();
    }

    public void getofflineData(){
        contactList.clear();
        Cursor cur=mDatabaseHelper.getAllData();
        while (cur.moveToNext()){
            if(cur.getString(11)!=null){
                contact newcon=new contact();
                Toast.makeText(chat_container.this, cur.getString(2),
                        Toast.LENGTH_SHORT).show();
            }

        }

    }
    private void getdata(){
        getallmessages();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(!profile.myData.getEmail().equals(snapshot.getValue(contact.class).getEmail())) {
                        String mm="";
                        String tt="";
                        recc=snapshot.getValue(contact.class);
                        for (int i = 0; i < allmessages.size(); i++)
                        {
                            if((profile.myData.getId().equals(allmessages.get(i).getSender()) &&
                                    recc.getId().equals(allmessages.get(i).getReceiver()) )||
                                   ( profile.myData.getId().equals(allmessages.get(i).getReceiver()) &&
                                           recc.getId().equals(allmessages.get(i).getSender())  )
                            )
                            {
                                recc.setTime(allmessages.get(i).getTime());
                                recc.setMsg(allmessages.get(i).getMsg());
                                mm=allmessages.get(i).getTime();
                                tt=allmessages.get(i).getMsg();
                            }
                        }
                        AddData(recc);
                        if(mm!="" && tt!="") {
                            contactList.add(recc);
                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void recyclerview_configuration(){
        chatRv = findViewById(R.id.chatRv);
        adapter = new chat_rv(contactList,chat_container.this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(chat_container.this);
        chatRv.setLayoutManager(manager);
        chatRv.setAdapter(adapter);
    }
    private void configure_navigation_Button(){
        navigation_button= findViewById(R.id.navigation_button);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.Header_user_name);
        navUsername.setText(profile.getMyData().getName());

        TextView navUseremail = (TextView) headerView.findViewById(R.id.Header_user_email);
        navUseremail.setText(profile.getMyData().getEmail());

        CircleImageView iimg = (CircleImageView) headerView.findViewById(R.id.Header_profile);
        iimg.setImageURI(Uri.parse(profile.getMyData().getPicture()));

        navigation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void see_profile(){
        iimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(chat_container.this,my_profile.class));
                finish();

            }
        });

    }
    private void configure_view_all_contacts_Button(){
        view_all_contacts= findViewById(R.id. view_all_contacts);
        view_all_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                startActivity(new Intent(chat_container.this, contact_container.class));
                finish();
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.user_Logout_id) {
            auth.signOut();
            startActivity(new Intent(chat_container.this, welcome_page.class));
        }
        else if (id == R.id.user_profile_id) {
            startActivity(new Intent(chat_container.this, my_profile.class));
        }
        return false;
    }
    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user_home);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void getallmessages(){
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    allmessages.add(snapshot.getValue(message.class));
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void filter(String string){
        ArrayList<contact> filteredList=new ArrayList<>();
        for (contact item:contactList){
            if( item.getName().toLowerCase().contains(string.toLowerCase())){
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
    public void AddData(contact c) {
        Cursor res=mDatabaseHelper.getAllData();
        if(res.getCount()==0)
        {
            boolean result = mDatabaseHelper.insertData(c);
            if (result) {
              //  Toast.makeText(chat_container.this, "data inserted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(chat_container.this, "Error inserting data to SQLite", Toast.LENGTH_SHORT).show();
            }
            return ;
        }
        else{
            int change=0;
            while (res.moveToNext()){
                if(res.getString(6).equals(c.getId())){
                    boolean isupdated=mDatabaseHelper.updateData(res.getString(6),c);
                    if(isupdated)
                    {
                       // Toast.makeText(chat_container.this,"data updated",Toast.LENGTH_SHORT).show();
                        change=1;
                    }
                    else
                    {
                        Toast.makeText(chat_container.this,"Error updating data to SQLite",Toast.LENGTH_SHORT).show();
                    }

                }
            }
            if(change==0){
                boolean result = mDatabaseHelper.insertData(c);
                if (result) {
                   // Toast.makeText(chat_container.this, "data inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(chat_container.this, "Error inserting data to SQLite", Toast.LENGTH_SHORT).show();
                }
                return ;
            }
        }


    }
    public void upload_undelivered_messages(){
        Cursor curs = mDatabaseHelper.getAllmessages();
        if(c.c.getConn()==1){
            message messages2 = new message();
            while (curs.moveToNext()) {
                messages2.setSender(curs.getString(1));
                messages2.setReceiver(curs.getString(2));
                messages2.setMsg(curs.getString(3));
                messages2.setTime(curs.getString(4));
                if(curs.getString(5).equals("0")){
                    reference2.push().setValue(messages2);

                    boolean cc= mDatabaseHelper.updateMessage(messages2.getSender(),messages2.getReceiver(),messages2.getMsg(),messages2.getTime(),messages2);
                    if(cc==true){Toast.makeText(chat_container.this,"updated to server",Toast.LENGTH_SHORT).show();}
                }
            }

        }

    }


}