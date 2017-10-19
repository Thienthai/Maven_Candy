package com.muic.game.candy;
//update this one
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Game extends Canvas implements Runnable,MouseListener{

    private boolean running = false;
    private int change = 5;
    private Thread thread;
    private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
    private BufferedImage background = null;
    BufferedImage fish = null;
    private int x = 0;
    private Block[][] board = new Block[7][6];
    Random rand = new Random();
    BufferedImageLoader loader = new BufferedImageLoader();
    private boolean initRen = true;
    public BufferedImage red = loader.loadImage("red.png");
    public BufferedImage yellow = loader.loadImage("yellow.png");
    public BufferedImage blue = loader.loadImage("blue.png");
    public BufferedImage brown = loader.loadImage("brown.png");
    private BufferedImage green = loader.loadImage("green.png");
    private BufferedImage purple = loader.loadImage("purple.png");
    private int getMouseXpos = -1;
    private int getMouseYpos = -1;
    private int isSwitch = 0;
    private Block switchOrigin = null;
    private Block getSwitchDes = null;
    private int oldXpos = 0;
    private int oldYpos = 0;

    public Game() throws IOException {
        addMouseListener(this);
    }

    private synchronized void start(){
        if(running)
            return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop(){
        if(!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

    private void init(){
        BufferedImageLoader loader = new BufferedImageLoader();
        requestFocus();
        try{
            background = loader.loadImage("background.png");
            fish = loader.loadImage("red.png");
        }catch(Exception e){
            e.printStackTrace();
        }
        for(int i = 0;i < 6;i++){
            for(int j = 0;j < 6;j++){
                int  n = rand.nextInt(4) + 1;
                Block b = new Block(j,i,n);
                board[i][j] = b;
                b.setSlidepoint(new Point(b.getX(),b.getY()));
            }
        }
//        board[0][0].setNull(true);
//        board[0][1].setNull(true);
//        board[0][2].setNull(true);
//        board[0][3].setNull(true);
//        board[0][4].setNull(true);
//        board[0][5].setNull(true);
        board[2][3].setNull(true);
    }

    private void render() throws IOException {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(background, 0, 0, this);
        for(int i = 0;i<6;i++){
            for(int j = 0;j<6;j++){
                Block b = board[i][j];
                if(!b.isNull()) {
                    if (b.getValue() == 1) {
                        setPosition(g, b, red);
                    }
                    if (b.getValue() == 2) {
                        setPosition(g, b, blue);
                    }
                    if (b.getValue() == 3) {
                        setPosition(g, b, green);
                    }
                    if (b.getValue() == 4) {
                        setPosition(g, b, yellow);
                    }
                    if (b.getValue() == 5) {
                        setPosition(g, b, brown);
                    }
                    if (b.getValue() == 6) {
                        setPosition(g, b, purple);
                    }
                }else{
                    setNullblock(b);
                }
            }

        }
        g.dispose();
        bs.show();
    }

    private void setNullblock(Block b) {
        if(b.getRow() == 0) {
            b.setY(0);
            b.setValue(rand.nextInt(4) + 1);
            b.setSlidepoint(new Point(b.getX(), 58));
            b.setNull(false);
        }else{
            board[b.getRow()-1][b.getCol()].setSlidepoint(new Point(b.getX(),b.getY()));
            board[b.getRow()][b.getCol()] = board[b.getRow()-1][b.getCol()];
            b.setNull(false);
            board[b.getRow()-1][b.getCol()].setNull(true);
        }
    }

    public void run() {
        init();
        long lastTime = System.nanoTime();
        final double amoutOfTicks = 60.0;
        double ns = 1000000000 / amoutOfTicks;
        double delta = 0;
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1){
                boardUpdate();
                updates++;
                delta--;
            }
            try {
                render();
            } catch (IOException e) {
                e.printStackTrace();
            }
            frames++;
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println(updates + " Ticks,Fps " + frames);
                updates = 0;
                frames = 0;
            }
        }
        stop();
    }

    private void boardUpdate() {

            if(isSwitch == 1) {
                switchOrigin = board[getMouseYpos][getMouseXpos];
                oldXpos = getMouseXpos;
                oldYpos = getMouseYpos;
            }else if(isSwitch == 2){
                getSwitchDes = board[getMouseYpos][getMouseXpos];
                switchOrigin.setSlidepoint(new Point(getSwitchDes.getX(),getSwitchDes.getY()));
                getSwitchDes.setSlidepoint(new Point(switchOrigin.getX(),switchOrigin.getY()));
//                //switchSelect = true;
                Block tmp;
                tmp = board[oldYpos][oldXpos];
                board[oldYpos][oldXpos] = board[getMouseYpos][getMouseXpos];
                board[getMouseYpos][getMouseXpos] = tmp;
                switchOrigin.setSwitchtrig(true);
                getSwitchDes.setSwitchtrig(true);
                isSwitch = 0;
            }

        for(int i = 0;i < 6;i++){
            for(int j = 0;j < 6;j++){
                board[i][j].tick();
            }
        }
    }

    public static void main(String args[]) throws IOException {
        Game game = new Game();
        game.setPreferredSize(new Dimension(700,600));
        game.setMaximumSize(new Dimension(700,600));
        game.setMinimumSize(new Dimension(700,600));

        JFrame frame = new JFrame();
        frame.setTitle("Candy Crush");
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

        if(34 <= e.getX() && e.getX() <= 118){
            getMouseXpos = 0;
        }else if(121 <= e.getX() && e.getX() <= 201){
            getMouseXpos = 1;
        }else if(206 <= e.getX() && e.getX() <= 287){
            getMouseXpos = 2;
        }else if(291<= e.getX() && e.getX() <= 372){
            getMouseXpos = 3;
        }else if(377 <= e.getX() && e.getX() <= 455){
            getMouseXpos = 4;
        }else if(464 <= e.getX() && e.getX() <= 541) {
            getMouseXpos = 5;
        }

        if(61 <= e.getY() && e.getY() <= 131){
            getMouseYpos = 0;
            isSwitch += 1;
        }else if(137 <= e.getY() && e.getY() <= 204){
            getMouseYpos = 1;
            isSwitch += 1;
        }else if(208 <= e.getY() && e.getY() <= 273){
            getMouseYpos = 2;
            isSwitch += 1;
        }else if(281 <= e.getY() && e.getY() <= 346){
            getMouseYpos = 3;
            isSwitch += 1;
        }else if(350 <= e.getY() && e.getY() <= 415){
            getMouseYpos = 4;
            isSwitch += 1;
        }else if(420 <= e.getY() && e.getY() <= 486){
            getMouseYpos = 5;
            isSwitch += 1;
        }
        //System.out.println("X = " + getMouseXpos + ", Y = " + getMouseYpos);
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void setPosition(Graphics g,Block b,BufferedImage img){
        Point des = b.getSlidepoint();
            if(des.y == b.getY()) {
                if (b.getX() > des.x) {
                    if (b.getX() - 20 < des.x) {
                        b.setVelX(-(b.getX() - des.x));
                    } else {
                        b.setVelX(-20);
                    }
                } else if (b.getX() < des.x) {
                    if (b.getX() + 20 > des.x) {
                        b.setVelX(des.x - b.getX());
                    } else {
                        b.setVelX(20);
                    }
                } else if(b.getX() == des.x){
                    b.setVelX(0);
                }
            }
            if(b.getX() == des.x) {
                if (b.getY() > des.y) {
                    if (b.getY() - 20 < des.y) {
                        b.setVelY(-(b.getY() - des.y));
                    } else {
                        b.setVelY(-20);
                    }
                } else if (b.getY() < des.y) {
                    if (b.getY() + 20 > des.y) {
                        b.setVelY(des.y - b.getY());
                    } else {
                        b.setVelY(20);
                    }
                } else if(b.getY() == des.y){
                    b.setVelY(0);
                }
        }
        g.drawImage(img, b.getX(), b.getY(), this);
    }
}
