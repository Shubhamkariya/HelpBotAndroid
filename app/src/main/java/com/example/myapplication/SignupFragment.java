package com.example.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignupFragment extends AppCompatActivity {
    public EditText emailId;
    private GpsTracker gpsTracker;
    private TextView tvLatitude,tvLongitude;
    private StorageReference storage= FirebaseStorage.getInstance().getReference();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference first, second, dbreff,dbreff_count;
    public EditText passwd;
    public EditText name,value_tx;
    public EditText Phone;
    public EditText birthdate;
    public ImageView cover;
    View btnSignUp,btnlang;
    String email,value_data;
    FirebaseAuth firebaseAuth;
    ImageView img;
    public TextView change;
    LinearLayout img1;
    UserData UserData;
    public Uri imguri,imguri1;
    StorageReference mStorageRef;

    long maxid;
    String uid1;
    String phoneNumber;

    double latitude=0,longitude = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signup);
        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.email);
        name = findViewById(R.id.name);

        Phone= findViewById(R.id.Phone);
//        img = findViewById(R.id.image1);
//        ch = findViewById(R.id.ch);
        passwd = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.upload);
        value_tx = findViewById(R.id.value);




        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);

            }
        } catch (Exception e){
            e.printStackTrace();
        }
//        dbreff_count = databaseReference.child("TOTAL");
//        dbreff_count.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String value = dataSnapshot.child("VISIT").getValue().toString();
//                value_tx.setText(value);
//                value_data = value_tx.getText().toString();
//
//               // Toast.makeText(SignupFragment.this, ""+value_tx.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });
        gpsTracker = new GpsTracker( SignupFragment.this);
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
           // Toast.makeText(SignupFragment.this, "Lan"+latitude+"lon"+longitude, Toast.LENGTH_SHORT).show();
        }else{
            gpsTracker.showSettingsAlert();
        }



        UserData=new UserData();
        mStorageRef = FirebaseStorage.getInstance().getReference("UserImage");
        dbreff = FirebaseDatabase.getInstance().getReference().child("UserData");



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailID = emailId.getText().toString();
                String paswd = passwd.getText().toString();
                String phone=Phone.getText().toString().trim();
                String names=name.getText().toString().trim();
                if(names.isEmpty())
                {
                    name.setError("Enter valid no");
                    name.requestFocus();
                    return;
                }


                phoneNumber="+44"+phone;

                if (emailID.isEmpty()) {
                    emailId.setError("Provide your Email first!");
                    emailId.requestFocus();
                } else if (paswd.isEmpty()) {
                    passwd.setError("Set your password");
                    passwd.requestFocus();
                } else if (emailID.isEmpty() && paswd.isEmpty()) {
                    Toast.makeText(SignupFragment.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(emailID.isEmpty() && paswd.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(SignupFragment.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@androidx.annotation.NonNull Task task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupFragment.this, "SignUp unsuccessful: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    uid1 = user.getUid();
                                    Fileuploader(uid1);
                                    CountInt();
                                    Intent intent =new Intent(SignupFragment.this,MainActivity.class);
                                    startActivity(intent);
                                    }
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignupFragment.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void CountInt() {
        int value1 = Integer.valueOf(value_data) +1;
        databaseReference.child("TOTAL").child("VISIT").setValue(value1).toString();
    }

    public void getLocation(View view){
        gpsTracker = new GpsTracker( SignupFragment.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            Toast.makeText(SignupFragment.this, "Lan"+latitude+"lon"+longitude, Toast.LENGTH_SHORT).show();
        }else{
            gpsTracker.showSettingsAlert();
        }
    }
    public String getExtension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimiTypeMap=MimeTypeMap.getSingleton();
        return mimiTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    public void Fileuploader(final String uid1)
    {


        UserData.setName(name.getText().toString().trim());
        UserData.setEmail(emailId.getText().toString().trim());
        UserData.setPhone(Phone.getText().toString().trim());
        UserData.setLongitude(latitude);
        UserData.setLatitude(longitude);
        UserData.setUserid(uid1);
        dbreff.child(uid1).setValue(UserData);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}