package edu.hitsz.prop;

import edu.hitsz.application.game.Game;
import edu.hitsz.application.MusicThread;
import edu.hitsz.application.VideoManager;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.observer.Publisher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BombProp extends GameProp implements Publisher {

    private List<AbstractFlyingObject> observers = new ArrayList<>();

    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void takeEffect() {
        if(Game.isNeedMusic()){
            MusicThread bombThread = new MusicThread(VideoManager.BOMB_EXPLOSION_VIDEO);
            bombThread.start();
        }
        notifyObservers();
        unsubscribe();
    }

    @Override
    public void subscribe(AbstractFlyingObject abstractFlyingObject) {
        observers.add(abstractFlyingObject);
    }


    @Override
    public void unsubscribe() {
        observers.clear();
    }

    @Override
    public void notifyObservers() {
        for (AbstractFlyingObject observer : observers){
            Class observerClass = observer.getClass();
            Method update = null;
            try {
                update = observerClass.getMethod("update");
            } catch (NoSuchMethodException ex){
                ex.printStackTrace();
            }
            try {
                update.invoke(observer);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
