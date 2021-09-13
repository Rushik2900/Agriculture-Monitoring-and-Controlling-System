package com.agriculture.ezagro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Home_Fragment extends Fragment {
    private FirebaseAuth fauth;
    private FirebaseUser user;
    private DatabaseReference fref;
    private String uid,email,parent,year,month,day;
    public TextView Home_Name,Home_Temp,Home_Moist, Home_Hum, Home_Tank;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home_, container, false);

        Home_Name=(TextView) v.findViewById(R.id.home_name);
        Home_Temp=(TextView) v.findViewById(R.id.home_tmp);
        Home_Hum= (TextView) v.findViewById(R.id.home_hum);
        Home_Moist=(TextView) v.findViewById(R.id.home_moist);
        Home_Tank= (TextView) v.findViewById(R.id.home_tank);

        Intent intent=getActivity().getIntent();
        email=intent.getStringExtra("email");

        fref = FirebaseDatabase.getInstance().getReference().child("Agro");
        fref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    String em = ds.child("email").getValue().toString();
                    if(em.equals(email))
                    {
                        parent=ds.getKey();
                        Home_Name.setText(ds.child("name").getValue(String.class));
                        fref.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot DS, @Nullable String previousChildName) {
                                String child = DS.getKey();
                                if(parent.equals(child)) {
                                    for(DataSnapshot ds1 : DS.getChildren()) {
                                        String temp = ds1.getKey();
                                        if (temp.equals("DHT11")) {
                                            for (DataSnapshot ds2 : ds1.getChildren()) {
                                                String temp1 = ds2.getKey();
                                                if (temp1.equals("Temperature")) {
                                                    for (DataSnapshot ds3 : ds2.getChildren()) {
                                                        year = ds3.getKey();
                                                        for(DataSnapshot ds4 : ds3.getChildren()) {
                                                            month = ds4.getKey();
                                                            for (DataSnapshot ds5 : ds4.getChildren()) {
                                                                day = ds5.getKey();
                                                                for (DataSnapshot ds6 : ds5.getChildren()) {
                                                                    Home_Temp.setText(ds6.getValue(String.class));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                String humi1 = ds2.getKey();
                                                if (humi1.equals("Humidity")) {
                                                    for (DataSnapshot ds3 : ds2.getChildren()) {
                                                        year = ds3.getKey();
                                                        for(DataSnapshot ds4 : ds3.getChildren()) {
                                                            month = ds4.getKey();
                                                            for (DataSnapshot ds5 : ds4.getChildren()) {
                                                                day = ds5.getKey();
                                                                for (DataSnapshot ds6 : ds5.getChildren()) {
                                                                    Home_Hum.setText(ds6.getValue(String.class));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                String mois1 = ds2.getKey();
                                                if (mois1.equals("Soil Moisture")) {
                                                    for (DataSnapshot ds3 : ds2.getChildren()) {
                                                        year = ds3.getKey();
                                                        for(DataSnapshot ds4 : ds3.getChildren()) {
                                                            month = ds4.getKey();
                                                            for (DataSnapshot ds5 : ds4.getChildren()) {
                                                                day = ds5.getKey();
                                                                for (DataSnapshot ds6 : ds5.getChildren()) {
                                                                    Home_Moist.setText(ds6.getValue(String.class));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                String tank1 = ds2.getKey();
                                                if (tank1.equals("Tank Water Level")) {
                                                    for (DataSnapshot ds3 : ds2.getChildren()) {
                                                        Home_Tank.setText(ds3.getValue(String.class));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot DS, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot DS) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot DS, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return v;
    }

}