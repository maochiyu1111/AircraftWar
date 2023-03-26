package edu.hitsz.bullet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemyBulletTest {

    EnemyBullet enemyBullet = new EnemyBullet(50, 0, 0, 2, 60);
    /**
     * 用例编号： EBT-1
     * **/
    @Test
    void forward() {
        assertEquals(0, enemyBullet.getLocationY());
        enemyBullet.forward();
        assertEquals(2, enemyBullet.getLocationY());
    }

    /**
     * 用例编号： EBT-2
     * **/
    @Test
    void getPower() {
        assertEquals(60, enemyBullet.getPower());
    }
}