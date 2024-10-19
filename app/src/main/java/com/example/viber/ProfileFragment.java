package com.example.viber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile, container, false);
        TextInputEditText txt_profilename,txt_profilepassword,txt_profilegender,txt_profilemobno;
        TextView plain_profilename, plain_profileemail;
        MaterialButton btn_updateprofile;
        txt_profilename=v.findViewById(R.id.text_profilename);
        txt_profilegender=v.findViewById(R.id.text_profilegender);
        txt_profilemobno=v.findViewById(R.id.text_profilemobono);
        txt_profilepassword=v.findViewById(R.id.text_profilepassword);
        plain_profilename=v.findViewById(R.id.plain_profilename);
        plain_profileemail=v.findViewById(R.id.plain_profileemail);
        btn_updateprofile=v.findViewById(R.id.btn_updateprofile);

        TextView text_profileabout;
        ImageView img_changeabout;
        text_profileabout=v.findViewById(R.id.text_profileabout);
        img_changeabout=v.findViewById(R.id.img_profilechangeabout);

        //show details of currentuser on profile page by fetching data from database
        String currentuser= FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(currentuser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txt_profilename.setText(snapshot.child("name").getValue().toString());
                txt_profilegender.setText(snapshot.child("gender").getValue().toString());
                txt_profilemobno.setText(snapshot.child("mobno").getValue().toString());
                txt_profilepassword.setText(snapshot.child("password").getValue().toString());
                plain_profilename.setText(snapshot.child("name").getValue().toString());
                plain_profileemail.setText(snapshot.child("emailid").getValue().toString());
                if (snapshot.child("about").getValue()!=null)
                {
                    text_profileabout.setText(snapshot.child("about").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Sever error", Toast.LENGTH_LONG).show();

            }
        });
        //update name ,mobno,password,gender on click of update profile button
        btn_updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=txt_profilename.getText().toString();
                String mobno=txt_profilemobno.getText().toString();
                String gender=txt_profilegender.getText().toString();
                String password=txt_profilepassword.getText().toString();
                HashMap<String,Object> hm=new HashMap<>();
                hm.put("name",name);
                hm.put("mobno",mobno);
                hm.put("gender",gender);
                hm.put("password",password);
                FirebaseDatabase.getInstance().getReference().child("users").child(currentuser).updateChildren(hm);
                Toast.makeText(getContext(), "profile update", Toast.LENGTH_LONG).show();
            }
        });
        //change about of user on click edit about imageview
        img_changeabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit=new EditText(getContext());
                AlertDialog.Builder alertbox=new AlertDialog.Builder(getContext());
                alertbox.setTitle("What's your mood today");
                alertbox.setMessage("Enter about yourself");
                alertbox.setView(edit);
                alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap<String,Object> hm=new HashMap<>();
                        hm.put("about",edit.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("users").child(currentuser).updateChildren(hm);
                        //Toast.makeText(getContext(), "Positive", Toast.LENGTH_SHORT).show();
                    }
                });
                alertbox.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Negative", Toast.LENGTH_SHORT).show();
                    }
                });
                alertbox.show();
            }
        });
        return v;
}
}
