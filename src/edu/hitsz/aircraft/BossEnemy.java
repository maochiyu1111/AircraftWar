package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.factory.base.PropFactory;
import edu.hitsz.factory.implement.BombPropFactory;
import edu.hitsz.factory.implement.FirepowerPropFactory;
import edu.hitsz.factory.implement.HpAddPropFactory;
import edu.hitsz.prop.GameProp;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Qing
 */
public class BossEnemy extends AbstractAircraft{
    /** 子弹伤害 **/
    private int power = 50;

    /*** 子弹射击方向 (向上发射：1，向下发射：-1) */
    private int direction = 1;

    private int shootNum = 3;

    private int propNum = 3;


    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }


    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speedY = this.getSpeedY() + direction*5;
        BaseBullet bullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            bullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y, (i -shootNum /2 )*2 , speedY, power);
            res.add(bullet);
        }
        return res;
    }

    public List<GameProp> creatProps(){
        List<GameProp>  res = new LinkedList<>();
        for (int i = 0; i < propNum; i++){
            double randNum = Math.random();
            PropFactory propFactory;
            if(randNum < 0.33){
                propFactory = new BombPropFactory();
            } else if (0.33 < randNum && randNum < 0.66) {
                propFactory = new FirepowerPropFactory();
            }
            else {
                propFactory = new HpAddPropFactory();
            }

            res.add(propFactory.creatProp(this.locationX + (i - shootNum/2 )*50 , this.locationY));
        }

        return res;
    }
}
