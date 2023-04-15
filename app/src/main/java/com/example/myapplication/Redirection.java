package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Redirection extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirection);
        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
//        Toast.makeText(Redirection.this, ""+user, Toast.LENGTH_SHORT).show();
        Intent I;
        if(user!=null){
            I = new Intent(Redirection.this, MainActivity.class);
        }else{
            I = new Intent(Redirection.this, LoginFragment.class);
        }
        startActivity(I);

    }
}