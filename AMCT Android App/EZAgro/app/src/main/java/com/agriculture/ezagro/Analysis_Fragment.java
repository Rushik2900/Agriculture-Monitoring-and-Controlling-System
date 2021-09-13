package com.agriculture.ezagro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Analysis_Fragment extends Fragment {
    private FirebaseAuth fauth;
    private FirebaseUser user;
    private DatabaseReference fref;
    private String uid,email,parent;
    private Button Anly_Temp, Anly_Hum, Anly_Soil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_analysis_, container, false);
        Anly_Temp=(Button) v.findViewById(R.id.anly_temp);
        Anly_Hum=(Button) v.findViewById(R.id.anly_hum);
        Anly_Soil=(Button) v.findViewById(R.id.anly_soil);
        Anly_Temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Temperature_Analysis temperature_analysis=new Temperature_Analysis();
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.main_rel, temperature_analysis);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        Anly_Hum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Humidity_Analysis humidity_analysis = new Humidity_Analysis();
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.main_rel, humidity_analysis);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        Anly_Soil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoilMoisture_Analysis soilMoisture_analysis = new SoilMoisture_Analysis();
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.main_rel, soilMoisture_analysis);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return v;
    }
}