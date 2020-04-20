package com.pratamatechnocraft.smarttempatsampah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pratamatechnocraft.smarttempatsampah.Service.SessionManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Login";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText Email;
    private EditText Password;
    private SessionManager sessionManager;
    private Button btnLogin;
    private ProgressBar progressBarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager( this );
        if (sessionManager.isLoggin()){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        Email = (EditText) findViewById(R.id.Email);
        Password = (EditText) findViewById(R.id.Password);
        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        /*btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(Login.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btnLogin.setEnabled(false);
                                showSignInOptions();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //Init provider
        providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build() // Google Builder
        );

        showSignInOptions();*/

    }

    private void proseslogin() {
        progressBarLogin.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);
        Log.d(TAG, "signIn");
        if (!validateForm()) {
            return;
        }
        String Emailstring = Email.getText().toString();
        String Passwordstring = Password.getText().toString();

        mAuth.signInWithEmailAndPassword(Emailstring, Passwordstring)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                            progressBarLogin.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                        } else {
                            progressBarLogin.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user){
        //String username = usernameFromEmail(user.getEmail());
        sessionManager.createSessionLogin(user.getUid());
        //go to mainActivity
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();

        Toast.makeText(getApplicationContext(),"Login Berhasil",Toast.LENGTH_LONG).show();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        }else {
            return email;
        }

    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(Email.getText().toString())) {
            Email.setError("Required");
            result = false;
        }else {
            Email.setError(null);
        }

        if (TextUtils.isEmpty(Password.getText().toString())) {
            Password.setError("Required");
            result = false;
        }else {
            Password.setError(null);
        }

        return result;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnLogin){
            proseslogin();
        }
    }

    /*private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.MyTheme)
                .build(),MY_REQUEST_CODE
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK)
            {
                //Get User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Show email on Toast
                Toast.makeText( this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                btnLogin.setEnabled(true);

            }
            else
            {
                Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }*/
}


