package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    TextView girisYapTxt,emailAdress,password,confirmPassword;
    FirebaseFirestore dataFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
        firebaseAuth = FirebaseAuth.getInstance();
        girisYapTxt = findViewById(R.id.girisYapTxt);
        emailAdress = findViewById(R.id.emailAdress);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        girisYapTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, LoginScreen.class));
                finish();
            }
        });
    }

    public void signUpClicked(View view){
        String email = emailAdress.getText().toString();
        String pass = password.getText().toString();
        String passCon=confirmPassword.getText().toString();

        if (pass.equals(passCon)){
            firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    String userId=firebaseAuth.getCurrentUser().getUid();
                    String userMail=firebaseAuth.getCurrentUser().getEmail();
                    dataFirestore=FirebaseFirestore.getInstance();
                    HashMap<String,Object> userMap=new HashMap<>();
                    userMap.put("id",userId);
                    userMap.put("isim","");
                    userMap.put("adres","");
                    userMap.put("telefon","");
                    userMap.put("email",userMail);
                    userMap.put("password",pass);
                    userMap.put("cuzdanadress","");
                    dataFirestore.collection("Users").document(userId).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(SignUp.this,"User Created",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignUp.this, LoginScreen.class));
                            finish();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUp.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });
        }else {
            Toast.makeText(SignUp.this,"Şifreler farklı",Toast.LENGTH_LONG).show();
        }

    }
}