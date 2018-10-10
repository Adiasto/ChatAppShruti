package com.example.adityachilka.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText name, email, password;
    Button register;
    TextView signin;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        signin = (TextView) findViewById(R.id.signin);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                registerUser(userName, userEmail, userPassword);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInActivity = new Intent(SignUp.this, SignIn.class);
                startActivity(signInActivity);
            }
        });
    }

    public void registerUser(final String name, final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignUp.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();

                            myRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                            HashMap<String,String> userObject=new HashMap<>();
                            userObject.put("email",email);
                            userObject.put("userName",name);

                            myRef.setValue(userObject);

                            Intent launchPage = new Intent(SignUp.this, MainActivity.class);
                            launchPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(launchPage);
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
