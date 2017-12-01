package com.muic.game.candy;

import com.muic.game.Observer;
import com.muic.game.logic.Score;

import java.util.ArrayList;
import java.util.Random;

public class BoardModel {

    // This contain the model of the board which use array for logic

    private ArrayList<Integer>[][] board;

    // this contain the model of the board which user array for view
    private Block[][] v_board;

    // Score for logic
    Score score;

    // Score for view
    Score v_score;

    // Observer in this class
    private Observer os;

    public BoardModel(){

        // when we create board model we will initiallize 2 model the logic model and view model
        this.board = new ArrayList[6][6];
        this.v_board = new Block[7][6];

        // Create the score to use in logic and view
        score = new Score();
        v_score = new Score();
        score.setScore(0);
        v_score.setScore(0);

    }

    //get the logic board
    public ArrayList<Integer>[][] getBoard(){
        return board;
    }

    public Block[][] getV_board(){
        return v_board;
    }

    //get the logic score
    public Score getScore(){
        return score;
    }

    // create the observer to a model
    public void addObserver(Observer os){
        this.os = os;
    }

    // let the observer to update the board
    public void notifyObserver_movepos(int ori_row,int ori_col,int des_row,int des_col){
        os.movePosition(v_board,ori_row,ori_col,des_row,des_col);
    }

    public void notifyObserver_setNull(int row,int col,boolean b){
        os.setNull(v_board,row,col,b);
    }

    public void notifyObserver_setValue(int row,int col,int value){
        os.setValue(v_board,row,col,value);
    }

    public void notifyObserver_setScore(){
        os.setScore(score,v_score);
    }



}
