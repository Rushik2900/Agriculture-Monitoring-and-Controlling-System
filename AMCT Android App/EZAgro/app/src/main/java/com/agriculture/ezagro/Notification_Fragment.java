package com.agriculture.ezagro;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notification_Fragment extends Fragment {
    private FirebaseAuth fauth;
    private FirebaseUser user;
    private DatabaseReference fref;
    private String uid,email,parent,temp,temp2,str,tim;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ArrayList<Model_Notification> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_notification_, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fref = FirebaseDatabase.getInstance().getReference().child("Agro");
        list=new ArrayList<>();

        Intent intent=getActivity().getIntent();
        email=intent.getStringExtra("email");
        /*
        final FragmentActivity c = getActivity();
        recyclerView= (RecyclerView) view.findViewById(R.id.not_recylcler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager= new LinearLayoutManager(c);
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        */


        final FragmentActivity c = getActivity();
        recyclerView= (RecyclerView) view.findViewById(R.id.not_recylcler);
        recyclerView.setHasFixedSize(true);
        adapter=new RecyclerAdapter(list, getContext());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        fref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String em = ds.child("email").getValue().toString();
                    if (em.equals(email)) {
                        parent = ds.getKey();
                        for (DataSnapshot ds1 : ds.getChildren()) {
                            temp = ds1.getKey();
                            if (temp.equals("Alerts")) {
                                for (DataSnapshot ds2 : ds1.getChildren()) {
                                    temp2=ds2.getKey();
                                    if(temp2.equals("Notifications"))
                                    {
                                        for (DataSnapshot ds3 : ds2.getChildren()) {
                                            for (DataSnapshot ds4 : ds3.getChildren()) {
                                                tim = ds4.getKey();
                                                for (DataSnapshot ds5 : ds4.getChildren()) {
                                                    str = ds5.getValue(String.class);
                                                }
                                                String temp1[] = str.split(":");
                                                Model_Notification model = new Model_Notification();
                                                model.alert_head = temp1[0];
                                                model.alert_desc = temp1[1];
                                                model.alert_time = tim;
                                                list.add(model);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}