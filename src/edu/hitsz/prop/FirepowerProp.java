package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class FirepowerProp extends GameProp{

    private HeroAircraft heroAircraft = HeroAircraft.getInstance();

    public FirepowerProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void takeEffect(){

        heroAircraft.addShootNum(1);
        System.out.println("FirePowerSupply active");
    }
}
