package sg.edu.np.mad.lettucecook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import sg.edu.np.mad.lettucecook.Models.DBHandler;
import sg.edu.np.mad.lettucecook.Models.User;

public class CreateAccountActivity extends AppCompatActivity {
    DBHandler dbHandler = new DBHandler(this , null, null, 1);

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

                User dbData = dbHandler.findUser(username.getText().toString());

                if (dbData == null) {
                    String dbUsername = username.getText().toString();
                    String dbPassword = password.getText().toString();

                    User dbUser = new User();
                    dbUser.setUsername(dbUsername);
                    dbUser.setPassword(dbPassword);

                    dbHandler.addUser(dbUser);
                    Toast.makeText(CreateAccountActivity.this,"Account Created!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                else {
                    Toast.makeText(CreateAccountActivity.this,"Username has been taken.\nPlease try again.", Toast.LENGTH_SHORT).show();
                }
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