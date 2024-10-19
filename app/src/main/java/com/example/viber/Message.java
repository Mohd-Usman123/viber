package com.example.viber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import com.example.viber.model.*;
import com.google.firebase.database.ValueEventListener;
import com.example.viber.adaptor.*;

public class Message extends AppCompatActivity {
TextView text_msgusername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }
        Intent i=getIntent();
        String name=i.getStringExtra("name");
        String recieveruid=i.getStringExtra("uid");
        // Toast.makeText(this,name+uid,Toast.LENGTH_LONG).show();
        TextView text_msgusername=findViewById(R.id.text_msgusername);
        TextInputEditText txt_msgbox=findViewById(R.id.txt_msgbox);
        CircleImageView btn_msgsend=findViewById(R.id.btn_msgsend);

        text_msgusername.setText(name);

        String senderuid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        btn_msgsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=txt_msgbox.getText().toString();
                if(msg.isEmpty())
                {
                    Toast.makeText(Message.this,"Input your message",Toast.LENGTH_LONG).show();
                }
                else {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date=formatter.format(new Date());
                    SimpleDateFormat format1=new SimpleDateFormat("HH:mm");
                    String time=format1.format(new Date());
                    HashMap<String, String> hm=new HashMap<>();
                    hm.put("chat",msg);
                    hm.put("date",date);
                    hm.put("time",time);
                    hm.put("sender",senderuid);


                    FirebaseDatabase.getInstance().getReference().child("message").child(senderuid+recieveruid).
                            push().setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        FirebaseDatabase.getInstance().getReference().child("message").child(recieveruid+senderuid).
                                                push().setValue(hm);
                                        Toast.makeText(Message.this, "Message sent", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(Message.this,"server error. Try again",Toast.LENGTH_LONG).show();
                                        txt_msgbox.setText("");
                                    }
                                }
                            });

                    // Toast.makeText(Message.this,date+time,Toast.LENGTH_LONG).show();
                }
            }

        });

        //click event of back button
        ImageView btn_backbutton=findViewById(R.id.img_msgback);
        btn_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message.this.finish();
            }
        });
        //select all data from message table and bind into a arraylist
        ArrayList<messagemodel> list=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("message").child(senderuid+recieveruid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for ( DataSnapshot msg: snapshot.getChildren())
                {
                    messagemodel m1=new messagemodel();
                    m1.msgid=msg.getKey();
                    m1.message=msg.child("chat").getValue().toString();
                    m1.date=msg.child("date").getValue().toString();
                    m1.time=msg.child("time").getValue().toString();
                    m1.sender=msg.child("sender").getValue().toString();
                    list.add(m1);
                }
                RecyclerView recycler=findViewById(R.id.msg_recycler);
                messageadaptor adapter=new messageadaptor(Message.this,list);
                recycler.setAdapter(adapter);
                recycler.setLayoutManager(new LinearLayoutManager(Message.this));

                if (list.size()>1)
                {
                    recycler.scrollToPosition(list.size()-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Message.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
}
}
