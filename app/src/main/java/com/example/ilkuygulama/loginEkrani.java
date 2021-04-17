package com.example.ilkuygulama;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class loginEkrani extends AppCompatActivity {

    Button login;
    EditText email,sifre;
    TextView txtreg;
    TextView txtforgotpsw;
    private ProgressDialog loginProgress;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ekrani);
        login=findViewById(R.id.btn_login);
        email=findViewById(R.id.txt_email);
        sifre=findViewById(R.id.txt_password);
        loginProgress=new ProgressDialog(this);
        txtreg=findViewById(R.id.txt_register);
        mAuth=FirebaseAuth.getInstance();
        txtforgotpsw =findViewById(R.id.txt_passwordforgot);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail =email.getText().toString();
                String password=sifre.getText().toString();
                if(!mail.isEmpty()&&!password.isEmpty())
                {
                    loginProgress.setTitle("Oturum açýlýyor...");
                    loginProgress.setMessage("Hesabýnýza giriþ yapýlýyor lütfen bekleyiniz");
                    loginProgress.setCanceledOnTouchOutside(false);
                    loginProgress.show();
                    login_user(mail,password);
                }
            }
        });
        txtreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(loginEkrani.this,registerEkrani.class);
                startActivity(intent);
            }
        });

        createRequest();
        SignInButton signInButton = findViewById(R.id.google_signIn);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.google_signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void perform_action(View v)
    {
        if(email.getText().toString().isEmpty()){

            Toast.makeText(loginEkrani.this,"Bir Mail Adresi Giriniz!",Toast.LENGTH_SHORT).show();

        }else{

            mAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        Toast.makeText(loginEkrani.this,"Sýfýrlama Maili Gönderildi!",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(loginEkrani.this,"Sýfýrlama Mailinde hata!",Toast.LENGTH_SHORT).show();
                        Log.e("hata",task.getException().toString());

                    }

                }
            });

        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(),anaEkran.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(loginEkrani.this, "Sorry auth failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void login_user(String mail, String password) {
        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    loginProgress.dismiss();
                    Intent intent=new Intent(loginEkrani.this,anaEkran.class);
                    startActivity(intent);

                }
                else
                {
                    loginProgress.dismiss();
                    Toast.makeText(getApplicationContext(),"Giriþ Yapýlamadý: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}