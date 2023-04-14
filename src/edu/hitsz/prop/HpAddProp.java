package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.game.Game;
import edu.hitsz.application.MusicThread;
import edu.hitsz.application.VideoManager;

public class HpAddProp extends GameProp{

    private HeroAircraft heroAircraft = HeroAircraft.getInstance();

    public HpAddProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void takeEffect(){
        if(Game.isNeedMusic()){
            MusicThread propThread = new MusicThread(VideoManager.GET_SUPPLY_VIDEO);
            propThread.start();
        }
        heroAircraft.hpAdding(50);
    }

}
