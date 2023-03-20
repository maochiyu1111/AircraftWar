package edu.hitsz.factory.implement;

import edu.hitsz.factory.base.PropFactory;
import edu.hitsz.prop.GameProp;
import edu.hitsz.prop.HpAddProp;

public class HpAddPropFactory implements PropFactory {

    @Override
    public GameProp creatProp(int locationX, int locationY) {
        return new HpAddProp(locationX, locationY, 0, 2);
    }
}
