package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.concrete.ScatterShootingStrategy;

public class FirepowerProp extends GameProp{

    private HeroAircraft heroAircraft = HeroAircraft.getInstance();

    public FirepowerProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void takeEffect(){

        heroAircraft.setShootNum(3);
        heroAircraft.setStrategy(new ScatterShootingStrategy());
        System.out.println("FirePowerSupply active");
    }
}
