package com.example.bistro_chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MyRecevier extends BroadcastReceiver {
    connvariable c;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null)
        {
            if(networkInfo.getType()==ConnectivityManager.TYPE_MOBILE)
            {
                //"internet",Toast.LENGTH_SHORT).show();
                c.c.setConn(1);
            }
            else if(networkInfo.getType()==ConnectivityManager.TYPE_WIFI)
            {
                //Toast.makeText(context,"internet",Toast.LENGTH_SHORT).show();
                c.c.setConn(1);
            }


        }
        else{
            //Toast.makeText(context," no internet",Toast.LENGTH_SHORT).show();
            c.c.setConn(0);
        }


    }
}
