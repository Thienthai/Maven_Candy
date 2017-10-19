package com.muic.game.candy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Candies {

    private BufferedImage red;
    private BufferedImage yellow;
    private BufferedImage blue;
    private BufferedImage brown;
    private BufferedImage green;
    private BufferedImage purple;

    public Candies() throws IOException {
        BufferedImageLoader loader = new BufferedImageLoader();
        this.red = loader.loadImage("red.png");
        this.yellow = loader.loadImage("yellow.png");
        this.green = loader.loadImage("green.png");
        this.purple = loader.loadImage("purple.png");
        this.blue = loader.loadImage("blue.png");
        this.brown = loader.loadImage("brown.png");
    }

    public void render(int[][] board, Graphics g,Game game) throws IOException {
        BufferedImageLoader loader = new BufferedImageLoader();
        for(int i = 0;i < 7;i++){
            for(int j = 0;j < 6;j++){
                BufferedImage candy = chooseCandy(board[i][j]);
                int[] pos = getPosition(j,i);
                g.drawImage(candy,pos[0],pos[1],game);
            }
        }
    }

    private BufferedImage chooseCandy(int a){
        if(a == 1)
            return red;
        if(a == 2)
            return blue;
        if(a == 3)
            return yellow;
        if(a == 4)
            return brown;
        if(a == 5)
            return purple;
        if(a == 6)
            return green;
        return null;
    }
    private int[] getPosition(int x,int y){
        int[] a = new int[2];
        if(x == 0){a[0] = 31;}
        if(x == 1){a[0] = 31+90;}
        if(x == 2){a[0] = 31+(90*2)-10;}
        if(x == 3){a[0] = 31+(90*3)-13;}
        if(x == 4){a[0] = 31+(90*4)-16;}
        if(x == 5){a[0] = 31+(90*5)-22;}

        if(y == 0){a[1] = 58;}
        if(y == 1){a[1] = 58+75;}
        if(y == 2){a[1] = 58+(75*2)-5;}
        if(y == 3){a[1] = 58+(75*3)-7;}
        if(y == 4){a[1] = 58+(75*4)-14;}
        if(y == 5){a[1] = 58+(75*5)-18;}
        if(y == 6){a[1] = 58+(75*6)-21;}
        return a;
    }
}
