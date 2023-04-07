package edu.hitsz.dao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/4/7 21:36
 */
public class User implements Serializable {

    private String userName;
    private int score;

    public String getUserName() {
        return this.userName;
    }

    public int getScore() {
        return this.score;
    }

    public String getTime() {
        return this.time;
    }

    private String time;

    public User(String userName, int score) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
        Timestamp d = new Timestamp(System.currentTimeMillis()); //获取当前时间
        this.time = sdf.format(d);
        this.userName = userName;
        this.score = score;
    }







}
