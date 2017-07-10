package edu.msu.rookscam.team9.connect4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    private TextView getWinner() {
        return (TextView)findViewById(R.id.winnerTextView);
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
        setContentView(R.layout.activity_end);

        Intent i  = getIntent();
        String winner = i.getStringExtra("winner");
        getWinner().setText(winner + " " + getString(R.string.wins));

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                final Cloud cloud = new Cloud();
                cloud.end();
            };
        });

        t1.start();

    }

    public void onExitClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onPlayAgainClick(View view) {
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }
}
