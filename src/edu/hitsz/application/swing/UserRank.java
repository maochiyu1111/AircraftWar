package edu.hitsz.application.swing;

import edu.hitsz.application.game.Game;
import edu.hitsz.dao.User;
import edu.hitsz.dao.UserDaoImp;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/4/10 11:40
 */
public class UserRank {
    private JTextField difficultyTextField;
    private JTextField rankTextField;
    private JButton deleteButton;
    private JTable userTable;
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JScrollPane tableScrollPanel;

    private UserDaoImp userDaoImp = new UserDaoImp();

    List<User> userList = null;


    private String[] columnName = {"名次","用户名","玩家得分","游戏时间"};

    private String[][]tableData = null;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private DefaultTableModel model;



    public UserRank() {

        String diffStr = "难度：";
        if (Game.getDifficulty().equals("easy")) {
            System.out.println("set easy");
            diffStr += "简单";
        } else if (Game.getDifficulty().equals("medium")) {
            diffStr += "普通";
        } else if (Game.getDifficulty().equals("hard")) {
            diffStr += "困难";
        }
        difficultyTextField.setText(diffStr);


        //获取表格数据
        tableGetData();


        //删除按键响应
        deleteButton.addActionListener(e -> {
            int row = userTable.getSelectedRow();
            System.out.println(row);
            int result = JOptionPane.showConfirmDialog(deleteButton,
                    "是否确定删除？");
            if (JOptionPane.YES_OPTION == result && row != -1) {
                model.removeRow(row);
                userDaoImp.deleteUser(row);
            }
            //每次删除完后要fresh一下列表，重新读取文件，重新排序。
            tableGetData();
        });

    }


    //添加用户游戏记录，添加后刷新排行榜
    public void inputUserName(int score){
        String userName = null;
        userName = JOptionPane.showInputDialog(tableScrollPanel, "游戏结束，您的得分为"+score+"，"
                +"请输入名字记录得分。");
        if ( null == userName || userName.equals("")) {
            JOptionPane.showMessageDialog(null, "未能记录分数", "warning",JOptionPane.WARNING_MESSAGE);
        }
        else {
            User user = new User(userName, score);
            userDaoImp.addUser(user);
            //refresh table
            tableGetData();
        }
    }

    private void tableGetData(){
        userList = userDaoImp.getRankedUserList();
        int userNum = userList.size();
        tableData = new String[userNum][4];
        for (int i = 0; i < userNum; i++) {
            User user = userList.get(i);
            tableData[i][0] = (i + 1) + "";
            tableData[i][1] = user.getUserName();
            tableData[i][2] = user.getScore() + "";
            tableData[i][3] = user.getTime();
        }

        //重新设置，model模型
        model = new DefaultTableModel(tableData, columnName){
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };

        //JTable并不存储自己的数据，而是从表格模型那里获取它的数据
        userTable.setModel(model);
        tableScrollPanel.setViewportView(userTable);

    }


}


