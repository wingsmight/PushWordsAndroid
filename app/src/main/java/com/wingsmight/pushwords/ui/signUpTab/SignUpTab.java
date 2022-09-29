package com.wingsmight.pushwords.ui.signUpTab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wingsmight.pushwords.MainActivity;
import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.User;
import com.wingsmight.pushwords.data.database.CloudDatabase;
import com.wingsmight.pushwords.data.stores.UserStore;
import com.wingsmight.pushwords.handlers.Email;
import com.wingsmight.pushwords.ui.GoogleSignInButton;
import com.wingsmight.pushwords.ui.logInTab.LogInTab;

public class SignUpTab extends AppCompatActivity {
    private static final String ACTION_BAR_TITLE = "Регистрация";


    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmedPasswordEditText;
    private Button signUpButton;
    private TextView loginPageBackTextView;
    private ProgressDialog progressDialog;
    private GoogleSignInButton googleSignInButton;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_up_tab);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(SignUpTab.this, MainActivity.class));

            finish();
            return;
        }

        emailEditText = findViewById(R.id.editEmail);
        passwordEditText = findViewById(R.id.editPassword);
        confirmedPasswordEditText = findViewById(R.id.editConfirmPassword);
        signUpButton = findViewById(R.id.buttonRegister);
        signUpButton.setOnClickListener(view -> trySignUserUp());
        loginPageBackTextView = findViewById(R.id.buttonLogin);
        loginPageBackTextView.setOnClickListener(view ->
                startActivity(new Intent(SignUpTab.this, LogInTab.class)));
        progressDialog = new ProgressDialog(this);

        googleSignInButton = findViewById(R.id.googleSignInButton);
        googleSignInButton.init(this,
                this::showLoadingView,
                googleAccount -> signUp(googleAccount.getEmail()),
                this::showError);
    }
    @Override
    protected void onStart() {
        super.onStart();

        getSupportActionBar().setTitle(ACTION_BAR_TITLE);
    }
    @Override
    public void onBackPressed() { }

    private void trySignUserUp() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmedPassword = confirmedPasswordEditText.getText().toString().trim();

        if (!Email.isValid(email)) {
            Toast.makeText(SignUpTab.this, "Введите корректную почту", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(SignUpTab.this, "Введите пароль", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(confirmedPassword)) {
            Toast.makeText(SignUpTab.this, "Подтвердите пароль", Toast.LENGTH_SHORT).show();
            return;
        } else if (!password.equals(confirmedPassword)) {
            Toast.makeText(SignUpTab.this, "Введенные пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }  else if (password.length() < 6) {
            Toast.makeText(SignUpTab.this,"Пароль слишком короткий",Toast.LENGTH_SHORT).show();
            return;
        }

        showLoadingView();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                signUp(email);
            } else {
                showError(task.getException());
            }

            progressDialog.dismiss();
        });
    }
    private void signUp(String email) {
        createNewUser(email);

        startActivity(new Intent(SignUpTab.this, MainActivity.class));
    }
    private User createNewUser(String email) {
        User user = new User(email);

        UserStore.getInstance(this).setUser(user);
        CloudDatabase.addUser(user);

        return user;
    }
    private void showLoadingView() {
        progressDialog.setMessage("Создание пользователя, пожалуйста, подождите...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    private void showError(Exception exception) {
        Toast.makeText(this,
                exception.getLocalizedMessage(),
                Toast.LENGTH_SHORT).show();

        progressDialog.hide();
    }
}