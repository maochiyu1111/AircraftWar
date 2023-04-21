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
    private int bossFlag = 0;
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
    protected void scoreCheckAction() {
        if (score >= threshold){
            enemyAircrafts.add(bossProduce());
            threshold += 600;
            HPMultiplier += 0.12;
            speedMultiplier += 0.08;
            elitePosibility += 0.04;
            bossFlag += 1;
            System.out.println("当前速度倍率"+speedMultiplier + "，当前血量倍率"+HPMultiplier);
        }
    }

    private AbstractAircraft bossProduce(){
        if (Objects.nonNull(boss)) {
            boss.vanish();
            System.out.println("boss刷新");
        }
        enemyFactory = new BossEnemyFactory();
        boss = enemyFactory.creatEnemy();
        boss.changeHP(50*bossFlag);
        return boss;
    }
}
