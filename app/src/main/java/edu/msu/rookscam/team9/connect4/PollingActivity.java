package edu.msu.rookscam.team9.connect4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class PollingActivity extends AppCompatActivity {
    private String player1;
    volatile String op="";
    private InputStream stream;
    volatile boolean notFail;
    volatile boolean ready = false;

    private TextView player1() {
        return (TextView)findViewById(R.id.player1Name);
    }
    private TextView player2() {
        return (TextView)findViewById(R.id.player2Name);
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
        setContentView(R.layout.activity_polling);
        final Intent intent = getIntent();
        player1 = intent.getExtras().getString("player1Name");
        player1().setText(player1);
        Button button = (Button)findViewById((R.id.button));
       // button.setVisibility(View.GONE);

        new Thread(new Runnable() {

            @Override
            public void run() {

                Cloud cloud = new Cloud();
                final boolean ok = cloud.join(player1);
            }
        }).start();

        checking();
    }

    public void checking() {

        final Timer timer = new Timer ();
        TimerTask timeTask = new TimerTask () {
            @Override
            public void run() {
                final Cloud cloud = new Cloud();
                notFail = cloud.checkStart(player1);
                if (notFail)
                {
                    op = cloud.getOpponent();
                    checkOp(timer);
                }
            }
        };
        timer.schedule (timeTask, 01, 100*60);   // 1000*10*60 every 10 minutes
    }

    public void onStart(View view)
    {
        if (ready) {
            Intent intent = new Intent(this, PlayActivity.class);
            intent.putExtra("player1Name", player1);
            intent.putExtra("player2Name", op);
            startActivity(intent);
        }
    }

    public void onEnableStart()
    {
    }

    public void checkOp(Timer timer)
    {
        if (op != "")
        {
            //Button button = (Button)findViewById((R.id.button));
            //button.setVisibility(View.VISIBLE);
            ready = true;
            timer.cancel();
            timer.purge();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView player2 = (TextView) findViewById(R.id.player2Name);
                    player2.setText(op);
                    Button button = (Button) findViewById(R.id.button);
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            onStart(v);
                        }
                    });
                }
            });
            // onStart();
        }
    }



}
