package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public interface ShootingStrategy {
    List<BaseBullet> shootBullet(AbstractAircraft aircraft, int direction, int power, int shootNum);
}
