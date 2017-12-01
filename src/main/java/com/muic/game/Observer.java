package com.muic.game;

import com.muic.game.candy.Block;
import com.muic.game.candy.BoardModel;
import com.muic.game.candy.Point;
import com.muic.game.logic.Score;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

// This will create the observer for the board model for communication to view

public class Observer {

    BoardModel md;

    public Observer(BoardModel md){
        // Create observer toward model
        this.md = md;
        md.addObserver(this);
    }

    // This will create random cell and have transition from the upper
    public void randomBlock(Block[][] board,int row,int col){

        Random r = new Random();

        // Create an instance of block
        Block b = new Block(col,row,r.nextInt(4) + 1);

        // assign it to the view board
        board[row][col] = b;

        //set the slide point to specific the board position of each block
        board[row][col].setSlidepoint(new Point(b.getX(),b.getY()));

        // set the current position to be outside so that it will create a good transition
        board[row][col].setX(board[row][col].getX());
        board[row][col].setY(-500);

    }

    // Move the candy from one position to another
    public void movePosition(Block[][] board,int ori_row,int ori_col,int des_row,int des_col){


        // Just a way to get the position from specific row and col
        int[] point = board[ori_row][ori_col].getPosition(ori_col,ori_row);

        // create the slide point
        board[des_row][des_col].setX(point[0]);
        board[des_row][des_col].setY(point[1]);

        point = board[des_row][des_col].getPosition(des_col,des_row);

        board[ori_row][ori_col].setX(point[0]);
        board[ori_row][ori_col].setY(point[1]);

        int num1 = board[ori_row][ori_col].getValue(); // save the value of origin
        int num2 = board[des_row][des_col].getValue(); // save the value of destination
        board[des_row][des_col].setValue(num1);
        board[ori_row][ori_col].setValue(num2);

    }

    //set specific block to null or not null
    public void setNull(Block[][] board,int row,int col,boolean b){
        board[row][col].setNull(b);
    }

    public void setValue(Block[][] board,int row,int col,int value){

        // Create an instance of block
        Block b = new Block(col,row,value);

        // assign it to the view board
        board[row][col] = b;

        //set the slide point to specific the board position of each block
        board[row][col].setSlidepoint(new Point(b.getX(),b.getY()));
    }

    public void setScore(Score score,Score v_score){
        v_score.setScore(score.getScore());
    }

}
