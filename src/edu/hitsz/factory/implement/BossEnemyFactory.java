package edu.hitsz.factory.implement;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.factory.base.EnemyFactory;

public class BossEnemyFactory implements EnemyFactory {
    @Override
    public AbstractAircraft creatEnemy() {
        return new BossEnemy(
                (int) (ImageManager.BOSS_ENEMY_IMAGE.getWidth() + Math.random() * (Main.WINDOW_WIDTH - 2 * ImageManager.BOSS_ENEMY_IMAGE.getWidth())),
                (int) (Main.WINDOW_HEIGHT * 0.05),
                10,
                0,
                500);
    }
}
