package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import sg.edu.np.mad.lettucecook.R;

public class LoginActivity extends AppCompatActivity implements  View.OnClickListener {
    Context mContext = this;

    private TextView createAccount, forgotPassword;
    private EditText editTextEmail, editTextPassword;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // setup create account on click
        createAccount = (TextView) findViewById(R.id.create_account_link);
        createAccount.setOnClickListener(this);

        // setup forgot password on click
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(this);

        // setup login button on click
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        // setup EditTexts for email and password
        editTextEmail = (EditText) findViewById(R.id.login_email);
        editTextPassword = (EditText) findViewById(R.id.login_password);

        // setup progress bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // firebase authentication instance
        mAuth = FirebaseAuth.getInstance();

        // Text change listener to watch if both text fields are filled to enable button.
        editTextEmail.addTextChangedListener(loginTextWatch);
        editTextPassword.addTextChangedListener(loginTextWatch);

        // navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.account);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.browse:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.account:
                        return true;

                    // if the user clicks on create recipe
                    case R.id.create_recipe:
                        // Get userID from Firebase
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user == null) {
                            Toast.makeText(mContext, "Please Login to use this feature", Toast.LENGTH_LONG).show();
                            return false;
                        }

                        // Bring user to CreateRecipeActivity
                        startActivity(new Intent(getApplicationContext(), CreateRecipeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.shoppingList:
                        startActivity(new Intent(getApplicationContext(), ShoppingListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_account_link:
                startActivity(new Intent(this, CreateAccountActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.forgot_password:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.login_button:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // check if email is in email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        // check if password is less than 6 characters
        if (password.length() < 6) {
            editTextPassword.setError("Min password length is 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(mContext, MainActivity.class));
                    progressBar.setVisibility(View.GONE);
                }

                else {
                    Toast.makeText(mContext, "Failed to login!\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private TextWatcher loginTextWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String emailInput = editTextEmail.getText().toString().trim();
            String passwordInput = editTextPassword.getText().toString().trim();

            loginButton.setEnabled(!emailInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    @Override
    public void onBackPressed() { }
}