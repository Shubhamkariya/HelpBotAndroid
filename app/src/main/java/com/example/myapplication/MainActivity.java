package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ChatBot.MainActivity_chat;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String token1;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    String uid1;
    TextView txtLat;
    String lat;
    String provider,value_data;
    String email1;
    int backButtonCount = 0;
    private StorageReference storage = FirebaseStorage.getInstance().getReference();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference first, second, dbreff;
    private DrawerLayout drawer;
    private FirebaseUser fUser;
    private TextView name, email,value_tx;
    private ImageView img;
    private LinearLayout header1, header2;
    private StorageReference mStorageRef;
    private GpsTracker gpsTracker;
    private TextView tvLatitude,tvLongitude;
    private static final int CALL_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("E-NUFF");
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        value_tx = (TextView) header.findViewById(R.id.value);


        LinearLayout header2 = (LinearLayout) header.findViewById(R.id.header2);

        Menu menu = navigationView.getMenu();

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);

            }
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        String another ;
//        dbreff = databaseReference.child("TOTAL");
//        dbreff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String value = dataSnapshot.child("COUNT").getValue().toString();
//                value_tx.setText(value);
//                value_data = value_tx.getText().toString();
//                //Toast.makeText(MainActivity.this, ""+value_tx.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        {

            email1 = user.getEmail();
            header2.setVisibility(View.VISIBLE);
            String uid1 = user.getUid();
            name = (TextView) header.findViewById(R.id.name);
            email = (TextView) header.findViewById(R.id.email);
//            img = (ImageView) header.findViewById(R.id.profile);
            MenuItem nav_signout = menu.findItem(R.id.nav_signout);
            nav_signout.setVisible(true);


            first = databaseReference.child("UserData").child(uid1);
            first.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name1 = dataSnapshot.child("name").getValue().toString();
                    name.setText(name1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

            email.setText(email1);
            Uri photoUrl1 = user.getPhotoUrl();
            // Check if user's email is verified
            boolean emailVerified1 = user.isEmailVerified();

        }

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart: {
                checkPermission(Manifest.permission.CALL_PHONE, CALL_PERMISSION_CODE);
                String number ="911";
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+number));
                startActivity(callIntent);
            }
            break;
            case R.id.fav: {
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setType("text/plain");
                sendIntent.putExtra("jid", "447459159410" + "@s.whatsapp.net");// here 44 is country code
                sendIntent.putExtra(Intent.EXTRA_TEXT,"Hello, I need help.");
                startActivity(sendIntent);
            }
            break;

        }
        return super.onOptionsItemSelected(item);
    }
    // This function is called when user accept or decline the permission.
// Request Code is used to check which permission called this function.
// This request code is provided when user is prompt for permission.

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }

    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CALL_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "CALL Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(MainActivity.this, "CALL Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer((GravityCompat.START));
        } else {
          String value = this.getClass().getSimpleName();
            if (value.equals("MainActivity")) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                backButtonCount++;
                ////startActivity(Intent(MainActivity.this, MainActivity.class))
                Toast.makeText(this,this.getClass().getSimpleName()+"Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();


            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_chat: {
//                int value = Integer.valueOf(value_data) +1;
//                databaseReference.child("TOTAL").child("COUNT").setValue(value).toString();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    startActivity(new Intent(MainActivity.this, MainActivity_chat.class));
                }
            }
            break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;

            case R.id.nav_signout: {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginFragment.class));
                finish();
                Toast.makeText(this, "Signing out", Toast.LENGTH_LONG).show();
                break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}