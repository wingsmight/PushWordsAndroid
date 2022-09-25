package com.wingsmight.pushwords.ui.signUpTab;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wingsmight.pushwords.MainActivity;
import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.User;
import com.wingsmight.pushwords.data.stores.UserStore;
import com.wingsmight.pushwords.data.database.CloudDatabase;
import com.wingsmight.pushwords.ui.logInTab.LogInTab;

public class SignUpTab extends AppCompatActivity {
    private static final String ACTION_BAR_TITLE = "Регистрация";
    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 909;


    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmedPasswordEditText;
    private Button signUpButton;
    private TextView loginPageBackTextView;
    private ProgressDialog progressDialog;
    private SignInButton googleSignInButton;

    private FirebaseAuth firebaseAuth;
    private String email;
    private String password;
    private String confirmedPassword;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    ActivityResultLauncher<Intent> googleSignInActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_up_tab);

        emailEditText = findViewById(R.id.editEmail);
        passwordEditText = findViewById(R.id.editPassword);
        confirmedPasswordEditText = findViewById(R.id.editConfirmPassword);
        signUpButton = findViewById(R.id.buttonRegister);
        signUpButton.setOnClickListener(view -> trySignUserUp());
        loginPageBackTextView = findViewById(R.id.buttonLogin);
        loginPageBackTextView.setOnClickListener(view ->
                startActivity(new Intent(SignUpTab.this, LogInTab.class)));
        progressDialog = new ProgressDialog(this);

//        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (firebaseUser != null) {
//            startActivity(new Intent(SignUpTab.this, MainActivity.class));
//
//            finish();
//        }

        configureGoogleSignIn();
    }
    @Override
    protected void onStart() {
        super.onStart();

        getSupportActionBar().setTitle(ACTION_BAR_TITLE);
    }
    @Override
    public void onBackPressed() { }

    private void configureGoogleSignIn() {
        googleSignInButton = findViewById(R.id.googleSignInButton);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount != null) {
            navigateToMainActivity();
        }

        googleSignInButton.setOnClickListener(view -> signInViaGoogle());

        googleSignInActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                        try {
                            task.getResult(ApiException.class);
                            navigateToMainActivity();
                        } catch (ApiException exception) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong: " + exception.getStatusCode(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void trySignUserUp() {
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        confirmedPassword = confirmedPasswordEditText.getText().toString().trim();

        if (!isValidEmail(email)) {
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

        progressDialog.setMessage("Создание пользователя, пожалуйста, подождите...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                OnAuth(task.getResult().getUser());
            } else {
                Exception exception = task.getException();
                String localizedMessage = exception.getLocalizedMessage();

                Toast.makeText(SignUpTab.this, localizedMessage, Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        });
    }
    private void OnAuth(FirebaseUser user) {
        createNewUser();

        startActivity(new Intent(SignUpTab.this, MainActivity.class));
    }
    private User createNewUser() {
        User user = new User(getUserEmail());

        UserStore.getInstance(this).setUser(user);
        CloudDatabase.addUser(user);

        return user;
    }
    private void navigateToMainActivity() {
        startActivity(new Intent(SignUpTab.this, MainActivity.class));

        finish();
    }
    private void signInViaGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();

        googleSignInActivityLauncher.launch(signInIntent);
    }

    public String getUserEmail() {
        return email;
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}