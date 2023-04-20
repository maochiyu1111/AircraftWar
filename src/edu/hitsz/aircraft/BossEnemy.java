package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.application.game.Game;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.base.PropFactory;
import edu.hitsz.factory.implement.BombPropFactory;
import edu.hitsz.factory.implement.FirepowerPropFactory;
import edu.hitsz.factory.implement.HpAddPropFactory;
import edu.hitsz.observer.Observer;
import edu.hitsz.prop.GameProp;
import edu.hitsz.strategy.ShootingStrategy;
import edu.hitsz.strategy.concrete.ScatterShootingStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Qing
 */
public class BossEnemy extends AbstractAircraft implements Observer {
    /** 子弹伤害 **/
    private int power = 50;

    /*** 子弹射击方向 (向上发射：1，向下发射：-1) */
    private int direction = 1;

    private int shootNum = 3;

    private int propNum = 3;

    private ShootingStrategy strategy = new ScatterShootingStrategy();


    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        locationX += speedX;
        locationY += speedY;
        if (locationX <= (int) (0.3*ImageManager.BOSS_ENEMY_IMAGE.getWidth()) || locationX >= Main.WINDOW_WIDTH - (int) (0.3*ImageManager.BOSS_ENEMY_IMAGE.getWidth())) {
            // 横向超出边界后反向
            speedX = -speedX;
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        return strategy.shootBullet(this, this.direction, this.power, this.shootNum);
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

    @Override
    public void update() {
        this.changeHP(-100);
        if(!this.isValid){
            Game.changeScore(50);
        }
    }
}
