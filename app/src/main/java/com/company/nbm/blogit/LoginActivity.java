package com.company.nbm.blogit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmailText, loginPasswordText;
    private Button loginButton, needAccountButton;
    private ProgressBar loadingProgress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginEmailText = findViewById(R.id.login_email);
        loginPasswordText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        needAccountButton = findViewById(R.id.login_need_account_button);
        loadingProgress = findViewById(R.id.login_progress);

        needAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get email and password from the fields
                String loginEmail = loginEmailText.getText().toString();
                String loginPassword = loginPasswordText.getText().toString();

                //Check if the fields are empty
                if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPassword)) {
                    loadingProgress.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                SendToMain();
                                Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                            }
                            loadingProgress.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Please fill email and password", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        //Check if the user is logged in or not
        if (currentUser != null) {
            SendToMain();
        }
    }

    private void SendToMain() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
