package com.example.bistro_chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import java.util.Calendar;

public class chatting_terminal extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;
    connvariable c;
    public static final String first_namej = "first_name";
    public static final String agej = "email";
    public static final String numberj = "number";
    public static final String genderj = "gender";
    public static final String urij = "uri";
    public static final String bioj = "bio";
    String pname, page, ppic, pnumber, pbio;
    Intent i, ii;
    ImageButton send, back;
    Button check_profile;
    RecyclerView rv;
    String IImg;
    message_rv messageAdapter;
    RecyclerView recyclerview;
    ArrayList<message> mchat;
    ArrayList<message> sqlite_chat;
    TextView contact_name, status;
    EditText msgET;
    CircleImageView img;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference reference = database.getReference("chatting");
    String rec, msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_terminal);
        chat_terminal_setup();
        mDatabaseHelper = new DatabaseHelper(this);

        if(c.c.getConn()==1)
        { readMessages(profile.myData.getId(), rec, "");}
        else{
            readmessagesSqlite(profile.myData.getId(), rec, "");
        }

        setRecyclerview_configuration();
        configure_send_Button();
        configure_click_here();
        configure_check_profile();
        upload_undelivered_messages();
    }

    public void configure_send_Button() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
                String currentDateTimeString = sdf.format(d);
                message msg = new message(profile.myData.getId(), rec, msgET.getText().toString(), currentDateTimeString);
                if(c.c.getConn()==1){
                reference.push().setValue(msg);
                mchat.add(msg);
                messageAdapter.notifyDataSetChanged();}
                else {
                    mDatabaseHelper.updateData2(rec,msg.getMsg());
                    mDatabaseHelper.insertMessagesUndelivered(msg);
                    mchat.add(msg);
                }

            }
        });
    }

    private void readMessages(final String myid, final String userid, String imgurl) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //mchat.add(snapshot.getValue(message.class));
                    message msg = snapshot.getValue(message.class);
                    if (msg.getReceiver().equals(myid) && msg.getSender().equals(userid) ||
                            msg.getReceiver().equals(userid) && msg.getSender().equals(myid)) {
                        mchat.add(msg);
                        adddata(msg);
                        messageAdapter.notifyDataSetChanged();
                    } else {
                        messageAdapter = new message_rv(mchat, chatting_terminal.this, IImg);
                        recyclerview.setAdapter(messageAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void setRecyclerview_configuration() {
        //recyclerview
        recyclerview = findViewById(R.id.chat_terminal_rv);
        recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerview.setLayoutManager(linearLayoutManager);
        rv = findViewById(R.id.chat_terminal_rv);
        messageAdapter = new message_rv(mchat, chatting_terminal.this, IImg);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        rv.setAdapter(messageAdapter);
        //messageAdapter=new message_rv(mchat,chatting_terminal.this,IImg);
        recyclerview.setAdapter(messageAdapter);
    }

    public void chat_terminal_setup() {
        msgET = findViewById(R.id.msgET);
        mchat = new ArrayList<>();
        send = findViewById(R.id.send);
        i = getIntent();
        contact_name = findViewById(R.id.contact_name);
        status = findViewById(R.id.status);
        img = findViewById(R.id.profile_img);
        contact_name.setText(i.getStringExtra(contact_rv.first_namex));
        status.setText(i.getStringExtra(contact_rv.genderx));
        IImg = i.getStringExtra(contact_rv.urix);
        Uri uri = Uri.parse(IImg);
        img.setImageURI(uri);
        rec = i.getStringExtra(contact_rv.idx);
        ii = new Intent(chatting_terminal.this, contact_profile.class);
        ii.putExtra(first_namej, i.getStringExtra(contact_rv.first_namex));
        ii.putExtra(numberj, i.getStringExtra(contact_rv.numberx));
        ii.putExtra(agej, i.getStringExtra(contact_rv.agex));
        ii.putExtra(genderj, i.getStringExtra(contact_rv.genderx));
        ii.putExtra(bioj, i.getStringExtra(contact_rv.biox));
        ii.putExtra(urij, i.getStringExtra(contact_rv.urix));

    }

    private void configure_click_here() {
        back = findViewById(R.id.user_chat_back_Button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                startActivity(new Intent(chatting_terminal.this, chat_container.class));
                finish();
            }
        });
    }

    private void configure_check_profile() {
        check_profile = findViewById(R.id.checkprofile);
        check_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                startActivity(ii);
                finish();
            }
        });
    }

    public void adddata(message c) {
        Cursor res = mDatabaseHelper.getAllmessages();
        if (res.getCount() == 0) {
            boolean result = mDatabaseHelper.insertMessages(c);
            if (result) {
                //  Toast.makeText(chat_container.this, "data inserted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(chatting_terminal.this, "Error inserting data to SQLite", Toast.LENGTH_SHORT).show();
            }
            return;
        } else {
            int change = 0;
            while (res.moveToNext()) {
                change=0;
                if (res.getString(1).equals(c.getSender())
                        && res.getString(2).equals(c.getReceiver())
                        && res.getString(3).equals(c.getMsg())
                        && res.getString(4).equals(c.getTime())) {
                    return ;
                } else {
                    change = 1;
                }
            }
            if(change==1){
                boolean result = mDatabaseHelper.insertMessages(c);

            }
        }
    }

    public void readmessagesSqlite( String myid, final String userid, String imgurl){
        Cursor curs = mDatabaseHelper.getAllmessages();
        mchat.clear();
        while (curs.moveToNext()) {
            if (curs.getString(2).equals(myid) && curs.getString(1).equals(userid) ||
                    curs.getString(2).equals(userid) && curs.getString(1).equals(myid)) {
                message mess = new message();
                mess.setSender(curs.getString(1));
                mess.setReceiver(curs.getString(2));
                mess.setMsg(curs.getString(3));
                mess.setTime(curs.getString(4));
                mchat.add(mess);
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
                    reference.push().setValue(messages2);

                  boolean cc= mDatabaseHelper.updateMessage(messages2.getSender(),messages2.getReceiver(),messages2.getMsg(),messages2.getTime(),messages2);
                  if(cc==true){Toast.makeText(chatting_terminal.this,"uploaded",Toast.LENGTH_SHORT).show();}
                }
            }

        }

    }

}