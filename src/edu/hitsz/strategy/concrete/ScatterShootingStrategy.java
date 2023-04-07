package edu.hitsz.strategy.concrete;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.strategy.ShootingStrategy;

import java.util.LinkedList;
import java.util.List;

public class ScatterShootingStrategy implements ShootingStrategy {

    @Override
    public List<BaseBullet> shootBullet(AbstractAircraft aircraft, int direction, int power, int shootNum) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + direction*10;
        BaseBullet bullet;
        if (direction == -1){
            for(int i=0; i<shootNum; i++){
                // 子弹发射位置相对飞机位置向前偏移
                // 子弹具有横向速度
                bullet = new HeroBullet(x + (i*2 - shootNum + 1)*10, y, aircraft.getSpeedX()+ (i -shootNum /2 )*2 , speedY, power);
                res.add(bullet);
            }
        }
        else {
            for(int i=0; i<shootNum; i++){
                // 子弹发射位置相对飞机位置向前偏移
                // 子弹具有横向速度
                bullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y, aircraft.getSpeedX()+ (i -shootNum /2 )*2 , speedY, power);
                res.add(bullet);
            }
        }
        return res;
    }
}
