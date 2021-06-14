package com.example.bistro_chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper  extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="CONTACTS.db";
    public static final String TABLE_NAME="contacts";
    public static final String TABLE_NAME1="messages";

    public static final String MCOL1="MID";
    public static final String MCOL2="sender";
    public static final String MCOL3="receiver";
    public static final String MCOL4="Mmsg";
    public static final String MCOL5="Mtime";
    public static final String MCOL6="delivered";


    public static final String COL1="ID";
    public static final String COL2="name";
    public static final String COL3="last_name";
    public static final String COL4="number";
    public static final String COL5="gender";
    public static final String COL6="age";
    public static final String COL7="id_c";
    public static final String COL8="email";
    public static final String COL9="bio";
    public static final String COL10="birthday";
    public static final String COL11="msg";
    public static final String COL12="time";
    public static final String COL13="pic";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,last_name TEXT,number TEXT,gender TEXT,age TEXT,id_c TEXT,email TEXT,bio TEXT,birthday TEXT,msg TEXT,time TEXT,pic TEXT)");
        db.execSQL("create table "+TABLE_NAME1+" (MID INTEGER PRIMARY KEY AUTOINCREMENT,sender TEXT,receiver TEXT,Mmsg TEXT,Mtime TEXT,delivered TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME1);

    }

    public boolean insertData(contact c){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL2,c.getName());
        contentValues.put(COL3,c.getLast_name());
        contentValues.put(COL4,c.getNumber() );
        contentValues.put(COL5,c.getGender() );
        contentValues.put(COL6,c.getAge() );
        contentValues.put(COL7,c.getId());
        contentValues.put(COL8,c.getEmail() );
        contentValues.put(COL9,c.getBio() );
        contentValues.put(COL10,c.getBirthday() );
        contentValues.put(COL11,c.getMsg() );
        contentValues.put(COL12,c.getTime());
        contentValues.put(COL13,c.getPicture());


        long result=db.insert(TABLE_NAME,null,contentValues);
        if (result==-1){
            return false;
        }
        else {
            return true;
        }
    }
    public Cursor getAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
    public boolean updateData(String id,contact c){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL2,c.getName());
        contentValues.put(COL3,c.getLast_name());
        contentValues.put(COL4,c.getNumber() );
        contentValues.put(COL5,c.getGender() );
        contentValues.put(COL6,c.getAge() );
        contentValues.put(COL7,c.getId());
        contentValues.put(COL8,c.getEmail() );
        contentValues.put(COL9,c.getBio() );
        contentValues.put(COL10,c.getBirthday() );
        contentValues.put(COL11,c.getMsg() );
        contentValues.put(COL12,c.getTime());
        contentValues.put(COL13,c.getPicture());
        db.update(TABLE_NAME,contentValues,"ID_c = ? ", new String[]{id});
        return true;
    }

    public boolean updateData2(String id,String c){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL11,c );
        db.update(TABLE_NAME,contentValues,"id_c = ? ", new String[]{id});
        return true;
    }
    public Integer deletedata(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_NAME," ID = ?",new String[]{id});
    }

    ///messages

    public boolean insertMessages(message c){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(MCOL2,c.getSender());
        contentValues.put(MCOL3,c.getReceiver());
        contentValues.put(MCOL4,c.getMsg());
        contentValues.put(MCOL5,c.getTime());
        contentValues.put(MCOL6,"1");

        long result=db.insert(TABLE_NAME1,null,contentValues);
        if (result==-1){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean insertMessagesUndelivered(message c){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(MCOL2,c.getSender());
        contentValues.put(MCOL3,c.getReceiver());
        contentValues.put(MCOL4,c.getMsg());
        contentValues.put(MCOL5,c.getTime());
        contentValues.put(MCOL6,"0");
        long result=db.insert(TABLE_NAME1,null,contentValues);
        if (result==-1){
            return false;
        }
        else {
            return true;
        }
    }
    public Cursor getAllmessages(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME1,null);
        return res;
    }


    public boolean updateMessage(String sender,String receiver,String message,String time,message c){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(MCOL2,c.getSender());
        contentValues.put(MCOL3,c.getReceiver());
        contentValues.put(MCOL4,c.getMsg() );
        contentValues.put(MCOL5,c.getTime() );
        contentValues.put(MCOL6,"1");

        db.update(TABLE_NAME1,contentValues,"sender = ? AND receiver = ? AND Mmsg = ? And Mtime = ? " , new String[]{sender,receiver,message,time});
        return true;
    }

}
//String name,String last_name,String number,String gender,String age,String id_c,String email,String bio,String birthday,String msg,String time