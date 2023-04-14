package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.application.MusicThread;
import edu.hitsz.basic.AbstractFlyingObject;

/**
 * 道具类。
 * 三个不同的道具
 *
 * @author hitsz
 */
public abstract class GameProp extends AbstractFlyingObject {


    public GameProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        this.locationX += this.speedX;
        this.locationY += this.speedY;

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            vanish();
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= Main.WINDOW_HEIGHT) {
            // 向下飞行出界
            vanish();
        } else if (locationY <= 0) {
            // 向上飞行出界
            vanish();
        }
    }

    public abstract void takeEffect();

}
