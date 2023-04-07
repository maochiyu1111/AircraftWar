package edu.hitsz.dao;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/4/7 21:36
 */
public class UserDaoImp implements UserDao{


    File userDataFile = new File("src\\edu\\hitsz\\dao\\userRank.dat");


    @Override
    public void deleteUser(User user) {

    }

    @Override
    public void addUser(User user) {
        try {
            FileOutputStream fos = new FileOutputStream(userDataFile, true);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getRank() {
        List<User> userList = new ArrayList<>();
        try (
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userDataFile))){
                System.out.println("********************* \n" +
                        "得分排行榜 \n" +
                        "*********************");

                try {
                    while (true) { // 读取所有的User对象

                        List<User> user = (List<User>) ois.readObject();
                        //userList.add(user);
                    }
                } catch (EOFException e) {
                    // 文件读取结束
                }

                for (User user : userList){
                    String score = String.valueOf(user.getScore());
                    System.out.println("hhh, "+ user.getUserName() + ", "+ score + ", "+ user.getTime());
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
