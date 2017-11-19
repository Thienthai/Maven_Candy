package com.muic.game.candy;
//update this one
import com.muic.game.Observer;
import com.muic.game.logic.ModBoard;
import com.muic.game.logic.Score;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
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

    // implement the variable to help to detect when press for command
    private int origin_row = -1; // origin row and column block that want to swap
    private int origin_col = -1;
    private int des_row = -1; // destination row and column block that want to swap
    private int des_col = -1;
    private int cmd_count = 1; // this will help to distinguis wether the press is for origin or destination

    private Observer os; // create observer in this class


    public Game() throws IOException {
        addMouseListener(this);
        // initialize the board first
        init();
    }

    // Start the thread
    public synchronized void start(){
        if(running)
            return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    //Stop the thread
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

    //Set up the initial Board
    private void init(){
        // Create loader to prepare for loading the image
        BufferedImageLoader loader = new BufferedImageLoader();

        // make us focus on the window immediatly
        requestFocus();

        // load the image to the board
        try{
            background = loader.loadImage("background.png");
        }catch(Exception e){
            e.printStackTrace();
        }

        // initialize the block
        for(int i = 0;i < 6;i++){
            for(int j = 0;j < 6;j++){
                int  n = rand.nextInt(4) + 1;
                Block b = new Block(j,i,n);
                board[i][j] = b;
                b.setSlidepoint(new Point(b.getX(),b.getY()));
            }
        }

        os = new Observer(board); // Create observer that use to manipulate the view
        //os.movePosition(0,0,0,5);
        //os.movePosition(0,5,0,0);
    }

    // THe main controller of our thread
    public void run() {

        // -- Manipulating the Frame rate thingy --
        long lastTime = System.nanoTime();
        final double amoutOfTicks = 60.0;
        double ns = 1000000000 / amoutOfTicks;
        double delta = 0;
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();
        // ----

        // Implement the board that use for logic for testing we need to fix the design pattern in this
        ArrayList<Integer>[][] boardLogic = new ArrayList[6][6];
        // We need to use modboard class to manipulate the logicBoard
        ModBoard mb = new ModBoard(os);
        mb.initBoard(boardLogic);

        //implement the score for testing
        Score score = new Score();
        printBoard(score,boardLogic);// print out the board

        // the main loop
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1){
                //boardUpdate();

                // if we detect any press we will use this if
                if(getMouseXpos != -1 && getMouseYpos != -1){
                    System.out.println("you press on row " + getMouseYpos + " column" + getMouseXpos);
                    int buffer_row = getMouseYpos; // use to keep the row and col from pressing
                    int buffer_col = getMouseXpos;
                    getMouseXpos = -1; // print out and set it back to -1
                    getMouseYpos = -1;

                    // if cmd_count = 2 we will start to mod since we gather all of origin and destination
                    if(cmd_count == 2){
                        des_row = buffer_row; // assign buffer to destination
                        des_col = buffer_col;

                        //if the board switch sucessfully we will print out the result and do dupcheck else we print error
                        if(mb.swapBoard(boardLogic,origin_row,origin_col,des_row,des_col)){
                            mb.dupCheck(boardLogic,score);
                            System.out.println("==============================================");
                            printBoard(score,boardLogic);// print out the board
                        }

                        cmd_count = 1;
                    }else {
                        origin_row = buffer_row; // assign buffer to origin
                        origin_col = buffer_col;
                        cmd_count++;
                    }
                }
                updates++;
                delta--;
            }
            try {

                // tick will help to add or subtract to the position of the block
                for(int i = 0;i < 6;i++){
                    for(int j = 0;j < 6;j++){
                        board[i][j].tick();
                }
        }

                // render the board
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

    // Use for render the graphic
    private void render() throws IOException {

        // -- initiallize buffer for graphic --
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            createBufferStrategy(3);
            return;
        }
        // --

        // Draw background and image to the board
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(background, 0, 0, this);
        // --

        // loop through the block in the board
        for(int i = 0;i<6;i++){
            for(int j = 0;j<6;j++){
                Block b = board[i][j];
                // if the block is exist we set the position to it
                if(!b.isNull()) {
                    if (b.getValue() == 1) {
                        // set position to it
                        setPosition(g, b, red);
                    }
                    if (b.getValue() == 2) {
                        // set position to it
                        setPosition(g, b, blue);
                    }
                    if (b.getValue() == 3) {
                        // set position to it
                        setPosition(g, b, green);
                    }
                    if (b.getValue() == 4) {
                        // set position to it
                        setPosition(g, b, yellow);
                    }
                    if (b.getValue() == 5) {
                        // set position to it
                        setPosition(g, b, brown);
                    }
                    if (b.getValue() == 6) {
                        // set position to it
                        setPosition(g, b, purple);
                    }
                }
            }

        }

        // clear the buffer thingy
        g.dispose();
        bs.show();
    }

//    private void boardUpdate() {
//
//            if(isSwitch == 1) {
//                switchOrigin = board[getMouseYpos][getMouseXpos];
//                oldXpos = getMouseXpos;
//                oldYpos = getMouseYpos;
//            }else if(isSwitch == 2){
//                getSwitchDes = board[getMouseYpos][getMouseXpos];
//                switchOrigin.setSlidepoint(new Point(getSwitchDes.getX(),getSwitchDes.getY()));
//                getSwitchDes.setSlidepoint(new Point(switchOrigin.getX(),switchOrigin.getY()));
////                //switchSelect = true;
//                Block tmp;
//                tmp = board[oldYpos][oldXpos];
//                board[oldYpos][oldXpos] = board[getMouseYpos][getMouseXpos];
//                board[getMouseYpos][getMouseXpos] = tmp;
//                switchOrigin.setSwitchtrig(true);
//                getSwitchDes.setSwitchtrig(true);
//                isSwitch = 0;
//            }
//
//        for(int i = 0;i < 6;i++){
//            for(int j = 0;j < 6;j++){
//                board[i][j].tick();
//            }
//        }
//    }

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


    // for set the new position to the block
    public void setPosition(Graphics g,Block b,BufferedImage img){

        //get the slidepoint
        Point des = b.getSlidepoint();
            // if y of destination equal to y of origin means that we need to fix x
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
        // if x of destination equal to x of origin means that we need to fix y
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

        // draw the image after reset the position
        g.drawImage(img, b.getX(), b.getY(), this);
    }

    // printout the logicBoard for testing
    public void printBoard(Score score,ArrayList<Integer>[][] boardLogic){ // print to check the board candy
        System.out.println("Result");
        System.out.println("");
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 6; j++){
                System.out.print(boardLogic[i][j].get(0) + " ");
            }
            System.out.println("");
        }
        System.out.println("Score: "+ score.getScore());
        System.out.println("");
        System.out.println("");
    }

}
