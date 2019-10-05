package com.example.reglogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class activity extends AppCompatActivity {

    EditText emailsignup ,passsignup;
    Button signup;
    TextView loginsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityy);

        emailsignup = findViewById(R.id.email_signup);
        passsignup = findViewById(R.id.pass_signup);
        signup = findViewById(R.id.btn_signup);
        loginsignup = findViewById(R.id.login_reg);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(v -> mAuth.createUserWithEmailAndPassword(emailsignup.getText().toString(), passsignup.getText().toString())
                .addOnCompleteListener(task ->
                {
                    if(task.isSuccessful())
                    {
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(activity.this, "Please check your email for verification", Toast.LENGTH_LONG).show();
                                    emailsignup.setText("");
                                    passsignup.setText("");
                                }
                                else
                                    {
                                    Toast.makeText(activity.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                    }
                    else {
                        Toast.makeText(activity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                }));

    }

    public void login(View view)
    {
        Intent intent = new Intent(activity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}

