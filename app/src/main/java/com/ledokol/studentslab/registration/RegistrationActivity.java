package com.ledokol.studentslab.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.ledokol.studentslab.MainActivity;
import com.ledokol.studentslab.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText nickname,email,password;
    Button signUpButton,loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        nickname = findViewById(R.id.nickname_input);
        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);

        signUpButton = findViewById(R.id.sign_up_button);
        loginButton = findViewById(R.id.login_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nickname.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                    signUp(email.getText().toString(),password.getText().toString(),nickname.getText().toString());
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        }
    }

    void updateUI(){
        Intent intent = new Intent(RegistrationActivity.this,MainActivity.class);
        startActivity(intent);
    }

    void signUp(final String email, String password, final String nickname) {
        Log.e("REGISTER LOG",email);
        Log.e("REGISTER LOG",password);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignUp", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference docRef = db.collection("Account").document(user.getUid());
                            HashMap<String,Object>  hashMap = new HashMap<>();
                            hashMap.put("name", nickname);
                            hashMap.put("email",email);
                            ArrayList arrayList = new ArrayList();
                            hashMap.put("myEvents",arrayList);
                            docRef.set(hashMap, SetOptions.merge());
                            updateUI();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                            throwError(errorCode);
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                    }
                });
    }

    private void throwError(String errorCode) {
        switch (errorCode) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(getApplicationContext(), "Неверный формат токена. Пожалуйста, проверьте данные", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(getApplicationContext(), "Пользовательский токен соответствует другой аудитории.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(getApplicationContext(), "Предоставленные учетные данные неверны или устарели.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(getApplicationContext(), "Адрес электронной почты имеет неправильный формат", Toast.LENGTH_LONG).show();
//                                    etEmail.setError("The email address is badly formatted.");
//                                    etEmail.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(getApplicationContext(), "Пароль неверен или у пользователя нет пароля.", Toast.LENGTH_LONG).show();
//                                    etPassword.setError("password is incorrect ");
//                                    etPassword.requestFocus();
//                                    etPassword.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(getApplicationContext(), "Предоставленные учетные данные не соответствуют ранее зарегистрированному пользователю.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(getApplicationContext(), "Эта операция чувствительна и требует недавней аутентификации. Войдите еще раз, прежде чем повторять этот запрос.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(getApplicationContext(), "Учетная запись уже существует с тем же адресом электронной почты, но с другими учетными данными для входа. Войдите, используя аккаунт, связанного с этим адресом электронной почты.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(getApplicationContext(), "Этот адрес электронной почты уже используется другой учетной записью.", Toast.LENGTH_LONG).show();
//                                    etEmail.setError("The email address is already in use by another account.");
//                                    etEmail.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(getApplicationContext(), "Эти учетные данные уже связаны с другой учетной записью пользователя.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(getApplicationContext(), "Учетная запись пользователя была отключена администратором.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(getApplicationContext(), "Учетные данные пользователя больше не действительны. Пользователь должен снова войти в систему.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(getApplicationContext(), "Нет записи пользователя, соответствующей этому идентификатору. Возможно, пользователь был удален.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(getApplicationContext(), "Учетные данные пользователя больше не действительны. Пользователь должен снова войти в систему.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(getApplicationContext(), "Эта операция не разрешена. Вы должны включить эту службу в консоли.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(getApplicationContext(), "Пароль должен содержать минимум 6 символов", Toast.LENGTH_LONG).show();
//                                    etPassword.setError("The password is invalid it must 6 characters at least");
//                                    etPassword.requestFocus();
                break;

        }
    }

    void logIn(){
        Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}