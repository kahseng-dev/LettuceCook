package sg.edu.np.mad.lettucecook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("LettuceCook Login");
        setContentView(R.layout.activity_login);

        EditText username = findViewById(R.id.inputLoginUsername);
        EditText password = findViewById(R.id.inputLoginPassword);
        password.setTypeface(Typeface.DEFAULT);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Fetch User List and Authorize User Access
            }
        });

        TextView createAccount = findViewById(R.id.createAccountLink);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}