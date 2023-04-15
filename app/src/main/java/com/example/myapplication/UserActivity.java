package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {
    Button btnLogOut;
    TextView email,name,uid;
    ImageView img;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        email=findViewById(R.id.email);
        name=findViewById(R.id.name);
        uid=findViewById(R.id.uid);
        img=findViewById(R.id.img);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name1 = user.getDisplayName();
            name.setText(name1);
            String email1 = user.getEmail();
            email.setText(email1);
            Uri photoUrl1 = user.getPhotoUrl();
            img.setImageURI(photoUrl1);
            // Check if user's email is verified
            boolean emailVerified1= user.isEmailVerified();
            String uid1 = user.getUid();
            uid.setText(uid1);

        }

        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}