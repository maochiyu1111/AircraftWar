package edu.hitsz.prop;

import edu.hitsz.application.game.Game;
import edu.hitsz.application.MusicThread;
import edu.hitsz.application.VideoManager;

public class BombProp extends GameProp {
    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void takeEffect() {
        if(Game.isNeedMusic()){
            MusicThread bombThread = new MusicThread(VideoManager.BOMB_EXPLOSION_VIDEO);
            bombThread.start();
        }
        System.out.println("BombSupply active");
    }

}
