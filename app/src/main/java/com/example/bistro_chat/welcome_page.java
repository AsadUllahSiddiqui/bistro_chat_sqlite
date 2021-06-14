package com.example.bistro_chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class welcome_page extends AppCompatActivity {
    ImageButton sign_in,register;
    connvariable c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        MyRecevier myRecevier=new MyRecevier();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(myRecevier,intentFilter);

        configure_sign_in_button();
        configure_register_button();
    }

    private void configure_sign_in_button(){
        sign_in= findViewById(R.id.sign_in_button);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                startActivity(new Intent(welcome_page.this,sign_in.class));
            }
        });
    }
    private void configure_register_button(){
        register= findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                String s="";
                s+=c.c.getConn();
                Toast.makeText(welcome_page.this,s,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(welcome_page.this,sign_up.class));
            }
        });
    }

}