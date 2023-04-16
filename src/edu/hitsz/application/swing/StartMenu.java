package edu.hitsz.application.swing;

import edu.hitsz.application.*;
import edu.hitsz.application.game.EasyGame;
import edu.hitsz.application.game.Game;
import edu.hitsz.application.game.HardGame;
import edu.hitsz.application.game.MediumGame;

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

    public static MusicThread bgmThread = new MusicThread(VideoManager.BGM_VIDEO);


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
                Game.setDifficulty("easy");
                Game game = new EasyGame();
                Main.cardPanel.add(game);
                Main.cardLayout.last(Main.cardPanel);
                game.action();

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
                Game.setDifficulty("medium");
                Game game = new MediumGame();
                Main.cardPanel.add(game);
                Main.cardLayout.last(Main.cardPanel);
                game.action();
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
                Game.setDifficulty("hard");
                Game game = new HardGame();
                Main.cardPanel.add(game);
                Main.cardLayout.last(Main.cardPanel);
                game.action();
            }
        });


        musicRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(musicRadioButton.isSelected()){
                    Game.setNeedMusic(true);
                    bgmThread.setLoop(true);
                    bgmThread.setStop(false);
                    try {
                        bgmThread.start();
                    }catch (Exception ex){
                        synchronized (bgmThread) {
                            bgmThread.notifyAll();
                        }
                    }
                }
                else {
                    bgmThread.setStop(true);
                    Game.setNeedMusic(false);
                }
            }
        });



    }

    public Component getMainPanel() {
        return mainPanel;
    }
}
