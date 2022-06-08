package com.aizhan.rockitapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aizhan.rockitapp.UserClasses.UserRoleType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEdit;
    private EditText passwordEdit;
    private Button btnSignin;
    private Button btnSignup;
    private FirebaseAuth auth;
    private DatabaseReference databaseUsers;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Define elements by Id
        loginEdit = (EditText) findViewById(R.id.loginEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        btnSignin = (Button) findViewById(R.id.signinBtn);
        btnSignup = (Button) findViewById(R.id.signupBtn);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("usersinfo");





        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = loginEdit.getText().toString().trim();
                String password = passwordEdit.getText().toString().trim();

                if (TextUtils.isEmpty(login)){
                    Toast.makeText(LoginActivity.this, "Enter your login",
                            Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Enter your password",
                            Toast.LENGTH_SHORT).show();
                }else {
                    auth.signInWithEmailAndPassword(login, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Signin();

                                    }else{
                                        Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null){
            //handle the user
            Signin();
        }
    }

    public void Signin(){
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i(TAG, "userid: "+userId);
        //check
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "dataSnapshot: "+dataSnapshot.toString());
                String usertype = dataSnapshot.child(userId).child("typeofuser").getValue(String.class);
                Log.i(TAG, "usertype: "+usertype);
                if (usertype.equals("musician")){
                    startActivity(new Intent(LoginActivity.this, MusicianProfileActivity.class));
                }else if (usertype.equals("client")){
                    startActivity(new Intent(LoginActivity.this, ClientProfileActivity.class));
                } else if (usertype.equals("fan")){
                    startActivity(new Intent(LoginActivity.this, FanProfileActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
