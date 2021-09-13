package com.agriculture.ezagro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    ArrayList<Model_Notification> list;
    Context context;

    public RecyclerAdapter(ArrayList<Model_Notification> list, Context context) {
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alerts, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Model_Notification model=list.get(position);
        holder.Not_Head.setText(model.getAlert_head());
        holder.Not_Desc.setText(model.getAlert_desc());
        holder.Not_Time.setText(model.getAlert_time());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView Not_Head,Not_Desc,Not_Time;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            Not_Head= itemView.findViewById(R.id.not_head);
            Not_Desc= itemView.findViewById(R.id.not_desc);
            Not_Time= itemView.findViewById(R.id.not_time);
        }
    }
}
