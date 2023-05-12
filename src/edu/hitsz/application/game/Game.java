package edu.hitsz.application.game;

import edu.hitsz.aircraft.*;
import edu.hitsz.application.*;
import edu.hitsz.application.swing.StartMenu;
import edu.hitsz.application.swing.UserRank;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;


import edu.hitsz.factory.base.EnemyFactory;
import edu.hitsz.factory.implement.EliteEnemyFactory;
import edu.hitsz.factory.implement.MobEnemyFactory;
import edu.hitsz.prop.BombProp;
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
public abstract class Game extends JPanel {


    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    protected int timeInterval = 40;

    protected final HeroAircraft heroAircraft = HeroAircraft.getInstance();

    protected AbstractAircraft boss;

    protected EnemyFactory enemyFactory;

    protected final List<AbstractAircraft> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;

    protected final List<GameProp> gameProps;

    protected boolean enemyShootFlag = true;
    protected boolean heroShootFlag = true;

    protected double elitePosibility = 0.2;

    public static double speedMultiplier = 1.0;

    public static double HPMultiplier = 1.0;

    /**
     * 屏幕中出现的敌机最大数量
     */
    protected int enemyMaxNumber = 5;

    /**
     * 当前得分
     */
    protected static int score = 0;
    public static void changeScore(int num){
        score += num;
    }

    //初始分数阈值
    protected int threshold = 200;

    /**
     * 当前时刻
     */
    protected int time = 0;

    protected int timeThreshold = 10000;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    protected int cycleDuration = 600;
    protected int cycleTime = 0;

    public static boolean isNeedMusic() {
        return needMusic;
    }

    public static void setNeedMusic(boolean needMusic) {
        Game.needMusic = needMusic;
    }

    private static boolean needMusic = false;

    MusicThread bossBgmThread = new MusicThread(VideoManager.BOSS_VIDEO);

    private static String difficulty;

    public static void setDifficulty(String difficulty) {
        Game.difficulty = difficulty;
    }

    public static String getDifficulty() {
        return difficulty;
    }

    /**
     * 游戏结束标志
     */
    private boolean gameOverFlag = false;

    protected boolean bossAppearance = false;

    private boolean isPaintBossBloodBar = false;

    private int BOSS_MAX_HP;


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
    public final void action() {

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

                //时间检测
                timeCheckAction();


                // 音效检测
                musicCheck();

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
                    if(needMusic){
                        StartMenu.bgmThread.setStop(true);
                        bossBgmThread.setStop(true);
                        MusicThread gameOverThread = new MusicThread(VideoManager.GAME_OVER_VIDEO);
                        gameOverThread.start();
                    }

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

    public AbstractAircraft produceEnemy(){
        double i = Math.random();

        if(i < elitePosibility) {
            enemyFactory = new EliteEnemyFactory();
        }
        else{
            enemyFactory = new MobEnemyFactory();
        }
        return enemyFactory.creatEnemy();
    }



    public void shootAction(){
        convertShootFlag();
        if(enemyShootFlag){
            for(AbstractAircraft enemy : enemyAircrafts  ){
                enemyBullets.addAll(enemy.shoot());
            }
        }

        if(heroShootFlag){
            // 英雄射击
            heroBullets.addAll(heroAircraft.shoot());
        }
    };


    protected abstract void convertShootFlag();

    protected abstract void timeCheckAction();

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
                    if(needMusic){
                        MusicThread hitThread = new MusicThread(VideoManager.BULLET_HIT_VIDEO);
                        hitThread.start();
                    }

                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        //获得分数，产生道具补给
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

        // 我方获得道具，道具生效
        for (GameProp prop :gameProps ){
            if (prop.crash(heroAircraft) || heroAircraft.crash(prop)){
                if(prop instanceof BombProp) {
                    for (BaseBullet enemyBullet : enemyBullets) {
                        ((BombProp) prop).subscribe(enemyBullet);
                    }
                    for (AbstractAircraft enemyAircraft : enemyAircrafts){
                        ((BombProp) prop).subscribe(enemyAircraft);
                    }
                }
                prop.takeEffect();
                prop.vanish();
            }
        }

    }

    /**
     * 分数检测，后续可能会添加多个方法到分数检测中
     * */
    protected abstract void scoreCheckAction();



    private void musicCheck() {
        //boss音乐
        if(needMusic){
            if (Objects.isNull(boss) || boss.notValid()) {
                bossBgmThread.setStop(true);
                bossBgmThread.setLoop(false);
                StartMenu.bgmThread.setStop(false);
                synchronized (StartMenu.bgmThread) {
                    StartMenu.bgmThread.notifyAll();
                }

            } else {
                StartMenu.bgmThread.setStop(true);
                bossBgmThread.setLoop(true);
                bossBgmThread.setStop(false);
                try {
                    bossBgmThread.start();
                }catch (Exception ex){
                    synchronized (bossBgmThread) {
                        bossBgmThread.notifyAll();
                    }
                }
            }
        }

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


        if (bossAppearance) {
                g.drawImage(ImageManager.TEST_IMAGE,
                        -40,
                        70,
                        null);
        }

        //绘制得分和生命值
        paintScoreAndLife(g);

        //绘制血条
        paintBossBloodBar(g);

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

    private void paintBossBloodBar(Graphics g) {
        boolean canPaint = false;
        try {
            if(boss.isValid()){
                canPaint = true;
            }
        } catch (Exception ex){
            canPaint = false;
        }

        if(canPaint){
            if(!isPaintBossBloodBar){
                BOSS_MAX_HP = boss.getHp();
                isPaintBossBloodBar = true;
            }
            g.setColor(Color.white);
            g.fillRect(boss.getLocationX() - 50, boss.getLocationY() + 100, 100, 10);
            int percentage = (int) (boss.getHp() * 100.0 / BOSS_MAX_HP) ;
            g.setColor(Color.red);
            g.fillRect(boss.getLocationX() - 50, boss.getLocationY() + 100, percentage, 10);
        } else {
            isPaintBossBloodBar = false;
        }


    }


}
