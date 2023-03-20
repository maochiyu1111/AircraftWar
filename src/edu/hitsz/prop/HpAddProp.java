package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class HpAddProp extends GameProp{

    private HeroAircraft heroAircraft = HeroAircraft.getInstance();

    public HpAddProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void takeEffect(){
        heroAircraft.hpAdding(20);
    }

}
