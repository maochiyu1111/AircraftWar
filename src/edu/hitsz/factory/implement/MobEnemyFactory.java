package edu.hitsz.factory.implement;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.factory.base.EnemyFactory;

public class MobEnemyFactory implements EnemyFactory {
    public AbstractAircraft creatEnemy() {
        return new MobEnemy( (int) (ImageManager.MOB_ENEMY_IMAGE.getWidth() + Math.random() * (Main.WINDOW_WIDTH - 2 * ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                0,
                10,
                30);
    }
}
