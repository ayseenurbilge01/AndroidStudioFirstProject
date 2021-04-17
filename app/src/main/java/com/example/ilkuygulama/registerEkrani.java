package com.example.ilkuygulama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class registerEkrani extends AppCompatActivity {

    Button register;
    TextView ztnuye;
    EditText email,sifre,tel,sifre2,ad,soyad,refid;
    private ProgressDialog registerProgress;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ekrani);
        ad=findViewById(R.id.txt_name);
        soyad=findViewById(R.id.txt_surname);
        email =findViewById(R.id.txt_email);
        tel=findViewById(R.id.txt_phone);
        sifre=findViewById(R.id.txt_password);
        sifre2=findViewById(R.id.txt_password2);
        register=findViewById(R.id.btn_register);
        ztnuye=findViewById(R.id.txt_ztnuye);
        refid=findViewById(R.id.ref_id);
        registerProgress=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        ztnuye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(registerEkrani.this,loginEkrani.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =ad.getText().toString();
                String surname =soyad.getText().toString();
                String mail =email.getText().toString();
                String phone=tel.getText().toString();
                String password=sifre.getText().toString();
                String password2=sifre2.getText().toString();
                String ref_id=refid.getText().toString();

                if(!name.isEmpty()&&!surname.isEmpty()&&!mail.isEmpty()&&!phone.isEmpty()&&!password.isEmpty()&&!password2.isEmpty())
                {
                    if(password.equals(password2))
                    {
                        registerProgress.setTitle("Kaydediliyor...");
                        registerProgress.setMessage("Hesabýnýzý oluþturuyoruz lütfen bekleyiniz");
                        registerProgress.setCanceledOnTouchOutside(false);
                        registerProgress.show();
                        if(ref_id.isEmpty())
                        {
                            register_user(name,surname,mail,phone,password);
                        }
                        else
                        {
                            register_userref(name,surname,mail,phone,password,ref_id);
                        }
                    }
                  
                }
            }
        });
    }

    private void register_user(String name, String surname, String mail, String phone, String password) {
        mAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener((task) -> {
            if(task.isSuccessful())
            {
                String user_id=mAuth.getCurrentUser().getUid();
                mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                HashMap<String,String> userMap = new HashMap<>();
                userMap.put("name",name);
                userMap.put("surname",surname);
                userMap.put("phone",phone);
                userMap.put("image","default");
                mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            registerProgress.dismiss();
                            Intent intent=new Intent(registerEkrani.this,loginEkrani.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Hata: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            else
            {
                registerProgress.dismiss();
                Toast.makeText(getApplicationContext(),"Hata: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void register_userref(String name, String surname, String mail, String phone, String password,String ref_id) {
        mAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener((task) -> {
            if(task.isSuccessful())
            {
                String user_id=mAuth.getCurrentUser().getUid();
                mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                HashMap<String,String> userMap = new HashMap<>();
                userMap.put("name",name);
                userMap.put("surname",surname);
                userMap.put("phone",phone);
                userMap.put("image","default");
                mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Random rnd=new Random();
                            Integer newid= rnd.nextInt(2147483647);
                            ArrayList <Integer> rndsayi=new ArrayList<Integer>();
                            while (rndsayi.contains(newid))
                            {
                                    newid =rnd.nextInt();

                            }
                            rndsayi.add(newid);
                            String new_id =newid.toString();
                            mDatabase=FirebaseDatabase.getInstance().getReference().child("UserandLider").child(new_id);
                            HashMap<String,String> uM =new HashMap<>();
                            uM.put("liderid",ref_id);
                            uM.put("userid",user_id);
                            mDatabase.setValue(uM).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        registerProgress.dismiss();
                                        Intent intent=new Intent(registerEkrani.this,loginEkrani.class);
                                        startActivity(intent);
                                    }
                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Hata: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            else
            {
                registerProgress.dismiss();
                Toast.makeText(getApplicationContext(),"Hata: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}