package edu.hitsz.application;

import edu.hitsz.application.swing.StartMenu;
import edu.hitsz.application.swing.UserRank;

import javax.swing.*;
import java.awt.*;

/**
 * 程序入口
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public static void main(String[] args) {

        System.out.println("Hello Aircraft War");

        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("Aircraft War");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        //设置窗口的大小和位置,居中放置
        frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        StartMenu menu = new StartMenu();
        JPanel startMenuMainPanel = (JPanel) menu.getMainPanel();
        frame.add(startMenuMainPanel);
        frame.setVisible(true);

        Game game = new Game();
        new Thread(()->{
            synchronized (ThreadManager.class) {
                while(!ThreadManager.menuOver) {
                    try {
                        ThreadManager.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            frame.remove(startMenuMainPanel);
            frame.add(game);
            frame.setVisible(true);
            //将game面板设置为可聚焦的状态，即可以通过键盘或鼠标输入来操作该面板上的控件。
            game.setFocusable(true);
            //将焦点设置为game面板，这样用户在与程序交互时，输入的信息会直接作用于该面板上。
            game.requestFocus();
            game.action();
        }).start();

        new Thread(()->{
            synchronized (ThreadManager.class) {
                while (!ThreadManager.gameOver) {
                    try {
                        ThreadManager.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            //展示排行榜
            UserRank ranking = new UserRank();
            JPanel rankPanel = UserRank.getMainPanel();
            frame.remove(game);
            frame.add(rankPanel);
            frame.setVisible(true);
            //展示确认窗口
            Input input = new Input(Game.getScore(), Game.getDifficulty());
            JFrame inputFrame = new JFrame("记录分数");
            input.setSelf(inputFrame);
            input.setBoard(board);
            inputFrame.setLocationRelativeTo(null);
            inputFrame.setContentPane(input.getMainPanel());
            inputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            inputFrame.setUndecorated(true);
            inputFrame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
            inputFrame.pack();
            inputFrame.setResizable(false);
            inputFrame.setVisible(true);
        }).start();


    }
}
