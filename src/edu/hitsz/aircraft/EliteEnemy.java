package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.application.game.Game;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.factory.base.PropFactory;
import edu.hitsz.factory.implement.BombPropFactory;
import edu.hitsz.factory.implement.FirepowerPropFactory;
import edu.hitsz.factory.implement.HpAddPropFactory;
import edu.hitsz.observer.Observer;
import edu.hitsz.prop.*;
import edu.hitsz.strategy.ShootingStrategy;
import edu.hitsz.strategy.concrete.StraightShootingStrategy;

import java.util.LinkedList;
import java.util.List;

public class EliteEnemy extends AbstractAircraft implements Observer {

    /** 子弹伤害 **/
    private int power = 30;

    /*** 子弹射击方向 (向上发射：1，向下发射：-1) */
    private int direction = 1;

    private ShootingStrategy strategy = new StraightShootingStrategy();

    public boolean isProducible() {
        return this.IsProducible;
    }

    public void setProducible(boolean producible) {
        this.IsProducible = producible;
    }

    private boolean IsProducible = true;

    private static final double BOMB_PROP_THRESHOLD = 0.15;
    private static final double FIREPOWER_PROP_THRESHOLD = 0.4;
    private static final double HP_ADD_PROP_THRESHOLD = 0.9;


    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        return strategy.shootBullet(this, this.direction, this.power, 1);
    }

    public GameProp creatProp(){
        if(isProducible()){
            double randNum = Math.random();
            PropFactory propFactory = null;
            if (randNum < BOMB_PROP_THRESHOLD) {
                propFactory = new BombPropFactory();
            } else if (BOMB_PROP_THRESHOLD <= randNum && randNum < FIREPOWER_PROP_THRESHOLD) {
                propFactory = new FirepowerPropFactory();
            } else if (FIREPOWER_PROP_THRESHOLD <= randNum && randNum < HP_ADD_PROP_THRESHOLD) {
                propFactory = new HpAddPropFactory();
            } else {
                return null;
            }
            return propFactory.creatProp(this.locationX, this.locationY);
        }
        else {
            return null;
        }

    }



    @Override
    public void update() {
        this.notValid();
        this.vanish();
        Game.changeScore(20);
        setProducible(false);
    }
}
