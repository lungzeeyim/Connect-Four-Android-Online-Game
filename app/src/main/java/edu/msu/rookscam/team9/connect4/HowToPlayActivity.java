package edu.msu.rookscam.team9.connect4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HowToPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
    }

    public void onHomeClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
