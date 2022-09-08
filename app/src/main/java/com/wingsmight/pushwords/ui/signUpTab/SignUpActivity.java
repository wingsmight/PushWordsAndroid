package com.wingsmight.pushwords.ui.signUpTab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wingsmight.pushwords.MainActivity;
import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.Preference;
import com.wingsmight.pushwords.data.User;
import com.wingsmight.pushwords.ui.logInTab.LogInActivity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class SignUpActivity
        extends AppCompatActivity
        implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private static final String ACTION_BAR_TITLE = "Регистрация";


    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmedPasswordEditText;
    private Button signUpButton;
    private TextView loginPageBackTextView;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference database;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String confirmedPassword;
    private Date birthdate = new Date(2001, 1, 1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_up_tab);

        emailEditText = findViewById(R.id.editEmail);
        passwordEditText = findViewById(R.id.editPassword);
        confirmedPasswordEditText = findViewById(R.id.editConfirmPassword);
        signUpButton = findViewById(R.id.buttonRegister);
        loginPageBackTextView = findViewById(R.id.buttonLogin);

        // for authentication using FirebaseAuth.
        firebaseAuth = FirebaseAuth.getInstance();
        signUpButton.setOnClickListener(this);
        loginPageBackTextView.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference().child("Users");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // if (firebaseUser != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));

            finish();
        // }
    }
    @Override
    protected void onStart() {
        super.onStart();

        getSupportActionBar().setTitle(ACTION_BAR_TITLE);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.SHORT).format(mCalendar.getTime());
        birthdate = new Date(year, month, dayOfMonth);
    }

    @Override
    public void onClick(View v) {
        if (v== signUpButton){
            UserRegister();
        }else if (v== loginPageBackTextView){
            startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
        }
    }

    private void UserRegister() {
        name = nameEditText.getText().toString().trim();
        surname = surnameEditText.getText().toString().trim();
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        confirmedPassword = confirmedPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(SignUpActivity.this, "Введите имя", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(surname)) {
            Toast.makeText(SignUpActivity.this, "Введите фамилию", Toast.LENGTH_SHORT).show();
            return;
        } else if (!isValidEmail(email)) {
            Toast.makeText(SignUpActivity.this, "Введите корректную почту", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(SignUpActivity.this, "Введите пароль", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(confirmedPassword)) {
            Toast.makeText(SignUpActivity.this, "Подтвердите пароль", Toast.LENGTH_SHORT).show();
            return;
        } else if (!password.equals(confirmedPassword)) {
            Toast.makeText(SignUpActivity.this, "Введенные пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }  else if (password.length() < 6) {
            Toast.makeText(SignUpActivity.this,"Пароль слишком короткий",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Создание пользователя, пожалуйста, подождите...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    OnAuth(task.getResult().getUser());
                    progressDialog.dismiss();
                }else{
                    Exception exception = task.getException();
                    String localizedMessage = exception.getLocalizedMessage();

                    Toast.makeText(SignUpActivity.this,localizedMessage,Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void OnAuth(FirebaseUser user) {
        createAnewUser(user.getUid());

        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
    }

    private void createAnewUser(String uid) {
        User user = BuildNewuser();
        database.child(uid).setValue(user);
    }


    private User BuildNewuser(){
//        User user = new User(
//                getUserName(),
//                getUserSurname(),
//                getUserEmail(),
//                getBirthdate(),
//                new Date().getTime(),
//                //CloudStoragePlan.getSize(CloudStoragePlan.Plan.free200MB)
//        );
//
//        saveUserToPreferences(user);
//
//        CloudDatabase.addUser(user);
//
//        return user;
        return null;
    }

    private void saveUserToPreferences(User user) {
//        SharedPreferences prefs = getSharedPreferences(Preference.SHARED, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//
//        editor.putString("userFullName", user.Surname + " " + user.Name);
//        editor.putString("birthDate", DateFormat.getDateInstance(DateFormat.SHORT).format(user.getBirthDate()));
//        editor.putString("email", user.getEmail());
//
//        editor.commit();
    }

    public String getUserName() {
        return name;
    }

    public String getUserSurname() {
        return surname;
    }

    public String getUserEmail() {
        return email;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}