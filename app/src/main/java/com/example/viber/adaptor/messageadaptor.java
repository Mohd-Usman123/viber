package com.example.viber.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.viber.R;
import com.example.viber.model.*;
import com.google.firebase.auth.FirebaseAuth;


public class messageadaptor extends RecyclerView.Adapter {
    Context con;
    ArrayList<messagemodel> list;
    TextView text_sendermsg, text_sendermsgtime, text_sendermsgdate,

             text_recivermsg, text_recivermsgtime, text_recivermsgdate;
    public messageadaptor(Context con,ArrayList<messagemodel> list)
    {
        this.con=con;
        this.list=list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==1)
        {
           View v= LayoutInflater.from(con).inflate(R.layout.sendermsgsampledesign,parent,false);
           return new SenderViewHolder(v);
        }
        else
        {
            View v= LayoutInflater.from(con).inflate(R.layout.recivermsgsampledesign,parent,false);
            return new ReciverViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
           if (holder.getClass()==SenderViewHolder.class)
           {
               text_sendermsg.setText(list.get(position).message);
               text_sendermsgtime.setText(list.get(position).time);
               text_sendermsgdate.setText(list.get(position).date);
           }
           else
           {
               text_recivermsg.setText(list.get(position).message);
               text_recivermsgtime.setText(list.get(position).time);
               text_recivermsgdate.setText(list.get(position).date);
           }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).sender.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            return 1;
        }
        else
        {
            return 2;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder
    {

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            text_sendermsg=itemView.findViewById(R.id.text_sendermsg);
            text_sendermsgtime=itemView.findViewById(R.id.text_sendermsgtime);
            text_sendermsgdate=itemView.findViewById(R.id.text_sendermsgdate);
        }
    }
    class ReciverViewHolder extends RecyclerView.ViewHolder
    {

        public ReciverViewHolder(@NonNull View itemView) {
            super(itemView);
            text_recivermsg=itemView.findViewById(R.id.text_recivermsg);
            text_recivermsgtime=itemView.findViewById(R.id.text_recivermsgtime);
            text_recivermsgdate=itemView.findViewById(R.id.text_recivermsgdate);
        }
    }
}
