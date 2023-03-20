package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.factory.base.PropFactory;
import edu.hitsz.factory.implement.BombPropFactory;
import edu.hitsz.factory.implement.FirepowerPropFactory;
import edu.hitsz.factory.implement.HpAddPropFactory;
import edu.hitsz.prop.*;

import java.util.LinkedList;
import java.util.List;

public class EliteEnemy extends AbstractAircraft{

    /** 子弹伤害 **/
    private int power = 30;

    /*** 子弹射击方向 (向上发射：1，向下发射：-1) */
    private int direction = 1;


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
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = this.getSpeedY() + direction*5;
        BaseBullet bullet;
        bullet = new EnemyBullet(x, y, speedX, speedY, power);
        res.add(bullet);
        return res;
    }

    public GameProp creatProp(){
        double randNum = Math.random();
        PropFactory propFactory = null;
        if(randNum < 0.3){
            propFactory = new BombPropFactory();
        } else if (0.3 < randNum && randNum < 0.6) {
            propFactory = new FirepowerPropFactory();
        }
        else if(0.6< randNum && randNum < 0.9){
            propFactory = new HpAddPropFactory();
        }
        else {
            return null;
        }
        return propFactory.creatProp(this.locationX, this.locationY);

    }

}
