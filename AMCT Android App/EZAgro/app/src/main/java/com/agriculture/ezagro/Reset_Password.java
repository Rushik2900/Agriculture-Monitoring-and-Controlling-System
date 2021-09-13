package com.agriculture.ezagro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Reset_Password extends AppCompatActivity {

    EditText Email_Resetpass;
    Button Reset_Pass_Button, Back_Button;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset__password);

        Email_Resetpass=(EditText) findViewById(R.id.email_resetpass);
        Reset_Pass_Button= (Button) findViewById(R.id.reset_pass_button);
        Back_Button= (Button) findViewById(R.id.back_button);

        fAuth=FirebaseAuth.getInstance();

        Back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Reset_Password.this,Login.class));
            }
        });

        Reset_Pass_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email= Email_Resetpass.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(getApplication(), "Enter your registered Email Id", Toast.LENGTH_SHORT).show();
                    return;
                }

                fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Reset_Password.this, "We have sent you instructions to reset password!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Reset_Password.this,"Failed to send reset Email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}