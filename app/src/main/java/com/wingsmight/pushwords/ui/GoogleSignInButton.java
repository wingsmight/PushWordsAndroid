package com.wingsmight.pushwords.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.wingsmight.pushwords.R;

public class GoogleSignInButton extends FrameLayout {
    private SignInButton button;

    private AppCompatActivity resultActivity;
    private Runnable onLoaded;
    private Consumer<GoogleSignInAccount> onComplete;
    private Consumer<Exception> onFailure;

    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInActivityLauncher;


    public GoogleSignInButton(@NonNull Context context) {
        super(context);

        initView();
    }
    public GoogleSignInButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView();
    }
    public GoogleSignInButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initView();
    }


    public void init(AppCompatActivity resultActivity,
                     Runnable onLoaded,
                     Consumer<GoogleSignInAccount> onComplete,
                     Consumer<Exception> onFailure) {
        this.resultActivity = resultActivity;
        this.onLoaded = onLoaded;
        this.onComplete = onComplete;
        this.onFailure = onFailure;

        googleSignInActivityLauncher = resultActivity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                        try {
                            GoogleSignInAccount googleAccount = task.getResult(ApiException.class);
                            AuthCredential googleCredential = GoogleAuthProvider
                                    .getCredential(googleAccount.getIdToken(), null);

                            FirebaseAuth.getInstance()
                                    .signInWithCredential(googleCredential)
                                    .addOnCompleteListener(signInTask -> {
                                        if (signInTask.isSuccessful()) {
                                            onComplete.accept(googleAccount);
                                        } else {
                                            onFailure.accept(signInTask.getException());
                                        }
                                    });
                        } catch (ApiException exception) {
                            onFailure.accept(exception);
                        }
                    }
                });
    }


    private void initView() {
        inflate(getContext(), R.layout.google_sign_in_button, this);

        button = findViewById(R.id.button);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getContext().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);

//        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if (googleSignInAccount != null) {
//
//        }

        button.setOnClickListener(view -> {
            onLoaded.run();

            Intent signInIntent = googleSignInClient.getSignInIntent();
            googleSignInActivityLauncher.launch(signInIntent);
        });
    }
}
