package edu.msu.rookscam.team9.connect4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by Robert on 3/12/2017.
 */

public class Disc implements Serializable{

    /**
     * The image of the actual disc
     */
    private transient Bitmap image;

    /**
     * The column in which the disc is in
     */
    private int col;

    /**
     * The row in which the disc is in
     */
    private int row;

    /**
     * Indicates if the disc belongs to player1 or player2
     */
    private boolean player1;

    /**
     * The current x coordinate of the disc
     */
    private float x;

    /**
     * The current y coordinate of the disc
     */
    private float y;

    /**
     * Indicates if the current disc is set in the board
     */
    private boolean discSet = false;

    /**
     * Indicates which player the chip belongs to
     */
    private int player;

    /**
     * Constructor for the Disc class
     * @param context
     * @param id Sets the id of the player
     * @param initX The initial x coordinate of the disc
     * @param initY The initial y coordinate of the disc
     */
    public Disc(Context context, int id, float initX, float initY)
    {
        x = initX;
        y = initY;

        if (id == 1)
        {
            image = BitmapFactory.decodeResource(context.getResources(), R.drawable.player1);
        }

        else
        {
            image = BitmapFactory.decodeResource(context.getResources(), R.drawable.player2);
        }

        player = id;

    }

    /**
     * Move the puzzle piece by dx, dy
     * @param dx x amount to move
     * @param dy y amount to move
     */
    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isDiscSet() {
        return discSet;
    }

    public void setDiscSet(boolean discSet) {
        this.discSet = discSet;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * Draw the puzzle piece
     * @param canvas Canvas we are drawing on
     * @param marginX Margin x value in pixels
     * @param marginY Margin y value in pixels
     * @param puzzleSize Size we draw the puzzle in pixels
     * @param scaleFactor Amount we scale the puzzle pieces when we draw them
     */
    public void draw(Canvas canvas, int marginX, int marginY,
                     int puzzleSize, float scaleFactor, float scale) {
        canvas.save();

        // Convert x,y to pixels and add the margin, then draw
        canvas.translate(x * puzzleSize / scaleFactor,
                y * puzzleSize / scaleFactor);

        // Scale it to the right size
       // canvas.scale(scaleFactor, scaleFactor);
       // canvas.scale(scale, scale);

        // This magic code makes the center of the piece at 0, 0
        canvas.translate(-(image.getWidth()), -(image.getHeight()));

        // Draw the bitmap
        canvas.drawBitmap(image, 0, 0, null);
        canvas.restore();
    }

//    /**
//     * Save the puzzle to a bundle
//     * @param bundle The bundle we save to
//     */
//    public void saveInstanceState(Bundle bundle) {
//        bundle.putSerializable("xo-array", xoArray);
//    }
//
//    /**
//     * Read the puzzle from a bundle
//     * @param bundle The bundle we save to
//     */
//    public void loadInstanceState(Bundle bundle) {
//        int[][] passXOArray = (int[][]) bundle.getSerializable("xo-array");
//        setXoArray(passXOArray);
//    }
}
