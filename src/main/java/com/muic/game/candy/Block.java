package com.muic.game.candy;

public class Block {

    private int x = 0;
    private int y = 0;
    private int value = 0;
    private int[] pos;
    private int VelX = 0;
    private int VelY = 0;
    private Point setPoint;
    private int row = 0;
    private int col = 0;

    public boolean isSwitchtrig;

    private boolean isNull = false;

    public Block(int col,int row,int value){
        this.row = row;
        this.col = col;
        pos = getPosition(col,row);
        this.x = pos[0];
        this.y = pos[1];
        this.value = value;
        this.setPoint = new Point(0,0);
        this.isSwitchtrig = false;
    }

    public void tick(){
        x+=VelX;
        y+=VelY;
    }

    public void setVelX(int VelX){
        this.VelX = VelX;
    }

    public void setVelY(int VelY){
        this.VelY = VelY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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
        return a;
    }

    public void setSlidepoint(Point p){
        setPoint.x = p.x;
        setPoint.y = p.y;
    }

    public Point getSlidepoint(){
        return setPoint;
    }

    public void setSwitchtrig(boolean switchtrig) {
        isSwitchtrig = switchtrig;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

}
