package com.agriculture.ezagro;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

public class  Login extends AppCompatActivity {

    EditText Email_Login, Password_Login;
    FirebaseAuth fAuth;
    Button Login_Button, Forgotpass_Link1, Register_Link;
    CheckBox checkBox;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        /*if(fAuth.getCurrentUser() !=null)
        {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
        */
        setContentView(R.layout.activity_login);

        Email_Login = (EditText) findViewById(R.id.email_login);
        Password_Login = (EditText) findViewById(R.id.password_login);
        Login_Button = (Button) findViewById(R.id.login_button);
        Forgotpass_Link1 = (Button) findViewById(R.id.forgotpass_link1);
        Register_Link = (Button) findViewById(R.id.register_link);

        fAuth=FirebaseAuth.getInstance();

        Register_Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        Forgotpass_Link1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Reset_Password.class));
            }
        });

        Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=Email_Login.getText().toString().trim();
                String pass=Password_Login.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(getApplicationContext(), "Enter Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass))
                {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            if(pass.length()<6)
                            {
                                Password_Login.setError("Password too short, enter minimum 6 characters!!");
                            }
                            else
                            {
                                Toast.makeText(Login.this, "Authentication failed, check your Email and Password", Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Intent intent= new Intent(Login.this, MainActivity.class);
                            intent.putExtra("email",email);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
}