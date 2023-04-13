package edu.hitsz.dao;

import java.util.List;

/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/4/7 21:28
 */
public interface UserDao {

    void deleteUser(int row);

    void addUser(User user);

    List<User> getRankedUserList();


}
