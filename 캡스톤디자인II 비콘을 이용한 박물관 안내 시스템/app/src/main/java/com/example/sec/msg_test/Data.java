package com.example.sec.msg_test;

import android.app.Application;

/**
 * Created by Dell Client on 2015-05-16.
 */
public class Data{
    String time;
    String writer;
    String name;
    int number;

    public void setData(String t, String w, String n, int num)
    {
        time = t;
        writer = w;
        name = n;
        number = num;
    }
    public void setData(String n, int num)
    {
        name = n;
        number = num;
    }
    public String getTime() { return time; }
    public String getWriter() { return writer; }
    public String getName() { return name; }
    public int getNumber() { return number; }
}
