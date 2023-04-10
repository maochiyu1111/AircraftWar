package edu.hitsz.application.swing;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.ThreadManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/4/10 11:38
 */
public class StartMenu {
    private JButton easyButton;
    private JButton hardButton;
    private JButton mediumButton;
    private JRadioButton musicRadioButton;
    private JPanel mainPanel;
    private JPanel modePanel;
    private JPanel musicPanel;


    public static void main(String[] args) {
        JFrame frame = new JFrame("StartMenu");
        frame.setContentPane(new StartMenu().mainPanel);
        //设置窗口关闭时的默认操作，这里是终止程序。
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //使窗口大小适合其内容
        frame.pack();
        frame.setVisible(true);
    }

    public StartMenu() {
        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println("easy mode activate");
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg.jpg"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                ThreadManager.menuOver = true;
                //Game.setNeedMusic(music.getSelectedIndex() == 0);
                synchronized (ThreadManager.class) {
                    ThreadManager.class.notifyAll();
                }
            }
        });

        mediumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println("medium mode activate");
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg2.jpg"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                ThreadManager.menuOver = true;
                //Game.setNeedMusic(music.getSelectedIndex() == 0);
                synchronized (ThreadManager.class) {
                    ThreadManager.class.notifyAll();
                }
            }
        });

        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println("hard mode activate");
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg3.jpg"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                ThreadManager.menuOver = true;
                //Game.setNeedMusic(music.getSelectedIndex() == 0);
                synchronized (ThreadManager.class) {
                    ThreadManager.class.notifyAll();
                }
            }
        });


        musicRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            //TODO：线程启动
            }
        });



    }

    public Component getMainPanel() {
        return mainPanel;
    }
}
