package edu.hitsz.dao;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/4/7 21:36
 */
public class UserDaoImp implements UserDao{

    private List<User> userList = new ArrayList<>();

    private File userDataFile = new File("src\\edu\\hitsz\\dao\\userRank.dat");
    public UserDaoImp(){
        if(userDataFile.exists()){
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userDataFile));
                userList = (List<User>) ois.readObject();
                ois.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                userDataFile.createNewFile();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public void deleteUser(User userDelete) {
        for (User user : userList){
            if(user.getUserName().equals(userDelete.getUserName())){
                userList.remove(user);
            }
        }

    }

    @Override
    public void addUser(User user) {
        userList.add(user);
        Collections.sort(userList, (user1, user2) -> Integer.compare(user2.getScore(), user1.getScore()));
        try {
            FileOutputStream fos = new FileOutputStream(userDataFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getRank() {
        System.out.println("""
                ********************
                得分排行榜
                ********************
                """);

        int i = 1;
        for (User user : userList){
            //String score = String.valueOf(user.getScore());
            System.out.println("第"+i+"名：" + user.getUserName() + ", "+ user.getScore() + ", "+ user.getTime());
            i++;
        }

    }

}
