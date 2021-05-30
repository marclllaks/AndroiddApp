package com.example.application;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;

import static java.lang.Integer.min;
import static java.lang.Integer.parseInt;

public class ElementActivity extends AppCompatActivity {
    private static final String PREF_KEY = RegisztracioActivity.class.getPackage().toString();
    private static final int SECRET = 99;

    EditText name;
    TextView id;
    CheckBox active;
    EditText telecom;
    EditText address;
    Spinner gender;
    EditText birthDate;
    EditText communication;
    Spinner qualification;
    EditText photo;


    private String docId;


    private FirebaseFirestore mFireStore;
    private CollectionReference mItems;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    public void onVissza(View view){
        finish();
    }
    public void onMentes(View view){
        mItems.document(docId).set(new PractitionerItem(name.getText().toString(), parseInt(id.getText().toString()), active.isChecked(), telecom.getText().toString(), address.getText().toString(), gender.getSelectedItem().toString(), new Date(birthDate.getText().toString()), photo.getText().toString(), communication.getText().toString(), qualification.getSelectedItem().toString()));
        finish();
    }
    public void onTorles(View view){
        mItems.document(docId).delete();
        finish();
    }


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element);
        name = findViewById(R.id.textView);
        id = findViewById(R.id.textView3);
        active = findViewById(R.id.textView4);
        telecom = findViewById(R.id.textView5);
        address = findViewById(R.id.textView6);
        gender = findViewById(R.id.textView7);
        birthDate = findViewById(R.id.textView8);
        communication = findViewById(R.id.textView9);
        qualification = findViewById(R.id.textView10);
        photo = findViewById(R.id.textView20);


        int secret = getIntent().getIntExtra("SECRET", 0);

        if (secret != 99){
            finish();
        }

        mFireStore = FirebaseFirestore.getInstance();
        mItems = mFireStore.collection("items");

        mItems.whereEqualTo("identifier", getIntent().getIntExtra("ID", 1)).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                PractitionerItem item = document.toObject(PractitionerItem.class);
                name.setText(item.getName().toString());
                id.setText(item.getIdentifier().toString());
                active.setChecked(item.isActive());
                telecom.setText(item.getTelecom().toString());
                address.setText(item.getAddress().toString());
                gender.setSelection(((ArrayAdapter)gender.getAdapter()).getPosition(item.getGender()));
                birthDate.setText(item.getBirthDate().toString());
                communication.setText(item.getCommunication().toString());
                qualification.setSelection(((ArrayAdapter)qualification.getAdapter()).getPosition(item.getQualification()));
                photo.setText(item.getPhoto());
                docId=document.getId();
            }
        });

    }





}
