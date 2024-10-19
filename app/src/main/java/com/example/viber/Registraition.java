package com.example.viber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registraition extends AppCompatActivity {
    TextView text_gotologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registraition);
        // hide action bar
        if(getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
        text_gotologin=findViewById(R.id.text_gotologin);
        text_gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent class is used to redirect from one activity to another activity
                Intent in=new Intent(Registraition.this,MainActivity.class);
                startActivity(in);
            }
        });

        //save data in database
        //------get data of form to store inn database ------

        TextInputEditText text_name, text_email,text_mobno,text_password;
        text_name=findViewById(R.id.txt_name);
        text_mobno=findViewById(R.id.txt_mobno);
        text_email=findViewById(R.id.txt_email);
        text_password=findViewById(R.id.txt_password);

        MaterialButton btn_signup;
        btn_signup=findViewById(R.id.btn_Signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name,mobno,email,password;
                name=text_name.getText().toString();
                mobno=text_mobno.getText().toString();
                password=text_password.getText().toString();
                email=text_email.getText().toString();
                if (name.isEmpty() || email.isEmpty() || mobno.isEmpty() || password.isEmpty())

                {
                    Toast.makeText(Registraition.this,"Please fill all fields properly",Toast.LENGTH_LONG).show();
                }
                else {
                    //save data in database
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                String uid = task.getResult().getUser().getUid();
                                HashMap<String, String> user = new HashMap<>();
                                user.put("name", name);
                                user.put("mobno", mobno);
                                user.put("emailid", email);
                                user.put("password", password);
                                user.put("gender", "NA");
                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Registraition.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                        Intent in = new Intent(Registraition.this, MainActivity.class);
                                        startActivity(in);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                            }
                            else
                            {
                                Toast.makeText(Registraition.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Registraition.this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });//ending onclick event of signup button

        }

    }