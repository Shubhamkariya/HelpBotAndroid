package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private StorageReference storage = FirebaseStorage.getInstance().getReference();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference dbreff;
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference first, second;
    private DrawerLayout drawer;
    private ImageView img, check, check1;
    private TextView name, email, phone, address;
    private EditText edit_name,edit_email,edit_phone,edit_address;
    private ImageView name_img,email_img,phone_img,address_img;
    private RadioGroup radioGroup;
    private Editable add;
    private CardView update;
    private String uid1;
    private Button button;
    private RadioButton gender;
    int value1=0,value2=0,value3=0,value4=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);



        name = (TextView) v.findViewById(R.id.name);


        email = (TextView) v.findViewById(R.id.email);

        phone = (TextView) v.findViewById(R.id.Phone);


        update = (CardView) v.findViewById(R.id.upload);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid1 = user.getUid();
            first = databaseReference.child("UserData").child(uid1);
            first.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name1 = dataSnapshot.child("name").getValue().toString();
                    String phone1 = dataSnapshot.child("phone").getValue().toString();
                    name.setText(name1);
                    phone.setText(phone1);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

            String email1 = user.getEmail();
            email.setText(email1);

        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Currently you cannot update the data", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

}
