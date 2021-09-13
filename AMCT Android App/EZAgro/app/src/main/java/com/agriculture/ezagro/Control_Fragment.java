package com.agriculture.ezagro;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Control_Fragment extends Fragment {
    private FirebaseAuth fauth;
    private FirebaseUser user;
    private DatabaseReference fref;
    private Control_Agro control_agro;
    private String uid,email;
    private Switch Fld_Swtch, Pmp_Swtch, Val1_Swtch, Val2_Swtch, Auto_Swtch;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_control_, container, false);

        Fld_Swtch = (Switch) v.findViewById(R.id.fld_swtch);
        Pmp_Swtch = (Switch) v.findViewById(R.id.pmp_swtch);
        Val1_Swtch = (Switch) v.findViewById(R.id.val1_swtch);
        Val2_Swtch = (Switch) v.findViewById(R.id.val2_swtch);
        Auto_Swtch = (Switch) v.findViewById(R.id.auto_swtch);

        control_agro = new Control_Agro();

        Intent intent=getActivity().getIntent();
        email=intent.getStringExtra("email");

        fref = FirebaseDatabase.getInstance().getReference().child("Agro");


        fref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    String em = ds.child("email").getValue().toString();
                    if (em.equals(email))
                    {
                        String user=ds.getKey();

                        if(!ds.hasChild("Flood Light") || !ds.hasChild("Pump") || !ds.hasChild("Valve 1") || !ds.hasChild("Valve 2") || !ds.hasChild("Valve 3"))
                        {
                            fref.child(user).child("Flood Light").setValue("Off");
                            fref.child(user).child("Pump").setValue("Off");
                            fref.child(user).child("Valve 1").setValue("Off");
                            fref.child(user).child("Valve 2").setValue("Off");
                            fref.child(user).child("Auto System").setValue("Off");
                        }
                        Fld_Swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked == true )
                                {
                                    fref.child(user).child("Flood Light").setValue("On");
                                }
                                else
                                {
                                    fref.child(user).child("Flood Light").setValue("Off");
                                }
                            }
                        });

                        Auto_Swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked == true )
                                {
                                    fref.child(user).child("Auto System").setValue("On");
                                }
                                else
                                {
                                    fref.child(user).child("Auto System").setValue("Off");
                                }
                            }
                        });

                        Val1_Swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked == true )
                                {
                                    fref.child(user).child("Valve 1").setValue("On");
                                }
                                else
                                {
                                    fref.child(user).child("Valve 1").setValue("Off");
                                }
                            }
                        });

                        Val2_Swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked == true )
                                {
                                    fref.child(user).child("Valve 2").setValue("On");
                                }
                                else
                                {
                                    fref.child(user).child("Valve 2").setValue("Off");
                                }
                            }
                        });

                        Pmp_Swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked == true )
                                {
                                    String value1 = ds.child("Valve 1").getValue(String.class);
                                    String value2 = ds.child("Valve 2").getValue(String.class);
                                    String off="Off";
                                    if(value1.equals(off) && value2.equals(off))
                                    {
                                        fref.child(user).child("Pump").setValue("On");
                                        showDialog();
                                        fref.child(user).child("Pump").setValue("Off");
                                    }
                                    else
                                    {
                                        fref.child(user).child("Pump").setValue("On");
                                    }
                                    // fref.child(user).child("Pump").setValue("On");
                                }
                                else
                                {
                                    fref.child(user).child("Pump").setValue("Off");
                                }
                            }
                        });


                        fref.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot DS, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot DS, @Nullable String previousChildName) {
                                String fl=DS.child("Flood Light").getValue().toString();
                                String on="On";
                                String off="Off";

                                if(fl.equals(on)) {
                                    Fld_Swtch.setChecked(true);
                                }

                                else
                                {
                                    Fld_Swtch.setChecked(false);
                                }

                                String autos=DS.child("Auto System").getValue().toString();
                                if(autos.equals(on))
                                {
                                    Auto_Swtch.setChecked(true);
                                }
                                else
                                {
                                    Auto_Swtch.setChecked(false);
                                }

                                String val1=DS.child("Valve 1").getValue().toString();
                                String val2=DS.child("Valve 2").getValue().toString();
                                String pmp=DS.child("Pump").getValue().toString();


                                if(val1.equals(on))
                                {
                                    Val1_Swtch.setChecked(true);
                                }
                                else
                                {
                                    Val1_Swtch.setChecked(false);
                                }

                                if(val2.equals(on))
                                {
                                    Val2_Swtch.setChecked(true);
                                }
                                else
                                {
                                    Val2_Swtch.setChecked(false);
                                }

                                if(pmp.equals(on))
                                {
                                    if(val1.equals(off) && val2.equals(off))
                                    {
                                        Pmp_Swtch.setChecked(false);
                                    }
                                    else {
                                        Pmp_Swtch.setChecked(true);
                                    }
                                }
                                else
                                {
                                    Pmp_Swtch.setChecked(false);
                                }

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

            private void showDialog() {
                Alert_Dialog alert_dialog = new Alert_Dialog();
                alert_dialog.show(getFragmentManager(),"Alert Dialog");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return v;
    }
}