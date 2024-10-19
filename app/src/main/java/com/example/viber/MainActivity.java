package com.example.viber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    TextView text_gotoregister;
    Button btn_signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // hide action bar
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }

        TextInputEditText txt_loginemail,txt_loginpassword;
        txt_loginemail=findViewById(R.id.txt_loginemail);
        txt_loginpassword=findViewById(R.id.txt_loginpassword);

        text_gotoregister=findViewById(R.id.text_gotoregister);
        text_gotoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent class is used to redirect from one
                //activity to anther activity
                Intent in=new Intent(MainActivity.this,Registraition.class);
                startActivity(in);
            }
        });//end of onClickListener

        btn_signin=findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password;
                email=txt_loginemail.getText().toString();
                password=txt_loginpassword.getText().toString();
               // Intent home=new Intent(MainActivity.this, Home.class);
                if (email.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Please Input emial and Password",Toast.LENGTH_LONG).show();
                }
                else
                {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Intent in=new Intent(MainActivity.this,Home.class);
                                startActivity(in);
                                Toast.makeText(MainActivity.this,"User valid",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"User Not valid"
                                        ,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent in=new Intent(this,Home.class);
            startActivity(in);
        }
    }
}