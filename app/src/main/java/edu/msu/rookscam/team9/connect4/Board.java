package edu.msu.rookscam.team9.connect4;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Robert on 3/13/2017.
 */

public class Board {

    /**
     * The current parameters
     */
    private Parameters params = new Parameters();

    private static class Parameters implements Serializable {

        public ArrayList<Disc> getPieces() {
            return pieces;
        }

        public void setPieces(ArrayList<Disc> pieces) {
            this.pieces = pieces;
        }

        public ArrayList<Disc> pieces;
    }

    /**
     * Completed board bitmap
     */
    private Bitmap boardComplete;

    /**
     * The size of the board in pixels
     */
    private int boardSize;

    /**
     * How much we scale the puzzle pieces
     */
    private float scaleFactor;

    /**
     * Left margin in pixels
     */
    private int marginX;

    /**
     * Top margin in pixels
     */
    private int marginY;

    /**
     * Percentage of the display width or height that
     * is occupied by the board.
     */
    final static float SCALE_IN_VIEW = 0.9f;

    /**
     * Paint for filling the area the board is in
     */
    private Paint fillPaint;

    public Disc getDragging() {
        return dragging;
    }

    public void setDragging(Disc dragging) {
        this.dragging = dragging;
    }

    /**
     * This variable is set to a piece we are dragging. If
     * we are not dragging, the variable is null.
     */
    private Disc dragging = null;

    /**
     * Most recent relative X touch when dragging
     */
    private float lastRelX;

    /**
     * Most recent relative Y touch when dragging
     */
    private float lastRelY;

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    /**
     * The number of the player currently playing
     */
    private int turn = 1;

    private boolean isTurn;

    public void setDiscSet(boolean discSet) {
        this.discSet = discSet;
    }

    /**
     * Indicates whether or not the disc has already been set
     */
    private boolean discSet = false;

    /**
     * The current context of the game
     */
    private Context currContext;

    private float boardHit;

    private float boardWid;

    private boolean won = false;

    private int placed = 0;

    public ArrayList<Disc> getPieces() {
        return params.pieces;
    }

    public void setPieces(ArrayList<Disc> pieces) {
        params.pieces = pieces;
    }

    /**
     * Collection of discs
     * Every sixth index is the start of a new column
     * Bottom of the board is zero for the rows, and left of the board is zero for the columns
     * Index of (0,0) would be 0, (0,1) would be 1, (1,0) would be 7, (1,1) would be 8
     */
    public ArrayList<Disc> pieces = new ArrayList<Disc>();

    public Board(Context context) {
        // Create paint for filling the area the board will
        // be solved in.
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(0xffcccccc);

        // Load the background board image
        boardComplete =
                BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.grip);

        boardHit = boardComplete.getHeight();
        boardWid = boardComplete.getWidth();

