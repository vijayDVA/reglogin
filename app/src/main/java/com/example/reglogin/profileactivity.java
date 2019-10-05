package com.example.reglogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profileactivity extends AppCompatActivity{
    Button logoutmain;
    FirebaseAuth  mmAuth;
    FirebaseUser mUser;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileactivity);
        logoutmain = findViewById(R.id.logoutmain);

        mmAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();


    }

    public void logoutmain(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(profileactivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
