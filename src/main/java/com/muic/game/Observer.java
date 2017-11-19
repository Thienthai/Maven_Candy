package com.muic.game;

import com.muic.game.candy.Block;
import com.muic.game.candy.Point;

import java.util.ArrayList;
import java.util.Random;

// This will create the observer for the board logic for communication to view

public class Observer {

    Block[][] board;

    public Observer(Block[][] board){
        this.board = board;
    }

    // This will create random cell and have transition from the upper
    public void randomBlock(int row,int col){

        // make the block to appear jus in case
        board[row][col].setNull(false);

        Random r = new Random();

        // set a new random number for
        board[row][col].setValue(r.nextInt(4) + 1);

        board[row][col].setX(board[row][col].getX());
        board[row][col].setY(-100);

    }

    // Move the candy from one position to another
    public void movePosition(int ori_row,int ori_col,int des_row,int des_col){


        // Just a way to get the position from specific row and col
        int[] point = board[ori_row][ori_col].getPosition(ori_col,ori_row);

        // create the slide point
        board[des_row][des_col].setX(point[0]);
        board[des_row][des_col].setY(point[1]);

    }

    //set specific block to null or not null
    public void setNull(int row,int col,boolean b){
        board[row][col].setNull(b);
    }



}
