package com.cheng.test;

import com.cheng.utils.RedisUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by niecheng on 2018/3/29.
 */
public class JedisPoolTestThread extends Thread{

    int i = 0;

    public JedisPoolTestThread(int i){
        this.i = i;
    }

    public void run(){
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        RedisUtils.setUser("foo", time);
        String foo = RedisUtils.getUser("foo");
        System.out.println("【输出>>>>】foo:" + foo + " 第："+i+"个线程" +"当前时间："+ format.format(Calendar.getInstance().getTime()));
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            JedisPoolTestThread t = new JedisPoolTestThread(i);
            t.start();
        }
    }
}
