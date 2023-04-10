package edu.hitsz.application.swing;

import javax.swing.*;

/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/4/10 11:35
 */
public class CardLayout {
    static final java.awt.CardLayout cardLayout = new java.awt.CardLayout(0,0);
    static final JPanel cardPanel = new JPanel(cardLayout);

    public static void main(String[] args) {

        JFrame frame = new JFrame("CardLayout");
        frame.setSize(800, 1024);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(cardPanel);

        //StartMenu start = new StartMenu();
        //cardPanel.add(start.getMainPanel());
        //frame.setVisible(true);
    }
}

