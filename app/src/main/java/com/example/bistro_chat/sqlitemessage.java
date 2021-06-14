package com.example.bistro_chat;

public class sqlitemessage {
    private String sender,receiver,msg,time,delivered;

    public sqlitemessage(String sender, String receiver, String msg, String time,String delivered) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
        this.time = time;
        this.delivered=delivered;
    }
    public sqlitemessage( ) {
        this.sender = "";
        this.receiver = "";
        this.msg = "";
        this.time = "";
        this.delivered="";
    }
    public sqlitemessage(String sender, String receiver, String msg) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
        this.time = "";
        this.delivered="";
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }
}
