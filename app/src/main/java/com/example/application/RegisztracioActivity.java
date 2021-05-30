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

public class RegisztracioActivity extends AppCompatActivity {
    private static final String PREF_KEY = RegisztracioActivity.class.getPackage().toString();
    private static final int SECRET = 99;

    EditText email;
    EditText password;
    EditText password2;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regisztracio);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);

        int secret = getIntent().getIntExtra("SECRET", 0);

        if (secret != 99) {
            finish();
        }

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        email.setText(preferences.getString("email", ""));
        password2.setText(preferences.getString("password", ""));

        mAuth = FirebaseAuth.getInstance();

    }

    private void startPractitioner(/* registered used class */) {
        Intent intent = new Intent(this, PractitionerListActivity.class);
        intent.putExtra("SECRET", SECRET);
        startActivity(intent);
    }

    public void onVissza(View view) {
        finish();
    }

    public void registerSend(View view) {
        if (password.getText().toString().equals(password2.getText().toString())) {

            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startPractitioner();
                    } else {
                        Toast.makeText(RegisztracioActivity.this, "User was't created successfully.", Toast.LENGTH_LONG).show();
                    }

                }
            });
        } else {
            Toast.makeText(RegisztracioActivity.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
        }
    }
}