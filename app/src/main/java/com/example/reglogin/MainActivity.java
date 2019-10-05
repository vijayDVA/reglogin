package com.example.reglogin;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
{

    static final int GOOGLE_SIGN_IN = 123;
    FirebaseAuth mAuth;
    Button btn_login, btn_logout ,login;
    TextView text, forgot , signupplz;
    EditText email_login, pass_login;
    ImageView image;
    ProgressBar progressBar;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forgot = findViewById(R.id.forgot);
        login = findViewById(R.id.btn_login);
        email_login = findViewById(R.id.email_login);
        pass_login = findViewById(R.id.pass_login);
        signupplz = findViewById(R.id.signupplz);


        btn_login = findViewById(R.id.login_google);
        btn_logout = findViewById(R.id.logout);
        text = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progress_circular);
        image = findViewById(R.id.image);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btn_login.setOnClickListener(v -> SignInGoogle());
        btn_logout.setOnClickListener(v -> Logout());

        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }
    }

    public void SignInGoogle()
    {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            progressBar.setVisibility(View.INVISIBLE);
            try
            {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null)
                    firebaseAuthWithGoogle(account);
            } catch (ApiException e)
            {
                Log.w("TAG", "Google sign in failed", e);

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account)
    {

        Log.d("TAG", "firebaseAuthWithGoogle: " + account.getId() );

        AuthCredential credential = GoogleAuthProvider.getCredential(account .getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);

                        Log.d("TAG", "signInWithCredential:success");

                        FirebaseUser user = mAuth.getCurrentUser();

                        Toast.makeText(this, "Successfull",
                                Toast.LENGTH_SHORT).show();
                        updateUI(user);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);

                        Log.w("TAG", "signInWithCredential:failure", task.getException());

                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                });


    }


    private void updateUI(FirebaseUser user)
    {
        if (user != null && user.isEmailVerified()) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String photo = String.valueOf(user.getPhotoUrl());

            text.append("Info : \n");
            text.append(name + "\n");
            text.append(email);
            Picasso.get().load(photo).into(image);
            btn_logout.setVisibility(View.VISIBLE);

            login.setVisibility(View.INVISIBLE);
            forgot.setVisibility(View.INVISIBLE);
            email_login.setVisibility(View.INVISIBLE);
            pass_login.setVisibility(View.INVISIBLE);
            btn_login.setVisibility(View.INVISIBLE);
        } else {
            text.setText("Login\n");
            Picasso.get().load(R.drawable.ic_world).into(image);
            btn_logout.setVisibility(View.INVISIBLE);
            btn_login.setVisibility(View.VISIBLE);

            login.setVisibility(View.VISIBLE);
            forgot.setVisibility(View.VISIBLE);
            email_login.setVisibility(View.VISIBLE);
            pass_login.setVisibility(View.VISIBLE);

        }
    }

    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> updateUI(null));
    }



    public void signup(View view)
    {
        Intent intent = new Intent(MainActivity.this, activity.class);
        startActivity(intent);
        finish();

    }

    public boolean logintoapp(View view) {
        String email_logi = email_login.getText().toString().trim();
        String pass_logi = pass_login.getText().toString().trim();

        if (email_logi.isEmpty())
        {
            email_login.setError("field is required");
            return false;
        } else if(pass_logi.isEmpty())
        {
            pass_login.setError("field is required");
            return false;
        }
        else { mAuth.signInWithEmailAndPassword(email_logi, pass_logi)
                    .addOnCompleteListener(task ->
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            if(mAuth.getCurrentUser().isEmailVerified())
                            {
                                Intent intent = new Intent(MainActivity.this, profileactivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(MainActivity.this, "please verify your email address", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);

                            }

                        } else {
                            Toast.makeText(MainActivity.this, "invalid", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

        }
        return false;
    }

    public boolean fotgot(View view) {
        String email_logi = email_login.getText().toString().trim();

        if (email_logi.isEmpty()) {
            email_login.setError("field is required");
            return false;
        }
        else {

            mAuth.sendPasswordResetEmail(email_login.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful())
                            Toast.makeText(MainActivity.this, "Password send to your email", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                    });


        }
        return false;
    }
}
