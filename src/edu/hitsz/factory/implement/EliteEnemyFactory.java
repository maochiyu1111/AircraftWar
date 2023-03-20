package edu.hitsz.factory.implement;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.factory.base.EnemyFactory;

public class EliteEnemyFactory implements EnemyFactory {
    @Override
    public AbstractAircraft creatEnemy() {
        return new EliteEnemy(
                (int) (ImageManager.ELITE_ENEMY_IMAGE.getWidth() + Math.random() * (Main.WINDOW_WIDTH - 2 * ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                0,
                12,
                60);
    }
}
