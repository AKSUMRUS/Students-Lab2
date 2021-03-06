package com.ledokol.studentslab.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ledokol.studentslab.MainActivity;
import com.ledokol.studentslab.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText email,password;
    Button signUp,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);
        signUp = findViewById(R.id.sign_up_button);
        login = findViewById(R.id.login_button);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().toString().equals("") && !password.getText().toString().equals("")) {
                    login(email.getText().toString(),password.getText().toString());
                }
            }
        });

    }

    void login(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Successfully logged in",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this,"Check your input",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void signUp(){
        Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
        startActivity(intent);
    }
}