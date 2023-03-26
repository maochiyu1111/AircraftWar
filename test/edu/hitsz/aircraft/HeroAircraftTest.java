package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.EnemyBullet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {

    HeroAircraft heroAircraft = HeroAircraft.getInstance();
    EnemyBullet enemyBullet = new EnemyBullet(
            Main.WINDOW_WIDTH / 2,
            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
            0,
            5,
            20);

    @BeforeEach
    void setUp() {
        System.out.println("**--- Executed before each test method in this class ---**");

    }

    @AfterEach
    void tearDown() {
        System.out.println("**--- Executed after each test method in this class ---**");

    }

    /**
     * 用例编号HAT-1**/
    @Test
    void decreaseHp() {
        heroAircraft.decreaseHp(50);
        assertEquals(950, heroAircraft.getHp());
    }

    /**
     * 用例编号HAT-2**/
    @Test
    void crash() {
        assertTrue(heroAircraft.crash(enemyBullet));
    }
}