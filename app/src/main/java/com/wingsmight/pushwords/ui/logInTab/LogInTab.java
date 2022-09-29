package com.wingsmight.pushwords.ui.logInTab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wingsmight.pushwords.MainActivity;
import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.database.CloudDatabase;
import com.wingsmight.pushwords.data.stores.UserStore;
import com.wingsmight.pushwords.ui.GoogleSignInButton;
import com.wingsmight.pushwords.ui.signUpTab.SignUpTab;

public class LogInTab extends AppCompatActivity {
    public static final String TAG = "LogInTab";
    private static final String ACTION_BAR_TITLE = "Вход";


    private EditText emailEditText;
    private EditText passwordEditText;
    private Button logInButton;
    private Button signUpButton;
    private ProgressDialog progressDialog;
    private GoogleSignInButton googleSignInButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.log_in_tab);

        logInButton = findViewById(R.id.buttonLogin);
        logInButton.setOnClickListener(view -> logUserIn());

        signUpButton = findViewById(R.id.buttonRegister);
        signUpButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpTab.class);
            startActivity(intent);
        });

        emailEditText = findViewById(R.id.editEmail);
        passwordEditText = findViewById(R.id.editPassword);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        authStateListener = firebaseAuth -> {
            if (firebaseUser != null) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Log.d(TAG,"AuthStateChanged:Logout");
            }
        };

        googleSignInButton = findViewById(R.id.googleSignInButton);
        googleSignInButton.init(this,
                this::showLoadingView,
                googleAccount -> logIn(googleAccount.getEmail()),
                this::showError);
    }
    @Override
    protected void onStart() {
        super.onStart();

        getSupportActionBar().setTitle(ACTION_BAR_TITLE);

        firebaseAuth.removeAuthStateListener(authStateListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
    @Override
    public void onBackPressed() { }

    private void logUserIn() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LogInTab.this, "Введена некорректная почта", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(LogInTab.this, "Введен некорректный пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoadingView();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progressDialog.dismiss();

            if (!task.isSuccessful()) {
                Toast.makeText(LogInTab.this, "Вход не удался, попытайтесь снова", Toast.LENGTH_SHORT).show();
            } else {
                logIn(email);
            }
        });
    }
    private void showLoadingView() {
        progressDialog.setMessage("Создание пользователя, пожалуйста, подождите...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    private void logIn(String email) {
        CloudDatabase.getUser(email, user -> {
            UserStore.getInstance(this).setUser(user);

            startActivity(new Intent(this, MainActivity.class));
        });
    }
    private void showError(Exception exception) {
        Toast.makeText(this,
                exception.getLocalizedMessage(),
                Toast.LENGTH_SHORT).show();

        progressDialog.hide();
    }
}
