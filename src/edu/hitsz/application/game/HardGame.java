package edu.hitsz.application.game;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;

import edu.hitsz.application.*;

import edu.hitsz.factory.implement.BossEnemyFactory;


import javax.imageio.ImageIO;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import java.util.Objects;


/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/4/14 22:29
 */
public class HardGame extends Game {

    private int bossFlag = 0;

    private boolean hasChangedBg = false;

    private int thresholdInterval = 500;

    public HardGame(){
        super();
        cycleDuration = 600;
        threshold = 400;
        timeInterval = 40;
        enemyMaxNumber = 6;
        elitePosibility = 0.3;

    }

    @Override
    protected void convertShootFlag() {}



    @Override
    protected void scoreCheckAction() {
        if (score >= threshold){
            enemyAircrafts.add(bossProduce());
            threshold += thresholdInterval;
            bossFlag += 1;
            if(cycleDuration >= 440){
                cycleDuration -= 40;
            }
            thresholdInterval += 100;
        }

        if (score >= 1000 && !hasChangedBg) {
            hasChangedBg = true;
            System.out.println("切换背景");
            try {
                ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg5.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void timeCheckAction() {
        if(time > timeThreshold){
            timeThreshold += 5000;
            HPMultiplier += 0.03;
            speedMultiplier += 0.03;
            if(elitePosibility + 0.02 <= 0.61 ){
                elitePosibility += 0.02;
            }
            System.out.println("当前速度倍率" + String.format("%.2f", speedMultiplier)
                    + "，当前血量倍率" + String.format("%.2f", HPMultiplier)
                    + "，当前产生精英机的概率" + String.format("%.2f", elitePosibility));

        }
    }

    private AbstractAircraft bossProduce(){
        if (Objects.nonNull(boss)) {
            boss.vanish();
            System.out.println("boss刷新");
        }
        enemyFactory = new BossEnemyFactory();
        boss = enemyFactory.creatEnemy();

        //每次boss出现，在原来的基础上加100生命值
        boss.changeHP(100*bossFlag);
        Class bossclass = BossEnemy.class;
        Method changeShootNum = null;
        try {
            changeShootNum = bossclass.getMethod("changeShootNum", int.class);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        try {
            changeShootNum.invoke(boss, 1*bossFlag);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("当前boss血量：" + boss.getHp());

        //设置boss出场特效线程
        new Thread(()->{
            bossAppearance = true;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            bossAppearance = false;
        }).start();
        return boss;
    }






}