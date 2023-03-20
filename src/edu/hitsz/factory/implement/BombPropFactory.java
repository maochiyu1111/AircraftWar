package edu.hitsz.factory.implement;

import edu.hitsz.factory.base.PropFactory;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.GameProp;

public class BombPropFactory implements PropFactory {

    @Override
    public GameProp creatProp(int locationX, int locationY) {
        return new BombProp(locationX, locationY , 0, 2);
    }
}
