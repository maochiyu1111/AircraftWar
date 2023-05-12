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

    /*** 一 DONE
     * 子弹发射周期函数
     * 简单：英雄机更快 2:1  基础timeduration不变
     * 普通：英雄机与敌机 1:1 基础timeduration不变
     * 困难：英雄机与精英机1:1  基础timeduration越来越小，子弹越射越快
     * **/


    /*** 二 DONE
     * 游戏界面中出现的敌机数量的最大值（无函数负责）
     *简单：4
     *普通：5
     *困难：6
     *
     *
     * Boss敌机产生的得分阈值
     * 简单：/
     * 普通：基础300，周期500
     * 困难：基础200，周期400
     *
     *
     * Boss敌机每次出现的血量
     * 简单：/
     * 普通：基础500，不加血
     * 困难：基础500，出现一次+50
     * ***/


    /*** 三
     * 敌机的属性值，如血量、速度、子弹数
     * 简单：不加倍
     * 普通：随着分数乘 倍率， 分数间隔设置大一点就行
     * 困难：随着分数加倍率
     * ***/

    /*** 四
     * 精英敌机的产生概率 POSIBILITY
     * 简单：无变化
     * 普通：随着分数越来越大
     * 困难：随着分数越来越大
     *
     */

    /*** 解决四、三需求，一的部分需求
     * 函数设置：在scoreCheck()中
     * 简单：不重写，啥也没有
     * 普通：设置POSIBILITY增量、敌机属性值的倍率，血量倍率与速度倍率分开设
     * 困难：设置POSIBILITY增量、敌机属性值的倍率，血量倍率与速度倍率分开设，倍率要比普通稍高、还要设置timeduration
     * 该函数中内置boss相关函数*/

    /***
     * 内置boss函数 解决二需求
     * 简单：没有
     * 普通：其实也可以没有
     * 困难：每次生成设置加血，加子弹数（有个上限，还可以写一个shootStrategy后续操作啦）
     *
     * */

    /***
     * 调整射击频率相关函数
     * 首先在Game里面设置flag变量，在每次shoot前判断当前周期是否能shoot
     * 其次在每次shootaction中加入一个setflag的函数，每次都改变设计频率比
     * 函数采用int num 取余的方式来控制射击频率的比值*/

    /***
     * 额外需求：给boss出场设计一个动画，其实就几个字就行，先把孙铎的代码搞懂
     * 额外需求2：设计一个爆炸特效，为boss机加一个大血条。
     * 额外需求3：加一堆其他的素材：https://space.bilibili.com/1565870704
     * ***/




}