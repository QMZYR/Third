
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Vector;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class test extends JFrame {

    EnemyPlane.MyPlane myPlane;

    //定义子弹的集合
    Vector<Bullet> bullets = new Vector<>();
    //敌机集合
    Vector<EnemyPlane> enemys = new Vector<>();

    test  frame;

    public test () {
        frame = this;
        //创建英雄机
        myPlane = new EnemyPlane.MyPlane();
        myPlane.start();
        //设置窗体的宽高
        this.setSize(600, 1000);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        //窗口可见
        this.setVisible(true);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    repaint();


                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //产生敌机的线程
        new Thread(new Runnable() {
            //创建随机数的对象
            Random r = new Random();

            @Override
            public void run() {
                while (true){
                    //启动敌机
                    EnemyPlane enemyPlane = new EnemyPlane(r.nextInt(500), 0, frame);
                    enemyPlane.start();
                    //添加敌机的时候，让x轴随机
                    enemys.add(enemyPlane);
                    if(enemys.contains(new EnemyPlane(myPlane.x, myPlane.y,frame))){
                        try {
                            test.this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try{
                        Thread.sleep(500);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }


//        *在窗口上画，内容，paint这 个画笔的方法在窗口初始化的时候会默认的执行
//        @param g

    public void paint(Graphics g) {
        //System.out.println("绘制画板");
        //两背景

        BufferedImage image = (BufferedImage) this.createImage(this.getSize().width, this.getSize().height);
        //高效缓存的画笔
        Graphics backGround = image.getGraphics();
        //画背景
        backGround.drawImage(new ImageIcon("6ec534289a07fd3e7a06d0d97c8589f8.png").getImage(),0,0,null);
        //画战斗机
        backGround.drawImage(myPlane.img, myPlane.x, myPlane.y, myPlane.width, myPlane.height,null);
        //飞机发射炮弹
        for (int i = 0; i < bullets.size(); i++) {
            System.out.println(bullets);
            Bullet bullet = bullets.get(i);
            if(bullet.y > 0){
                backGround.drawImage(bullet.image, bullet.x,bullet.y -= bullet.speed, bullet.width,bullet.height, null);}
            else
            { bullets.remove(bullet);}
        }
        //画敌机
        for (int i = 0; i < enemys.size(); i++) {
            System.out.println(enemys);
            EnemyPlane ep = enemys.get(i);
            if(ep.y < 730 ) {
                backGround.drawImage(ep.img, ep.x, ep.y += ep.speed, ep.width, ep.height, null);
            } else{
                enemys.remove(ep);
            }}


        //生效
        g.drawImage(image,0,0,null);
    }
    public static void main (String[]args){
        test frame = new test();
        //test.;
        Player player = new Player(frame);
        frame.addKeyListener(player);
    }
}


class Player extends KeyAdapter {
    test frame;
    EnemyPlane.MyPlane myPlane;
    public Player(test frame) {
        this.frame=frame;
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        //38、40、37、39
        switch (keyCode){
            case 38:
                frame.myPlane.up = true;
                break;
            case 40:
                frame.myPlane.down = true;
                break;
            case 37:
                frame.myPlane.left = true;
                break;
            case 39:
                frame.myPlane.right = true;
                break;
            case 66:
                addBullut();
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        //38、40、37、39
        switch (keyCode){
            case 38:

                frame.myPlane.up = false;
                break;
            case 40:

                frame.myPlane.down = false;
                break;
            case 37:

                frame.myPlane.left = false;
                break;
            case 39:

                frame.myPlane.right = false;
                break;
        }
    }
    public void addBullut(){
        frame.bullets.add(new Bullet( frame.myPlane.x+5,  frame.myPlane.y - 20));
    }
}

class EnemyPlane extends Thread {
    public test gf;
    //子弹的坐标，大小速度
    public int x, y;
    public int width = 50;
    public int height = 50;
    public int speed = 2;
    public Image img = new ImageIcon("src/be4c41e39e7975621988dd69279c762f.jpeg").getImage();

    public EnemyPlane(int x, int y, test gf) {
        super();
        this.x = x;
        this.y = y;
        this.gf = gf;
    }

    public EnemyPlane(int x, int y, int width, int height, test gf) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.gf = gf;
    }


    public void run() {
        while (true) {
            //向左走

            if (hit()) {
                System.out.println("hit................");
                this.speed = 0;
                this.img = new ImageIcon("src/08a3b0380cec04f0b8da7daef29a0cce.jpeg").getImage();
                try {
                    this.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gf.enemys.remove(this);
                break;
            }
            if (this.y >= 760) {
                break;
            }
            try {
                this.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    //检测碰撞
    public boolean hit() {
        // swing在水中，人家已经提供了
        boolean flag= false;
        Rectangle myrect = new Rectangle(this.x, this.y, this.width, this.height);
        Rectangle rect = null;
        for (int i = 0; 1 < gf.bullets.size(); i++) {
            Bullet bullet = gf.bullets.get(i);
            System.out.println("test hit");
            rect = new Rectangle(bullet.x, bullet.y - 1, bullet.width, bullet.height);
            //碰撞检测
            if (myrect.intersects(rect)) {
                flag=true;
            }
        }
        return flag;
    }

    @Override
    public String toString() {
        return "EnemyPlane{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    static class MyPlane extends Thread{
        //英雄机在画板上的位置
        int x=200, y=600;

        int width = 50, height = 50;
        //飞机的速度
        int speed = 10;

        Image img = new ImageIcon("src/ab412ac641bd31b1beaaa2dfb0380de9.jpeg").getImage();

        //定义方向键的标志
        boolean up,down,left,right;

        public MyPlane() {
        }

        public MyPlane(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        //和飞机移动有关
        @Override
        public void run() {
            while (true){
                if (up){
                    if(this.y>30){
                        y -= speed;}
                }
                if (down){
                    if(this.y<670){
                        y += speed;}
                }
                if (left){if(this.x>0){
                    x -= speed;}
                }
                if (right){if(this.x<550){
                    x += speed;}
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


class Bullet {
    //在面板上的坐标
    int x, y;
    int width= 50,height = 50;

    //定义飞机默认速度
    int speed = 5;

    Image image = new ImageIcon("src/08a3b0380cec04f0b8da7daef29a0cce.jpeg").getImage();

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Bullet(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
