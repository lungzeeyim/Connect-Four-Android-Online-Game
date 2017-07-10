package edu.msu.rookscam.team9.connect4;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class PlayActivity extends AppCompatActivity {

    private static final String PARAMETERS = "parameters";

    private boolean isTurn = false;

    private volatile boolean notFail;

    private volatile boolean justSent = false;

    private volatile int changeCount = 0;


    /**
     * The label to show which player's turn it is
     */
    private TextView getCurrentPlayer() {
        return (TextView)findViewById(R.id.currentPlayerName);
    }

    private PlayView getPlayView() {
        PlayView view = (PlayView)findViewById(R.id.playView);
        return view;
    }

    /**
     * This disables the back button by keeping this function empty
     */
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent i  = getIntent();
        String player1Name = i.getStringExtra("player1Name");
        String player2Name = i.getStringExtra("player2Name");
        getPlayView().setPlayer1Name(player1Name);
        getPlayView().setPlayer2Name(player2Name);

       checkForTurn();

        String currentPlayerName;

        if (isTurn) {
            getPlayView().setCurrentPlayerName(player1Name);
            currentPlayerName = player1Name;
        }

        else{
            getPlayView().setCurrentPlayerName(player2Name);
            currentPlayerName = player2Name;
        }

        TextView curr = (TextView)findViewById(R.id.currentPlayerName);
        curr.setText(getString(R.string.current_player) + " " + currentPlayerName);


        /*
         * Restore any state
         */
        if(savedInstanceState != null) {
            getPlayView().getFromBundle(PARAMETERS, savedInstanceState);


            getPlayView().loadInstanceState(savedInstanceState);
            //getGameView().loadInstanceState(savedInstanceState);

            updateUI();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        getPlayView().putToBundle(PARAMETERS, bundle);
        getPlayView().saveInstanceState(bundle);
    }

    public void onQuitClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onSurrenderClick(View view) {
        Intent intent = new Intent(this, EndActivity.class);
        String current = getPlayView().getCurrentPlayerName();
        String player1 = getPlayView().getPlayer1Name();
        String player2 = getPlayView().getPlayer2Name();
        String winner = "";
        if (current.equals(player1)){
            winner = player2;
        }
        else if (current.equals(player2)) {
            winner = player1;
        }

        intent.putExtra("winner", winner);
        startActivity(intent);
    }

    public void onWin(View view)
    {
        Intent intent = new Intent(this, EndActivity.class);
        String winner = getPlayView().getCurrentPlayerName();
        intent.putExtra("winner", winner);
        startActivity(intent);
    }

    public void onDoneClick(View view) {

        Intent intent = new Intent(this, EndActivity.class);
        final String player1 = getPlayView().getPlayer1Name();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                final Cloud cloud = new Cloud();
                cloud.push(player1, getPlayView().getPlayBoard().getPlaced());
            };
        });

        t1.start();

        if ( getPlayView().getPlayBoard().checkForWin() && isTurn) {
            String winner = getPlayView().getCurrentPlayerName();

            intent.putExtra("winner", winner);
            startActivity(intent);
        }

        else if ( getPlayView().getPlayBoard().changeTurn() && isTurn) {
            String current = getPlayView().getCurrentPlayerName();
            String player2 = getPlayView().getPlayer2Name();
            if (current.equals(player1)) {
                current = player2;
            } else if (current.equals(player2)) {
                current = player1;
            }


            getPlayView().setCurrentPlayerName(current);
            getCurrentPlayer().setText(getString(R.string.current_player) + " " + current);

            isTurn = false;
            getPlayView().getPlayBoard().setTurn(2);
            getPlayView().getPlayBoard().setTurn(false);

            justSent = true;

            checkForTurn();

        }

        else
        {
            new AlertDialog.Builder(getPlayView().getContext())
                    .setTitle("Player has not taken turn")
                    .setMessage("Player needs to take turn")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {}})
                    .show();
        }
    }

    void onUndoClick (View view)
    {
        if (isTurn)
        {
            getPlayView().getPlayBoard().undo();
            getPlayView().invalidate();

        }
    }

    public void checkForTurn()
    {
        final Timer timer = new Timer ();
        final TimerTask timeTask = new TimerTask () {
            @Override
            public void run() {
                final Cloud cloud = new Cloud();
                notFail = cloud.pull();
                String name = getPlayView().getPlayer1Name();
                if (notFail) {
                    if (changeCount < 2)
                    {
                        changeCount += 1;
                    }
                    else if ((getPlayView().getPlayer1Name().equals(cloud.getCurrUser())) || (cloud.getCurrUser().equals(""))) {
                        changeCount = 0;
                        isTurn = false;
                        getPlayView().getPlayBoard().setTurn(2);
                        getPlayView().getPlayBoard().setTurn(false);
                        getPlayView().setCurrentPlayerName(getPlayView().getPlayer2Name());
                        updateUI();
                    } else {
                        changeCount = 0;
                        isTurn = true;
                        int gi = cloud.getGridIndex();
                        if (gi != 43)
                        {
                            getPlayView().getPlayBoard().setPiece(cloud.getGridIndex());
                        }
                        Thread thread = new Thread() {
                            public void run(){
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        getPlayView().invalidate();
                                        boolean ifWin = getPlayView().getPlayBoard().checkForWin();
                                        if (ifWin) {
                                            onWin(findViewById(android.R.id.content));
                                        }
                                        getPlayView().setCurrentPlayerName(getPlayView().getPlayer1Name());
                                        updateUI();
                                        getPlayView().getPlayBoard().setTurn(1);
                                        getPlayView().getPlayBoard().setTurn(true);
                                    }
                                });
                            }
                        };
                        thread.start();

                        timer.cancel();
                        timer.purge();
                    }
                }
            }

        };

       /* try{
            if (justSent)
            {
                timeTask.wait(1000);
                justSent = false;
            }
        }
        catch (InterruptedException ex)
        {
            //Does nothing
        }*/

        timer.schedule (timeTask, 0l, 100*60);   // 1000*10*60 every 10 minutes
    }

    /**
     * Ensure the user interface components match the current state
     */
    private void updateUI() {
        getCurrentPlayer().setText(getString(R.string.current_player) + " " + getPlayView().getCurrentPlayerName());
    }
}
