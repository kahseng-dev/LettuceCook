package sg.edu.np.mad.lettucecook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Create LettuceCook Account");
        setContentView(R.layout.activity_create_account);

        EditText username = findViewById(R.id.inputCreateUsername);
        EditText password = findViewById(R.id.inputCreatePassword);
        password.setTypeface(Typeface.DEFAULT);

        Button createButton = findViewById(R.id.createAccountButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add new User to SQLite DB
            }
        });

        TextView loginAccount = findViewById(R.id.loginAccountLink);
        loginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}