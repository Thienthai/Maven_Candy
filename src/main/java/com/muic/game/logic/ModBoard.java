package com.muic.game.logic;

import com.muic.game.Observer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class ModBoard {

    Random r = new Random();

    Observer os; // Create observer variable to this class

    public ModBoard(Observer os){
        this.os = os;
    }

    // Initiallize the board when started
    public void initBoard(ArrayList<Integer>[][] board){



        for(int i = 0 ;i < 6;i++){
            for(int j = 0;j < 6;j++){
                board[i][j] = new ArrayList<Integer>();
                board[i][j].add(r.nextInt(4) + 1);
            }
        }

        // testing for dup initalize
        board[5][3].clear();
        board[5][4].clear();
        board[5][5].clear();

        board[5][3].add(4);
        board[5][4].add(4);
        board[5][5].add(4);

        int dup_num = 1;
        // Check from left to right in horizontal first
        for(int i = 0; i < 6;i++){
            for(int j = 1;j < 6;j++){

                // if it dup we will count up 1
                if(board[i][j-1].get(0) == board[i][j].get(0)){

                    dup_num+=1;


                }else{
                    dup_num=1;
                }

                // if the dup is equal to 3 we will random the new number on the current one
                if(dup_num == 3){
                    dup_num = 1;
                    board[i][j].clear(); // erase the old one
                    // we will keep random it until it not equal to the duplicate one
                    int num = r.nextInt(4)+1;
                    while(num == board[i][j-1].get(0)){
                        num = r.nextInt(4)+1;
                    }
                    board[i][j].add(num); // assign the random new one


                }

            }
        }

        // Check from vertical
        for(int j = 0; j < 6;j++){
            for(int i = 1;i < 6;i++){

                // if it dup we will count up 1
                if(board[i-1][j].get(0) == board[i][j].get(0)){

                    dup_num+=1;


                }else{
                    dup_num = 1;
                }

                // if the dup is equal to 3 we will random the new number on the current one
                if(dup_num == 3){
                    dup_num = 1;
                    board[i][j].clear(); // erase the old one
                    // we will keep random it until it not equal to the duplicate one
                    int num = r.nextInt(4)+1;
                    while(num == board[i-1][j].get(0)){
                        num = r.nextInt(4)+1;
                    }
                    board[i][j].add(num); // create the new random one

                }

            }
        }

        //use is_dup to check if it have dup so we still to reinitialize it
        if(isDup(board)){
            System.out.println("still dup");
            initBoard(board);
        }

    }

    //Make the block to be 0 for fall testing purpose
    public void setNullCandy(ArrayList<Integer>[][] board){

        board[1][1].clear();
        board[1][1].add(0);

        board[2][1].clear();
        board[2][1].add(0);

        board[3][1].clear();
        board[3][1].add(0);

    }

    //Set the falling position from the null cell
    public void fallSet(ArrayList<Integer>[][] board){

        boolean is_full = false; // For checking that if we have unfinished null block

        while(!is_full) // we keep do it until we get full board
        {
            is_full = true;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {

                    // If the current position is equal to 0 and the row is not on 0 index
                    if (board[i][j].get(0) == 0 && i != 0) {

                        is_full = false; // we see 0 the board still have null

                        int upperNumber = board[i - 1][j].get(0); //get number from the upper row

                        //set the position current to be upper number
                        board[i][j].clear();
                        board[i][j].add(upperNumber);

                        //set the upper position to be equal to 0
                        board[i - 1][j].clear();
                        board[i - 1][j].add(0);

                    }

                    // If the current position is equal to 0 and the row is on 0 index
                    else if (board[i][j].get(0) == 0 && i == 0) {

                        is_full = false; // we see 0 the board still have null

                        // set the position to be the random number bwtween 1 - 4
                        board[i][j].clear();
                        board[i][j].add(r.nextInt(4) + 1);

                    }

                }
            }
        }
    }

    //Check the duplication more than 3 cell
    public void dupCheck(ArrayList<Integer>[][] board,Score score){
        boolean dup_complete = false; // use to check if we complete dup check or not

        while(!dup_complete) {
            dup_complete = true;
            // Clone things for row_Board and Column board
            ArrayList<Integer>[][] row_Board = new ArrayList[6][6];
            ArrayList<Integer>[][] col_Board = new ArrayList[6][6];
            cloneArray(board, row_Board);
            cloneArray(board, col_Board);

            int dup_num = 1;
            int sus_num = -1; // create suspecious number

            //check on the vertical first
            for (int j = 0; j < 6; j++) {
                for (int i = 1; i < 6; i++) {

                    // we see the duplication by compare to the previous one
                    if (col_Board[i][j].get(0) == col_Board[i - 1][j].get(0)) {
                        sus_num = col_Board[i - 1][j].get(0); // make suspicous to be the previous one
                        dup_num += 1;
                    } else {
                        //in case that it the upper is all null we need to check on sus_num instead
                        if (col_Board[i - 1][j].get(0) == 0 && col_Board[i][j].get(0) == sus_num) {
                            dup_num += 1; // increase the dup number like before
                        } else {
                            dup_num = 1; // if we don't see anything we start a new count and set sus_num back to 0
                            sus_num = -1;
                        }
                    }

                    // we will start to set null if more than or equal to three
                    if (dup_num >= 3) {
                        score.setScore(score.getScore() + 100); // adding the score when we see duplication

                        dup_complete = false; // see dup occur so we still not complete

                        for (int k = 0; k < dup_num; k++) //loop back to the previous dup_num times and set it all to 0
                        {
                            col_Board[i - ((dup_num - k) - 1)][j].clear();
                            col_Board[i - ((dup_num - k) - 1)][j].add(0);
                        }
                    }
                }
                dup_num = 1; //reset dupnum when they increase more i
            }

            dup_num = 1; // initial it to be a new one
            sus_num = -1; // create suspecious number

            //check on the horizontal second
            for (int i = 0; i < 6; i++) {
                for (int j = 1; j < 6; j++) {

                    // we see the duplication
                    if (row_Board[i][j].get(0) == row_Board[i][j - 1].get(0)) {
                        sus_num = row_Board[i][j - 1].get(0); // make suspicous to be the previous one
                        dup_num += 1;
                    } else {

                        //in case that it the left is all null we need to check on sus_num instead
                        if (row_Board[i][j - 1].get(0) == 0 && row_Board[i][j].get(0) == sus_num) {
                            dup_num += 1; // increase the dup number like before
                        } else {
                            dup_num = 1; // if we don't see anything we start a new count and set sus_num back to 0
                            sus_num = -1;
                        }

                    }

                    // we will start to set null if more than or equal to three
                    if (dup_num >= 3) {
                        score.setScore(score.getScore() + 100); // adding the score when we see duplication
                        dup_complete = false; // see dup occur so we still not complete

                        for (int k = 0; k < dup_num; k++) //loop back to the previous dup_num times and set it all to 0
                        {
                            row_Board[i][j - ((dup_num - k) - 1)].clear();
                            row_Board[i][j - ((dup_num - k) - 1)].add(0);
                        }
                    }
                }
                dup_num = 1; //reset dupnum when they increase more i
            }

            // implement the solution to merge the origin with the row_Board and col_Board
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {

                    // set the current position to be 0 if we find out that the row or col board is equal to 0
                    if (row_Board[i][j].get(0) == 0 || col_Board[i][j].get(0) == 0) {
                        board[i][j].clear();
                        board[i][j].add(0);
                    }

                }
            }
            printBoard(board,score);
            fallSet(board); // set the null block so the board will be full for check again next round

        }
    }

    //clone the array from origin to destination
    public void cloneArray(ArrayList<Integer>[][] origin,ArrayList<Integer>[][] des){

        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 6; j++){
                des[i][j] = new ArrayList<Integer>();
                des[i][j].add(origin[i][j].get(0));
            }
        }

    }

    //swap the cell from origin to destination
    public boolean swapBoard(ArrayList<Integer>[][] board,int origin_row, int origin_col,int des_row,int des_col){

        // cross out since we need to inplement another input for gui
//        // get input from the user
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("Select origin row that you want to swap: ");
//        int origin_row = Integer.parseInt(br.readLine());
//        System.out.println("Select origin col that you want to swap: ");
//        int origin_col = Integer.parseInt(br.readLine());
//        System.out.println("Select destination row that you want to swap: ");
//        int des_row = Integer.parseInt(br.readLine());
//        System.out.println("Select destination col that you want to swap: ");
//        int des_col = Integer.parseInt(br.readLine());

        // list of position of origin is possible
        boolean false1 = (origin_col == des_col && origin_row == des_row + 1);
        boolean false2 = (origin_col == des_col && origin_row == des_row - 1);
        boolean false3 = (origin_row == des_row && origin_row == des_col + 1);
        boolean false4 = (origin_row == des_row && origin_row == des_col - 1);

        // if it true either one of the four the position is legit else it is not and we will not swap
        if(false1 || false2 || false3 || false4){
            int bufferInt_Origin = board[origin_row][origin_col].get(0); // keep the origin candy
            int bufferInt_Des = board[des_row][des_col].get(0); // keep the destination candy

            // delete the specific array to add new
            board[origin_row][origin_col].clear();
            board[des_row][des_col].clear();

            // swap the candy
            board[origin_row][origin_col].add(bufferInt_Des);
            board[des_row][des_col].add(bufferInt_Origin);
            return true;
        }else {

            System.out.println("FALSE POSITION NO SWAP");
            System.out.println(" ");
            System.out.println(" ");
            return false;
        }

    }

    // To check is it have duplication in the board or not
    public boolean isDup(ArrayList<Integer>[][] board){

        boolean isDup = false;

        // Clone things for row_Board and Column board
        ArrayList<Integer>[][] row_Board = new ArrayList[6][6];
        ArrayList<Integer>[][] col_Board = new ArrayList[6][6];
        cloneArray(board,row_Board);
        cloneArray(board,col_Board);

        int dup_num = 1;

        //check on the vertical first
        for(int j = 0; j < 6;j++){
            for(int i = 1; i < 6;i++){

                // we see the duplication
                if(row_Board[i][j].get(0) == row_Board[i-1][j].get(0)){
                    dup_num+=1;
                }else{
                    dup_num = 1; // if we don't see anything we start a new count
                }

                // we will start to set null if more than or equal to three
                if(dup_num >= 3){
                    isDup = true; // we see duplication
                    break;
                }

            }
        }

        dup_num = 1; // initial it to be a new one

        //check on the horizontal second
        for(int i = 0; i < 6; i ++){
            for(int j = 1;j < 6; j ++){

                // we see the duplication
                if(row_Board[i][j].get(0) == row_Board[i][j-1].get(0)){
                    dup_num+=1;
                }else{
                    dup_num=1; // if we don't see reset dup_num
                }

                // we will start to set null if more than or equal to three
                if(dup_num >= 3){
                    isDup = true; // we see duplication
                    break;
                }

            }
        }

        return isDup;

    }

    public void testCase(ArrayList<Integer>[][] board){

        for(int i = 0; i < 6; i++){
            for(int j = 0;j < 6; j++){

            }
        }

        board[0][0].add(1);
        board[1][0].add(1);
        board[2][1].add(1);
        board[3][0].add(1);


    }

    //print out the board
    public void printBoard(ArrayList<Integer>[][] board,Score score){ // print to check the board candy
        System.out.println("Duplication Occur");
        System.out.println("");
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 6; j++){
                System.out.print(board[i][j].get(0) + " ");
            }
            System.out.println("");
        }
        System.out.println("Score: "+ score.getScore());
        System.out.println("");
        System.out.println("");
    }
}
