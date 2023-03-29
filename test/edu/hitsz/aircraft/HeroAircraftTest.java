package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.EnemyBullet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {

    HeroAircraft heroAircraft = HeroAircraft.getInstance();
    EnemyBullet enemyBullet;

    @BeforeEach
    void setUp() {
        enemyBullet = new EnemyBullet(
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
                0,
                5,
                20);

    }

    @AfterEach
    void tearDown() {
        enemyBullet = null;

    }

    /**
     * 用例编号HAT-1**/
    @DisplayName("Test HeroAircraft decreaseHp()")
    @Test
    void decreaseHp() {
        heroAircraft.decreaseHp(50);
        assertEquals(950, heroAircraft.getHp());
    }

    /**
     * 用例编号HAT-2**/
    @DisplayName("Test HeroAircraft crash()")
    @Test
    void crash() {
        assertTrue(heroAircraft.crash(enemyBullet));
    }
}