package com.agriculture.ezagro;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;

public class Temperature_Analysis extends Analysis_Fragment {

    private FirebaseAuth fauth;
    private FirebaseUser user;
    private DatabaseReference fref;
    private String uid,email,parent,temp,temp2,year,month,day,temp_value,temperature;
    private GraphView graphView;
    private LineGraphSeries series;

    private float avg;
    private ArrayList<Float> list=new ArrayList<Float>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_temperature__analysis, container, false);

        graphView=(GraphView) v.findViewById(R.id.temp_graph);
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScalableY(true);
        graphView.getViewport().setScrollableY(true);

        series=new LineGraphSeries();
        graphView.addSeries(series);

        GridLabelRenderer gridLabel = graphView.getGridLabelRenderer();
        gridLabel.setPadding(32);
        gridLabel.setHorizontalAxisTitle("Month Days");
        gridLabel.setVerticalAxisTitle("Temperature in °C");

        Intent intent=getActivity().getIntent();
        email=intent.getStringExtra("email");


        fref = FirebaseDatabase.getInstance().getReference().child("Agro");

        fref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String em = ds.child("email").getValue().toString();
                    if (em.equals(email)) {
                        parent = ds.getKey();
                        fref.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot DS, @Nullable String previousChildName) {
                                String child = DS.getKey();
                                if(parent.equals(child)) {
                                    for (DataSnapshot ds1 : DS.getChildren()) {
                                        temp = ds1.getKey();
                                        if (temp.equals("DHT11")) {
                                            for (DataSnapshot ds2 : ds1.getChildren()) {
                                                temp2 = ds2.getKey();
                                                if (temp2.equals("Temperature")) {
                                                    for (DataSnapshot ds3 : ds2.getChildren()) {
                                                        year = ds3.getKey();
                                                        for (DataSnapshot ds4 : ds3.getChildren()) {
                                                            month = ds4.getKey();
                                                            DataPoint dp[] = new DataPoint[(int) ds4.getChildrenCount()];
                                                            int index=0;
                                                            for (DataSnapshot ds5 : ds4.getChildren()) {
                                                                day = ds5.getKey();

                                                                for (DataSnapshot ds6 : ds5.getChildren()) {
                                                                    temp_value = ds6.getValue(String.class);
                                                                    String temp1[] = temp_value.split("°");
                                                                    temperature = temp1[0];
                                                                    list.add(Float.parseFloat(temperature));
                                                                }
                                                                float total = 0;
                                                                for (int i = 0; i < list.size(); i++) {
                                                                    total = total + list.get(i);
                                                                }
                                                                avg = total / list.size();
                                                                list.clear();
                                                                Datapoint datapoint = new Datapoint();
                                                                datapoint.X_Value = Integer.parseInt(day);
                                                                datapoint.Y_Value = avg;
                                                                dp[index]=new DataPoint(datapoint.getX_Value(), datapoint.getY_Value());
                                                                index++;
                                                            }
                                                            series.resetData(dp);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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