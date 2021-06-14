package com.example.bistro_chat;

public class connvariable {
    public static connection c=new connection();

    public connvariable(connection c) {
        c=c;
    }

    public static connection getC() {
        return c;
    }

    public static void setC(connection c) {
        connvariable.c = c;
    }
}
