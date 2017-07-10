package edu.msu.rookscam.team9.connect4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewUserActivity extends AppCompatActivity {

    private String username = "";
    private String password1 = "";
    private String password2 = "";
    private EditText getUserName() {
        return (EditText)findViewById(R.id.createUsernameEditText);
    }
    private EditText getPsw1() {
        return (EditText)findViewById(R.id.createPasswordEditText);
    }
    private EditText getPsw2() {
        return (EditText)findViewById(R.id.createPasswordConfirmEditText);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);



        //Log.d("p1",password1);
        final Button button = (Button) findViewById(R.id.createButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onCreateUser(view);
            }
        });
    }


    public boolean createUserCheck() {
        username = getUserName().getText().toString();
        password1 = getPsw1().getText().toString();
        password2 = getPsw2().getText().toString();
        if (password1.equals(password2)) {
            Log.d("fffffffffffff",password1.toString());
            return true;
        }
        return false;
    }


    public void onCreateUser(View view) {
        //TODO
        // add create user code here

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        if (createUserCheck()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    final Cloud cloud = new Cloud();
                    Log.d("usr",username);
                    cloud.createUser(username,password1);
                }
            }).start();
        } else {
            Toast.makeText(view.getContext(), "Please changed other username or password.", Toast.LENGTH_SHORT).show();
        }


    }
}
