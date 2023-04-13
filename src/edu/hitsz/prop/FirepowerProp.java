package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.concrete.ScatterShootingStrategy;
import edu.hitsz.strategy.concrete.StraightShootingStrategy;

import java.util.Timer;
import java.util.TimerTask;


public class FirepowerProp extends GameProp{

    private HeroAircraft heroAircraft = HeroAircraft.getInstance();

    private Timer timer = null;


    public FirepowerProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void takeEffect(){

        // 重置定时器
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();

        // 将英雄机的状态设置为散射和shootNum为3
        heroAircraft.setShootNum(3);
        heroAircraft.setStrategy(new ScatterShootingStrategy());

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 将英雄机的状态重置为直射和shootNum为1
                heroAircraft.setShootNum(1);
                heroAircraft.setStrategy(new StraightShootingStrategy());
                timer = null;
            }
        }, 5000);

    }
}
