package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.DBHandler;
import sg.edu.np.mad.lettucecook.models.User;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextUsername, editTextEmail, editTextPassword;
    private TextView loginAccount;
    private Toolbar toolbar;
    private Button createButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_black_arrow_back);
        toolbar.findViewById(R.id.app_logo).setVisibility(View.INVISIBLE);

        createButton = (Button) findViewById(R.id.create_account_button);
        createButton.setOnClickListener(this);

        loginAccount = (TextView) findViewById(R.id.login_link);
        loginAccount.setOnClickListener(this);

        editTextUsername = (EditText) findViewById(R.id.create_account_username);
        editTextEmail = (EditText) findViewById(R.id.create_account_email);
        editTextPassword = (EditText) findViewById(R.id.create_account_password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Text change listener to watch if all text fields are filled to enable button.
        editTextUsername.addTextChangedListener(createTextWatch);
        editTextEmail.addTextChangedListener(createTextWatch);
        editTextPassword.addTextChangedListener(createTextWatch);
    }

    private TextWatcher createTextWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String usernameInput = editTextUsername.getText().toString().trim();
            String emailInput = editTextEmail.getText().toString().trim();
            String passwordInput = editTextPassword.getText().toString().trim();

            createButton.setEnabled(!usernameInput.isEmpty() && !emailInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_account_button:
                registerUser();
                break;

            case R.id.login_link:
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
    }

    private void registerUser() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Min password length is 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(username, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CreateAccountActivity.this, "Created Account Successfully!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(CreateAccountActivity.this, MainActivity.class));
                                        progressBar.setVisibility(View.GONE);
                                    }

                                    else {
                                        Toast.makeText(CreateAccountActivity.this, "Failed to register!\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }

                        else {
                            Toast.makeText(CreateAccountActivity.this, "Failed to register!\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    // if the user clicks on the back button in the toolbar, bring them back to main activity.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}