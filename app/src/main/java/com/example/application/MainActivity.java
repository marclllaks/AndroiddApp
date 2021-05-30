package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final int SECRET = 99;
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    EditText email;
    EditText password;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
    }

    private void startPractitioner() {
        Intent intent = new Intent(this, PractitionerListActivity.class);
        intent.putExtra("SECRET", SECRET);
        startActivity(intent);
    }



    public void login(View view) {
        EditText userEmail = findViewById(R.id.email);
        EditText userPassword = findViewById(R.id.password);

        mAuth.signInWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startPractitioner();
                } else {
                    Toast.makeText(MainActivity.this, "Failed login ", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void register(View view) {

        Intent intent = new Intent(this, RegisztracioActivity.class);
        intent.putExtra("SECRET", 99);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.apply();

    }
}