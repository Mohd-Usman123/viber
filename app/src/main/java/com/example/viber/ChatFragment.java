package com.example.viber;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import com.example.viber.model.usermodel;
import com.example.viber.adaptor.useradaptor;

public class ChatFragment extends Fragment {



    public ChatFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_chat, container, false);
        //coding to fetch all users from database

        ArrayList<usermodel> list=new ArrayList<>();


        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot user :snapshot.getChildren())
                {
                    usermodel u1=new usermodel();
                    u1.uid=user.getKey().toString();
                    u1.name=user.child("name").getValue().toString();
                    u1.mobno=user.child("mobno").getValue().toString();
                    u1.emailid=user.child("emailid").getValue().toString();
                    u1.password=user.child("password").getValue().toString();
                    u1.gender=user.child("gender").getValue().toString();
                    if (!u1.uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        list.add(u1);
                    }
                    else
                    {
                        ((Home)getActivity()).getSupportActionBar().setTitle("Welcome"+u1.name);
                    }
                   // Toast.makeText(getContext(),user.getKey() , Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getContext(),user.child("name")+"" , Toast.LENGTH_SHORT).show();
                }
                //let's bind adaptor to the RecyclerView

                RecyclerView userrecycle=v.findViewById(R.id.user_recycler);
                useradaptor adaptor=new useradaptor(getContext(),list);
                userrecycle.setAdapter(adaptor);
                userrecycle.setLayoutManager(new LinearLayoutManager(getContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return v;
    }
}