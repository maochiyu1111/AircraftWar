package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.application.swing.UserRank;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;

import edu.hitsz.dao.User;
import edu.hitsz.dao.UserDaoImp;
import edu.hitsz.factory.base.EnemyFactory;
import edu.hitsz.factory.implement.BossEnemyFactory;
import edu.hitsz.factory.implement.EliteEnemyFactory;
import edu.hitsz.factory.implement.MobEnemyFactory;
import edu.hitsz.prop.GameProp;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public class Game extends JPanel {


    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    private final HeroAircraft heroAircraft = HeroAircraft.getInstance();

    protected AbstractAircraft boss;

    private EnemyFactory enemyFactory;

    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;

    private final List<GameProp> gameProps;



    /**
     * 屏幕中出现的敌机最大数量
     */
    private int enemyMaxNumber = 5;

    /**
     * 当前得分
     */
    private int score = 0;

    //初始分数阈值
    private int threshold = 200;

    /**
     * 当前时刻
     */
    private int time = 0;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;

    /**
     * 游戏结束标志
     */
    private boolean gameOverFlag = false;

    public Game() {
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        gameProps = new LinkedList<>();


        /**
         * Scheduled 线程池，用于定时任务调度
         * 关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
         * apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {
            try {
                time += timeInterval;

                // 周期性执行（控制频率）
                if (timeCountAndNewCycleJudge()) {
                    System.out.println(time);
                    // 新敌机产生
                    if (enemyAircrafts.size() < enemyMaxNumber) {
                        AbstractAircraft enemyAircraft = produceEnemy();
                        enemyAircrafts.add(enemyAircraft);
                    }

                    // 飞机射出子弹
                    shootAction();
                }

                // 子弹移动
                bulletsMoveAction();

                // 飞机移动
                aircraftsMoveAction();

                // 道具移动
                propMoveAction();

                // 撞击检测
                crashCheckAction();

                // 分数检测
                scoreCheckAction();

                // 后处理
                postProcessAction();

                //每个时刻重绘界面
                repaint();

                // 游戏结束检查英雄机是否存活
                if (heroAircraft.getHp() <= 0) {
                    // 游戏结束
                    executorService.shutdown();
                    gameOverFlag = true;
                    System.out.println("Game Over!");
                    //展示排行榜
                    UserRank userRank = new UserRank();
                    JPanel rankPanel = userRank.getMainPanel();
                    Main.cardPanel.add(rankPanel);
                    Main.cardLayout.last(Main.cardPanel);
                    userRank.inputUserName(score);
                }
            }
            catch (Exception e){
                System.out.println(e);
            }


        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private AbstractAircraft produceEnemy(){
        double i = Math.random();
        if(i > 0.8) {
            enemyFactory = new EliteEnemyFactory();
        }
        else{
            enemyFactory = new MobEnemyFactory();
        }
        return enemyFactory.creatEnemy();
    }



    private void shootAction() {
        // 敌机射击
        for(AbstractAircraft enemy : enemyAircrafts  ){
            enemyBullets.addAll(enemy.shoot());
        }

        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());

    }



    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    public void propMoveAction(){
        for (GameProp prop : gameProps) {
            prop.forward();
        }
    }



    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // TODO 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets){
            if(bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                // 英雄机撞击到敌机子弹
                // 英雄机损失一定生命值
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }

        }


        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // TODO 获得分数，产生道具补给
                        if(enemyAircraft instanceof EliteEnemy){
                            //分数+道具
                            score += 20;
                            GameProp prop = (((EliteEnemy) enemyAircraft).creatProp());
                            if (!Objects.isNull(prop)) {
                                gameProps.add(prop);
                            }
                        }
                        else if (enemyAircraft instanceof BossEnemy){
                            score += 50;
                            gameProps.addAll(((BossEnemy) enemyAircraft).creatProps());
                        }

                        score += 10;
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // Todo: 我方获得道具，道具生效
        for (GameProp prop :gameProps ){
            if (prop.crash(heroAircraft) || heroAircraft.crash(prop)){
                prop.takeEffect();
                prop.vanish();
            }
        }

    }

    /**
     * 分数检测，后续可能会添加多个方法到分数检测中
     * */
    private void scoreCheckAction(){
        if (score >= threshold){
            enemyAircrafts.add(bossProduce());
            threshold += 500;
        }
    }

    private AbstractAircraft bossProduce(){
        if (Objects.nonNull(boss)) {
            boss.vanish();
            System.out.println("boss刷新");
        }
        enemyFactory = new BossEnemyFactory();
        boss = enemyFactory.creatEnemy();
        return boss;
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        gameProps.removeIf(AbstractFlyingObject::notValid);
    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param  g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);

        paintImageWithPositionRevised(g, enemyAircrafts);
        paintImageWithPositionRevised(g, gameProps);


        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }


}
