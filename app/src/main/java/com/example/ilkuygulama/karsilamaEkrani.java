package com.example.ilkuygulama;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class karsilamaEkrani extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button register,login;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            Intent intent = new Intent(getApplicationContext(),anaEkran.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karsilama_ekrani);
        register =findViewById(R.id.btn_register);
        login=findViewById(R.id.btn_login);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(karsilamaEkrani.this,registerEkrani.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(karsilamaEkrani.this,loginEkrani.class);
                startActivity(intent);
            }
        });
    }
}