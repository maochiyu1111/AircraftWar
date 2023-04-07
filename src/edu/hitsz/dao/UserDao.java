package edu.hitsz.dao;

/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/4/7 21:28
 */
public interface UserDao {
    void deleteUser(User user);
    void addUser(User user);

    void getRank();


}
