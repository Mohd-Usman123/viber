package com.example.viber.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devlomi.circularstatusview.CircularStatusView;
import com.example.viber.R;
import com.example.viber.model.*;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class storyadapter extends RecyclerView.Adapter<storyadapter.ViewHolder> {
    Context con;
    ArrayList<storymodel> list;
    TextView text_statususername,text_statususertime;
    CircleImageView img_statususericon;
    CircularStatusView img_statusportionicon;
    public storyadapter(Context con, ArrayList<storymodel> list)
    {
        this.con=con;
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(con).inflate(R.layout.samplestorydesign,parent,false);
        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            text_statususername.setText( " Your Status");
        }
        else {
            text_statususername.setText(list.get(position).name);
            text_statususertime.setText(list.get(position).time + "|" + list.get(position).date);
            img_statusportionicon.setPortionsColor(list.get(position).urls.size());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_statususername=itemView.findViewById(R.id.text_statususername);
            text_statususertime=itemView.findViewById(R.id.text_statusdatetime);
            img_statususericon=itemView.findViewById(R.id.img_statususericon);
            img_statusportionicon=itemView.findViewById(R.id.circular_status_view);
        }
    }
}
