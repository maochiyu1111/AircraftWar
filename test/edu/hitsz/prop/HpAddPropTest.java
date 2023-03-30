package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class HpAddPropTest {

    HpAddProp hpAddProp = new HpAddProp(10,10,0, 5);
    HeroAircraft heroAircraft = HeroAircraft.getInstance();

    /**
     * 用例编号HAPT-1
     * **/
    @DisplayName("Test HpAddProp takeEffect()")
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
    @DisplayName("Test HpAddProp getLocationY()")
    @ParameterizedTest
    @ValueSource(ints = { 9, 10, 11, 12 } )
    void getLocationY(int location) {
        if (location == 10) {
            assertEquals(location, hpAddProp.getLocationY());
        } else {
            assertNotEquals(location, hpAddProp.getLocationY());
        }

    }


}