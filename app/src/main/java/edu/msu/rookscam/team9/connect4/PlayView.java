package edu.msu.rookscam.team9.connect4;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;
import java.io.Serializable;import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.content.Context;
import android.view.MotionEvent;

/**
 * Custom view class for Connect 4.
 */
public class PlayView extends View {

    final static float SCALE_IN_VIEW = 1f;

    private ScaleGestureDetector mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    private float mScaleFactor = 1.f;

    /**
     * The current parameters
     */
    private Parameters params = new Parameters();

    //grid variable
    private Bitmap grids = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.grip);

    private Board playBoard;

    public PlayView(Context context) {
        super(context);
        init(null, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);

        // Passes events to playBoard
        playBoard.onTouchEvent(this, ev, mScaleFactor);
        return true;
    }


    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }

    public PlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        playBoard = new Board(getContext());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        playBoard.draw(canvas, mScaleFactor);

        canvas.restore();

    }

    /**
     * Save the view state to a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to save to
     */
    public void putToBundle(String key, Bundle bundle) {
        bundle.putSerializable(key, params);
    }

    /**
     * Get the view state from a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to load from
     */
    public void getFromBundle(String key, Bundle bundle) {
        params = (Parameters)bundle.getSerializable(key);

        setCurrentPlayerName(params.currentPlayerName);
    }

    private static class Parameters implements Serializable {

        /**
         * player 1 name
         */
        public String player1Name;

        /**
         * player 2 name
         */
        public String player2Name;

        /**
         * current player name
         */
        public String currentPlayerName;
    }

    public void setCurrentPlayerName(String name) {
        params.currentPlayerName = name;
    }

    public String getCurrentPlayerName() {
        return params.currentPlayerName;
    }

    public String getPlayer1Name() {
        return params.player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        params.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return params.player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        params.player2Name = player2Name;
    }

    public Board getPlayBoard() {
        return playBoard;
    }

    public void setPlayBoard(Board playBoard) {
        this.playBoard = playBoard;
    }

    /**
     * Save the puzzle to a bundle
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {

          playBoard.saveInstanceState(bundle);
    }

    /**
     * Load the puzzle from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        playBoard.loadInstanceState(bundle);
    }
}