package com.agriculture.ezagro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    private EditText Name, Email, Password, Age, Contact;
    private Button Register_Button, Forgotpass_link2, Login_link;
    private FirebaseAuth fAuth;
    private DatabaseReference fref;
    private Agro agro;
    private long maxid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Name = (EditText) findViewById(R.id.name);
        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        Age = (EditText) findViewById(R.id.age);
        Contact = (EditText) findViewById(R.id.contact);

        Register_Button = (Button) findViewById(R.id.register_button);
        Forgotpass_link2 = (Button) findViewById(R.id.forgotpass_link2);
        Login_link = (Button) findViewById(R.id.login_link);

        fAuth = FirebaseAuth.getInstance();
        agro = new Agro();
        fref = FirebaseDatabase.getInstance().getReference().child("Agro");

        fref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    maxid=(dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        Forgotpass_link2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Reset_Password.class));
            }
        });
        Register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = Email.getText().toString().trim();
                final String password = Password.getText().toString().trim();
                final String name = Name.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(), "Enter your Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "Enter your Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "Enter your Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length()<6){
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(Age.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(), "Enter your Age", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(Contact.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(), "Enter your Contact", Toast.LENGTH_SHORT).show();
                    return;
                }

                final int age =Integer.parseInt(Age.getText().toString().trim());
                final Long contact=Long.parseLong(Contact.getText().toString().trim());

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(Register.this, "Authentication Failed" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(Register.this, "Registered Successfully. Please check your Email for verification" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(Register.this, "Email not verified" + task.getException() , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            agro.setName(name);
                            agro.setAge(age);
                            agro.setContact(contact);
                            agro.setEmail(email);
                            agro.setPassword(password);

                            fref.child(String.valueOf(maxid+1)).setValue(agro);

                            //Toast.makeText(Register.this, "Data Inserted Successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Register.this, Login.class));
                            //Toast.makeText(Register.this, "Please Login to proceed", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
            }
        });
    }
}