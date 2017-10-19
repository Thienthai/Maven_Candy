package com.muic.game.candy;

public class SwitchRender {

    private int PosX_Ori;
    private int PosY_Ori;
    private int PosX_Des;
    private int PosY_Des;

    public void switchFunc(Block A,Block B,Block[][] board){
        PosX_Ori = A.getX();
        PosY_Ori = A.getY();
        PosY_Des = B.getX();
        PosY_Des = B.getY();

    }

}
