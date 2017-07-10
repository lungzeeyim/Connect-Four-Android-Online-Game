package edu.msu.rookscam.team9.connect4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /**
     * The edit text box for player 1 object
     */
    private EditText getPlayer1() {
        return (EditText)findViewById(R.id.usernameEditText);
    }

    /**
     * The edit text box for player 2 object
     */
    private EditText getPlayer2() {
        return (EditText)findViewById(R.id.passwordEditText);
    }

    private CheckBox getRememberMe() {
        return (CheckBox)findViewById(R.id.rememberMeCheckBox);
    }

    volatile boolean success;

    /**
     * This disables the back button by keeping this function empty
     */
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This grabs the username/password if the user selected "remember me" upon a successful login
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        try {
            String username =  sharedPreferences.getString("user", null);
            String password =  sharedPreferences.getString("password", null);

            if (username != null && password != null) {
                getPlayer1().setText(username);
                getPlayer2().setText(password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void onClickHowToPlay(View view) {
        Intent intent = new Intent(this, HowToPlayActivity.class);
        startActivity(intent);
    }

    public void onCreateUser(View view) {
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
    }

    public void onLoginUser(View view) {

        Cloud cloud = new Cloud();
        Thread t1 =new Thread(new Runnable() {

                @Override
                public void run() {
                    final Cloud cloud = new Cloud();
                    success = cloud.loginUser(getPlayer1().getText().toString(), getPlayer2().getText().toString());

                }
            });

        t1.start();
        try{
            t1.join(5000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(success)
        {
            Toast.makeText(view.getContext(), "Logged in successfully!.", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(this, PollingActivity.class);
            Intent intent = new Intent(this, PollingActivity.class);
            String username = getPlayer1().getText().toString();
            String password = getPlayer2().getText().toString();

            if (getRememberMe().isChecked()) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                final SharedPreferences.Editor editor = prefs.edit();
                editor.putString("user", username);
                editor.putString("password", password);
                editor.commit();
            }
            intent.putExtra("player1Name", username);
            intent.putExtra("player2Name", password);

            startActivity(intent);
        }

        else
        {
            Toast.makeText(view.getContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
