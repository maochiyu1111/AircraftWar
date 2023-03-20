package edu.hitsz.factory.implement;

import edu.hitsz.factory.base.PropFactory;
import edu.hitsz.prop.FirepowerProp;
import edu.hitsz.prop.GameProp;

public class FirepowerPropFactory implements PropFactory {
    @Override
    public GameProp creatProp(int locationX, int locationY) {
        return new FirepowerProp(locationX, locationY, 0, 2);
    }
}
