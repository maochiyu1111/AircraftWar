package edu.hitsz.observer;
import edu.hitsz.basic.AbstractFlyingObject;
import java.util.List;

/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/4/20 20:14
 */
public interface Publisher {

    public void subscribe(AbstractFlyingObject abstractFlyingObject);

    public void unsubscribe();

    public void notifyObservers();
}