        currContext = context;
        params.pieces = pieces;
        for (int i = 0; i < 42; i++)
        {
            params.pieces.add(null);
        }

    }

    /**
     * Handle a touch event from the view.
     * @param view The view that is the source of the touch
     * @param event The motion event describing the touch
     * @return true if the touch is handled.
     */
    public boolean onTouchEvent(View view, MotionEvent event, float scale) {
        //
        // Convert an x,y location to a relative location in the
        // board.
        //
        float relX = (event.getX()) / boardSize / scale;
        float relY = (event.getY()) / boardSize / scale;

        if (!won && isTurn) {
            switch (event.getActionMasked()) {

                case MotionEvent.ACTION_DOWN:
                    return onTouched(relX, relY);

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    return onReleased(view, relX, relY);

                case MotionEvent.ACTION_MOVE:
                    // If we are dragging, move the piece and force a redraw
                    if ((dragging != null) && (discSet == false)) {
                        dragging.move(relX - lastRelX, relY - lastRelY);
                        lastRelX = relX;
                        lastRelY = relY;
                        view.invalidate();
                        return true;
                    }
                    break;
            }
        }

        return false;
    }

    private boolean onTouched(float x, float y) {
        if(discSet == false)
        {
            dragging = new Disc(currContext, turn, x, y);
            params.pieces.add(dragging);
            lastRelX = x;
            lastRelY = y;
            return true;
        }

        return false;
    }

    /**
     * Handle a release of a touch message.
     * @param x x location for the touch release, relative to the board - 0 to 1 over the board
     * @param y y location for the touch release, relative to the board - 0 to 1 over the board
     * @return true if the touch is handled
     */
    private boolean onReleased(View view, float x, float y) {

        if((dragging != null) && (discSet == false)) {
            //set piece
            discSet = true;
            view.invalidate();
            return setDisc(x);
        }

        return false;
    }

    public boolean setDisc(float x) {

       // x = ((dragging.getImage().getWidth()/2) / boardWid) + x;

        for (int i = 1; i < 8; i++)
        {
            if (x < ((float)i/7))
            {
                dragging.setCol(i-1);
                float xI = ((float)i/7);
                dragging.setX(xI);
                break;
            }

           /* else if (x > ((float)6/7))
            {
                dragging.setCol(6);
                dragging.setX((float)1);
                break;
            }*/

        }

        for (int i = 0; i < 6; i++)
        {
            if(params.pieces.get((i*7)+dragging.getCol()) == null)
            {
                params.pieces.remove(dragging);
                float ratio = boardHit/boardWid;
                float y = ((float)(6-i)/6);
                dragging.setY(y*ratio);
                dragging.setRow(i);
                dragging.setDiscSet(true);
                discSet= true;
                params.pieces.set((i*7)+dragging.getCol(),dragging);
                placed = (i*7)+dragging.getCol();
                break;
            }

            else if(i == 5)
            {
                params.pieces.remove(dragging);
                discSet = false;
                return false;
            }
        }

        return true;
    }

    public boolean changeTurn()
    {
        if ((discSet != false) && (won == false))
        {
            discSet = false;
            if (turn == 1) {
                turn = 2;
            } else {
                turn = 1;
            }

            dragging = null;
            return true;
        }

        else
        {
            return false;
        }
    }

    public void undo()
    {
        params.pieces.set(params.pieces.indexOf(dragging), null);
        dragging = null;
        discSet = false;
    }

    public boolean checkForWin(){
        for (int i = 0; i < 42; i++)
        {
            if ((params.pieces.get(i) != null) && (params.pieces.get(i).getPlayer() == turn))
            {
                int count = 1;
                for (int j = i+1; j < i + 4; j++)
                {
                    if((params.pieces.get(j) != null)&&(params.pieces.get(j).getPlayer() == turn))
                    {
                        count += 1;
                        if (count == 4)
                        {
                            new AlertDialog.Builder(currContext)
                                    .setTitle("Winner!")
                                    .setMessage("You win across!")
                                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {}})
                                    .show();
                            won = true;
                            return true;
                        }
                    }

                    else
                    {
                        break;
                    }
                }

                count = 1;
                for (int j = i+7; j < 42; j = j+7)
                {
                    if((params.pieces.get(j) != null)&&(params.pieces.get(j).getPlayer() == turn))
                    {
                        count += 1;
                        if (count == 4)
                        {
                            new AlertDialog.Builder(currContext)
                                    .setTitle("Winner!")
                                    .setMessage("You win vertical!")
                                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {}})
                                    .show();
                            won = true;
                            return true;
                        }
                    }

                    else
                    {
                        break;
                    }
                }

                count = 1;
                for (int j = i+8; j < 42; j = j+8)
                {
                    if((params.pieces.get(j) != null)&&(params.pieces.get(j).getPlayer() == turn))
                    {
                        count += 1;
                        if (count == 4)
                        {
                            new AlertDialog.Builder(currContext)
                                    .setTitle("Winner!")
                                    .setMessage("You win diagonal!")
                                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {}})
                                    .show();
                            won = true;
                            return true;
                        }
                    }

                    else
                    {
                        break;
                    }
                }

                if (i > 20) {
                    count = 1;
                    for (int j = i - 6; j > 0; j = j - 6) {
                        if ((params.pieces.get(j) != null) && (params.pieces.get(j).getPlayer() == turn)) {
                            count += 1;
                            if (count == 4) {
                                new AlertDialog.Builder(currContext)
                                        .setTitle("Winner!")
                                        .setMessage("You win downwards diagonal!")
                                        .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .show();
                                won = true;
                                return true;
                            }
                        } else {
                            break;
                        }
                    }
                }

            }

        }

        return false;
    }


    public void draw(Canvas canvas, float scale) {
        canvas.save();
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        // Determine the minimum of the two dimensions
        int minDim = wid < hit ? wid : hit;

        boardSize = (int)(minDim * SCALE_IN_VIEW);

        // Compute the margins so we center the board
        marginX = (wid - boardSize) / 2;
        marginY = (hit - boardSize) / 2;

        //
        // Draw the outline of the board
        //

        scaleFactor = (float)boardSize / (float)boardComplete.getWidth();

        canvas.scale(scaleFactor, scaleFactor);
        canvas.scale(scale, scale);
        canvas.drawBitmap(boardComplete, 0, 0, null);


        for (Disc disc: params.pieces) {
            if (disc != null) {
                disc.draw(canvas, marginX, marginY, boardSize, scaleFactor, scale);
            }
        }

        canvas.restore();
    }


    /**
     * Save the puzzle to a bundle
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {

        //bundle.putSerializable("xo-array", xoArray);
        bundle.putSerializable("pieces", params.pieces);

        bundle.putBoolean("disc-set", discSet);

        bundle.putInt("turn", turn);

        bundle.putSerializable("dragging", dragging);

    }

    /**
     * Read the puzzle from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        ArrayList<Disc> passPieces = (ArrayList<Disc>) bundle.getSerializable("pieces");

        setDiscSet(bundle.getBoolean("disc-set"));

        setPieces(passPieces);

        setTurn(bundle.getInt("turn"));

        Disc passDragging = (Disc) bundle.getSerializable("dragging");
        setDragging(passDragging);
    }

    public int getPlaced() {
        return placed;
    }

    public void setPlaced(int placed) {
        this.placed = placed;
    }

    public void setPiece(int grid)
    {
        Disc disc = new Disc(currContext,2,0,0);
        float ratio = boardHit/boardWid;
        disc.setX(((float)((grid%7)+1)/7));
        disc.setCol((int)grid%7);
        disc.setRow((int)grid/7);
        disc.setY(((float)(6-disc.getRow())/6)*ratio);
        pieces.set(grid,disc);
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void setTurn(boolean turn) {
        isTurn = turn;
    }
}
