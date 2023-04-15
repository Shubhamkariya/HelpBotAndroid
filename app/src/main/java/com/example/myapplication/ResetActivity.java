 package com.example.myapplication;

 import android.content.Intent;
 import android.os.Bundle;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.Toast;

 import androidx.appcompat.app.AppCompatActivity;
 import androidx.appcompat.widget.Toolbar;

 import com.google.android.gms.tasks.OnCompleteListener;
 import com.google.android.gms.tasks.Task;
 import com.google.firebase.auth.FirebaseAuth;

 public class ResetActivity extends AppCompatActivity {

        EditText send_email;
        Button btn_reset;

        FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("RESET PASSWORD");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        send_email=findViewById(R.id.send_email);
        btn_reset=findViewById(R.id.btn_reset);

        firebaseAuth= FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email =send_email.getText().toString();

                if(email.equals("")){
                    Toast.makeText(ResetActivity.this, "ENTER THE EMAIL ID ", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ResetActivity.this, "Please check your email id", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetActivity.this,LoginFragment.class));
                            }
                            else
                            {
                                String error=task.getException().getMessage();
                                Toast.makeText(ResetActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
}
