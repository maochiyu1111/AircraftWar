package edu.hitsz.factory.base;

import edu.hitsz.prop.GameProp;

public interface PropFactory {
    GameProp creatProp(int locationX, int locationY);
}
