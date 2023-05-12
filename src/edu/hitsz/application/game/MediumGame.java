package edu.hitsz.application.game;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.factory.implement.BossEnemyFactory;
import edu.hitsz.factory.implement.EliteEnemyFactory;
import edu.hitsz.factory.implement.MobEnemyFactory;

import java.util.Objects;

/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/4/14 22:29
 */
public class MediumGame extends Game{

    private int thresholdInterval = 500;

    public MediumGame(){
        super();
        cycleDuration = 520;
        threshold = 400;
        timeInterval = 40;
        enemyMaxNumber = 5;
        elitePosibility = 0.25;

    }

    @Override
    protected void convertShootFlag() {}

    @Override
    protected void timeCheckAction() {
        if(time > timeThreshold){
            timeThreshold += 7000;
            HPMultiplier += 0.02;
            speedMultiplier += 0.02;
            if(elitePosibility + 0.02 <= 0.51 ){
                elitePosibility += 0.02;
            }
            System.out.println("当前速度倍率" + String.format("%.2f", speedMultiplier)
                    + "，当前血量倍率" + String.format("%.2f", HPMultiplier)
                    + "，当前产生精英机的概率" + String.format("%.2f", elitePosibility));
        }
    }


    @Override
    protected void scoreCheckAction() {
        if (score >= threshold){
            enemyAircrafts.add(bossProduce());
            threshold += thresholdInterval;
            thresholdInterval += 100;
        }
    }

    private AbstractAircraft bossProduce(){
        if (Objects.nonNull(boss)) {
            boss.vanish();
            System.out.println("boss未被消灭，已刷新重置");
        }
        enemyFactory = new BossEnemyFactory();
        boss = enemyFactory.creatEnemy();

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
