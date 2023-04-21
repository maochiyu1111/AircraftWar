package edu.hitsz.application.game;

import edu.hitsz.aircraft.AbstractAircraft;


/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/4/14 22:28
 */
public class EasyGame extends Game{
    public EasyGame(){
        super();
        cycleDuration = 480;
        threshold = 300;
        timeInterval = 40;
        enemyMaxNumber = 4;
        elitePosibility = 0.2;

    }
    // 敌机射击
    @Override
    protected void convertShootFlag(){
        enemyShootFlag = !enemyShootFlag;
    }

    @Override
    protected void scoreCheckAction() {}



}
