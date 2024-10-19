package com.example.viber;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import com.example.viber.model.*;
import com.example.viber.adaptor.*;

public class StoryFragment extends Fragment {


    public StoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_story, container, false);
        CircleImageView img_mystoryicon=v.findViewById(R.id.img_mystoryicon);
        img_mystoryicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Intent.ACTION_GET_CONTENT);
                in.setType("image/*");
                startActivityForResult(in,100);

            }
        });

        ArrayList<storymodel> list=new ArrayList<>();
        //get all stories from Database and bind in Arraylist
        FirebaseDatabase.getInstance().getReference().child("Story").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0)
                {
                    list.clear();
                    for (DataSnapshot s :snapshot.getChildren())
                    {
                        storymodel sm=new storymodel();
                        sm.name=s.child("name").getValue().toString();
                        sm.date=s.child("date").getValue().toString();
                        sm.time=s.child("time").getValue().toString();
                        sm.userid=s.getKey();
                        ArrayList<String> urls=new ArrayList<>();
                        for (DataSnapshot url : s.child("status").getChildren())
                        {
                          urls.add(url.child("url").getValue().toString());
                        }
                        sm.urls=urls;
                        list.add(sm);
                    }
                    RecyclerView recycler=v.findViewById(R.id.recycler_userstory);
                    storyadapter adapter=new storyadapter(getContext(),list);
                    recycler.setAdapter(adapter);
                    recycler.setLayoutManager(new LinearLayoutManager(getContext()));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
       return v;
    }

    //override onActivityResult to get response of calling of Explicit intent
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         Uri img= data.getData();
         CircleImageView ci=getActivity().findViewById(R.id.img_mystoryicon);
         ci.setImageURI(img);
         //set selected image into database
        Random r=new Random();
        int number=r.nextInt(40000);
        FirebaseStorage.getInstance().getReference().child("Viber"+number).putFile(img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    //download url of recent upload file from FirebaseStorage and store that URL in RealtimeDatabase
                    task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            String date=formatter.format(new Date());
                            SimpleDateFormat format1=new SimpleDateFormat("HH:mm");
                            String time=format1.format(new Date());
                            String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                            String name=FirebaseAuth.getInstance().getCurrentUser().getEmail();
                            HashMap<String, Object> hm=new HashMap<>();
                            hm.put("name",name);
                            hm.put("date",date);
                            hm.put("time",time);
                            FirebaseDatabase.getInstance().getReference().child("Story").child(uid).updateChildren(hm);
                            HashMap<String,Object> hm1=new HashMap<>();
                            hm1.put("url",uri.toString());
                            FirebaseDatabase.getInstance().getReference().child("Story").child(uid)
                                    .child("status").push().setValue(hm1);
                            Toast.makeText(getContext(),"Story Updated",Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else
                {
                    Toast.makeText(getContext(), "Server Error.Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();


    }
}