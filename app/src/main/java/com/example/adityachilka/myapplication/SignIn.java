package com.example.adityachilka.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText name, email, password;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.useremail);
        password = (EditText) findViewById(R.id.userpassword);
        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                registerUser(userEmail, userPassword);
            }
        });
    }

    public void registerUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent launchPage = new Intent(SignIn.this, MainActivity.class);
                            launchPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(launchPage);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
