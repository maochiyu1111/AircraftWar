package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HpAddPropTest {

    HpAddProp hpAddProp;
    HeroAircraft heroAircraft = HeroAircraft.getInstance();

    @BeforeEach
    void setUp() {
        System.out.println("**--- Executed before each test method in this class ---**");
        hpAddProp = new HpAddProp(10,10,0, 5);
    }

    @AfterEach
    void tearDown() {
        System.out.println("**--- Executed after each test method in this class ---**");
        hpAddProp = null;
    }

    /**
     * 用例编号HAPT-1
     * **/
    @Test
    void takeEffect() {
        heroAircraft.decreaseHp(100);
        hpAddProp.takeEffect();
        assertEquals(950, heroAircraft.getHp());
        hpAddProp.takeEffect();
        assertEquals(1000, heroAircraft.getHp());
        hpAddProp.takeEffect();
        assertEquals(1000, heroAircraft.getHp());
    }

    /**
     * 用例编号HAPT-2
     * **/
    @Test
    void getLocationY() {
        assertEquals(10, hpAddProp.getLocationY());
    }


}