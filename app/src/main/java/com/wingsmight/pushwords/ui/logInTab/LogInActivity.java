package com.wingsmight.pushwords.ui.logInTab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wingsmight.pushwords.MainActivity;
import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.Preference;
import com.wingsmight.pushwords.data.User;
import com.wingsmight.pushwords.ui.signUpTab.SignUpActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {
    private static final String ACTION_BAR_TITLE = "Вход";
    public static final String TAG = "LOGIN";
    public static final String USER_EMAIL = "";


    private EditText emailEditText;
    private EditText passwordEditText;
    private Button logInButton;
    private Button signUpButton;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private String email;
    private String password;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.log_in_tab);

        logInButton = findViewById(R.id.buttonLogin);
        signUpButton = findViewById(R.id.buttonRegister);
        emailEditText = findViewById(R.id.editEmail);
        passwordEditText = findViewById(R.id.editPassword);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        authStateListener = firebaseAuth -> {
            if (firebaseUser != null) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Log.d(TAG,"AuthStateChanged:Logout");
            }
        };
        logInButton.setOnClickListener(view -> {
            userSign();
        });

        signUpButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
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
    public void onBackPressed() {
        LogInActivity.super.finish();
    }

    private void userSign() {
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LogInActivity.this, "Введен некорректная почта", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(LogInActivity.this, "Введен некорректный пароль", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Производится вход, пожалуйста, подождите...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                progressDialog.dismiss();

                Toast.makeText(LogInActivity.this, "Вход не удался, попытайтесь снова", Toast.LENGTH_SHORT).show();

            } else {
                progressDialog.dismiss();

//                CloudDatabase.loadUser(email, (Consumer<Map<String, Object>>) userData -> {
//                    User user = new User(
//                            (String)userData.get("name"),
//                            (String)userData.get("surname"),
//                            (String)userData.get("email"),
//                            new Date(),
//                            new Date().getTime(),
//                            ((Long)userData.get("storageSize")).intValue()
//                    );
//                    saveUserToPreferences(user);
//
//                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
//                    intent.putExtra(USER_EMAIL,email);
//                    startActivity(intent);
//                });
            }
        });
    }
    private void saveUserToPreferences(User user) {
//        SharedPreferences preferences = getSharedPreferences(Preference.SHARED, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//
//        editor.putString("userFullName", user.Surname + " " + user.Name);
//        editor.putString("birthDate", DateFormat.getDateInstance(DateFormat.SHORT).format(user.getBirthDate()));
//        editor.putString("email", user.getEmail());
//
//        editor.commit();
    }
}
