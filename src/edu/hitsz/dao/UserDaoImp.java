package edu.hitsz.dao;

import java.io.*;
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
        if(userDataFile.exists()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(userDataFile));
                userList = (List<User>) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
    public void deleteUser(int row) {
        userList.remove(userList.get(row));
        writeToFile(userList);

    }

    @Override
    public void addUser(User user) {
        userList.add(user);
        writeToFile(userList);
    }


    @Override
    public List<User> getRankedUserList() {
        readFromFile();
        Collections.sort(userList, (user1, user2) -> Integer.compare(user2.getScore(), user1.getScore()));
        return userList;
    }

    private void writeToFile(List<User> userList){
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(userDataFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(userList);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //提供给其他函数的读取文件接口，返回userList列表
    private void readFromFile(){
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(userDataFile));
            userList = (List<User>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
