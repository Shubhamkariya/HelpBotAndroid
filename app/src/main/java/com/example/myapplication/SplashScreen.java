package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {
    ImageView bgapp,clover,txtimg;
    LinearLayout textsplash, texthome,menu,opn1,opn2,opn3,opn4;
    Animation frombottom;
    String email1;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference first;
    TextView txtname;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        frombottom=AnimationUtils.loadAnimation(this,R.anim.fromottom);
        bgapp=(ImageView) findViewById(R.id.bgapp);
        clover=(ImageView) findViewById(R.id.clover);
        txtname=(TextView) findViewById(R.id.name);
        txtimg=(ImageView) findViewById(R.id.img);
        textsplash=(LinearLayout) findViewById(R.id.textsplash);
        texthome=(LinearLayout) findViewById(R.id.texthome);


        bgapp.animate().translationY(-1900).setDuration(800).setStartDelay(300);
        clover.animate().alpha(0).setDuration(800).setStartDelay(600);

        textsplash.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(300);

        texthome.startAnimation(frombottom);

        Log.d("MainActivity1:", "onCreate: created activity_main.xml UI succesfully.");



        new Timer().schedule(new TimerTask(){
            public void run() {
                Intent I = new Intent(SplashScreen.this, LoginFragment.class);
                   startActivity(I);
                    finish();
                Log.d("MainActivity1.java:", "onCreate: waiting 5 seconds for MainActivity... loading PrimaryActivity.class"+email1);
            }
        }, 1);
    }
}
