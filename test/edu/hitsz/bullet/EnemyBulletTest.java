package edu.hitsz.bullet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EnemyBulletTest {

    EnemyBullet enemyBullet = new EnemyBullet(50, 0, 0, 2, 60);
    /**
     * 用例编号： EBT-1
     * **/
    @DisplayName("Test EnemyBullet forward()")
    @Test
    void forward() {
        assertEquals(0, enemyBullet.getLocationY());
        enemyBullet.forward();
        assertEquals(2, enemyBullet.getLocationY());
    }

    /**
     * 用例编号： EBT-2
     * **/
    @DisplayName("Test EnemyBullet getPower()")
    @ParameterizedTest
    @ValueSource(ints = { 59, 60, 61, 62 } )
    void getPower(int power) {
        if (power == 60) {
            assertEquals(power, enemyBullet.getPower());
        } else {
            assertNotEquals(power, enemyBullet.getPower());
        }
    }
}