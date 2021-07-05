package sg.edu.np.mad.lettucecook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("LettuceCook Login");
        setContentView(R.layout.activity_login);

        EditText username = findViewById(R.id.inputUsername);
        EditText password = findViewById(R.id.inputPassword);
        password.setTypeface(Typeface.DEFAULT);

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Fetch User List and Authorize User Access
            }
        });
    }
}