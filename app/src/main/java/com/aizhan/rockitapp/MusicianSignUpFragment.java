package com.aizhan.rockitapp;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.aizhan.rockitapp.MusicianClasses.MusicianInfo;
import com.aizhan.rockitapp.MusicianClasses.MusicianMain;
import com.aizhan.rockitapp.UserClasses.UserRoleType;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicianSignUpFragment extends Fragment {
    private FirebaseAuth auth;
    private DatabaseReference databaseMusicians, databaseUsers, databaseMusmain;
    private StorageReference musstorage;
    private CircleImageView prof_photo;
    private Button choose_btn;
    private Uri imageUri;
    private EditText mname;
    private EditText msurname;
    private Spinner mage;
    private Spinner mcountry;
    private Spinner mcity;
    private EditText mlogin;
    private EditText mpassword;
    private EditText mrepassword;
    private Button mbtnNext;
    protected FragmentActivity mActivity;

    public static final int KITKAT_VALUE = 1002;
    private static final String TAG = "MyActivity";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            mActivity = (FragmentActivity) context;
        }
    }

    public MusicianSignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_musician_sign_up, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        databaseMusicians = FirebaseDatabase.getInstance().getReference("musicianinfo");
        databaseUsers = FirebaseDatabase.getInstance().getReference("usersinfo");
        databaseMusmain = FirebaseDatabase.getInstance().getReference("musmain");
        musstorage = FirebaseStorage.getInstance().getReference("profilephotos");


        //Define all elements
        prof_photo = (CircleImageView) view.findViewById(R.id.profile_image);
        choose_btn = (Button) view.findViewById(R.id.choose_btn);
        mname = (EditText) view.findViewById(R.id.nameEdit);
        msurname = (EditText) view.findViewById(R.id.surnameEdit);
        mage = (Spinner) view.findViewById(R.id.ageSpinner);
        mcountry = (Spinner) view.findViewById(R.id.countrySpinner);
        mcity = (Spinner) view.findViewById(R.id.citySpinner);
        mlogin = (EditText) view.findViewById(R.id.loginedit);
        mpassword = (EditText) view.findViewById(R.id.passwordedit);
        mrepassword = (EditText) view.findViewById(R.id.repasswordedit);
        mbtnNext = (Button) view.findViewById(R.id.nextBtn);

        //Fill Age Spinner
        List age = new ArrayList<Integer>();
        for (int i = 1; i <= 90; i++) {
            age.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                getActivity(), android.R.layout.simple_spinner_item, age);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        mage.setAdapter(spinnerArrayAdapter);

        choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFileChooser();
            }
        });

        mbtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String namet = mname.getText().toString().trim();
                final String surnamet = msurname.getText().toString().trim();
                final String countryt = mcountry.getSelectedItem().toString();
                final String cityt = mcity.getSelectedItem().toString();
                final Integer aget = Integer.parseInt(mage.getSelectedItem().toString());;
                String logint = mlogin.getText().toString().trim();
                String passwordt = mpassword.getText().toString().trim();
                String repasswordt = mrepassword.getText().toString().trim();

                if (TextUtils.isEmpty(namet)) {
                    Toast.makeText(getActivity(), "Enter your name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(surnamet)) {
                    Toast.makeText(getActivity(), "Enter your surname!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mage.getSelectedItem().toString())) {
                    Toast.makeText(getActivity(), "Select your age!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(countryt)) {
                    Toast.makeText(getActivity(), "Select your country!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(cityt)) {
                    Toast.makeText(getActivity(), "Select your city!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(logint)) {
                    Toast.makeText(getActivity(), "Create your login!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passwordt)) {
                    Toast.makeText(getActivity(), "Create your password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(repasswordt)) {
                    Toast.makeText(getActivity(), "Retype your password!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!repasswordt.equals(passwordt)){
                    Toast.makeText(getActivity(), "Passwords are not matching!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //create musician
                auth.createUserWithEmailAndPassword(logint, passwordt)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(mActivity, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                if (!task.isSuccessful()){
                                    Toast.makeText(mActivity, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    MusicianInfo musicianInfo = new MusicianInfo(namet, surnamet, aget, countryt, cityt);
                                    databaseMusicians.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(musicianInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(mActivity, "Musician info added!",
                                                        Toast.LENGTH_SHORT).show();
                                                //add Userid to separate it as Client
                                                UserRoleType userRoleType = new UserRoleType(FirebaseAuth.getInstance().getCurrentUser().getUid().toString(), "musician");
                                                databaseUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .setValue(userRoleType).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Log.i(TAG, "user role saved! ");
                                                            uploadFile(namet, surnamet);
                                                            startActivity(new Intent(mActivity, SignupPart2Activity.class));
                                                            getActivity().finish();
                                                        }else {
                                                            Log.i(TAG, task.getException().toString());
                                                        }
                                                    }
                                                });


                                            }else {
                                                Toast.makeText(mActivity , task.getException().toString(),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }

                            }
                        });
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void OpenFileChooser(){
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, KITKAT_VALUE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, KITKAT_VALUE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KITKAT_VALUE  && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            Picasso.with(getActivity()).load(imageUri).into(prof_photo);

        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = mActivity.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile(final String name, final String surname ){
        if (imageUri != null){
            final StorageReference fileRef = musstorage.child(auth.getCurrentUser().getUid()).child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));
            fileRef.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>(){

                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        Log.e(TAG, "then: " + downloadUri.toString());
                        Log.e(TAG, "then: " + downloadUri.toString());
                        MusicianMain musicianMain = new MusicianMain(name+" "+surname, downloadUri.toString(), 0, "-", 0, 0, 0);
                        databaseMusmain.child(auth.getCurrentUser().getUid()).setValue(musicianMain);
                    }else {
                        Toast.makeText(getActivity(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
            MusicianMain musicianMain = new MusicianMain(name+" "+surname, "-", 0, "-", 0, 0, 0);
            databaseMusmain.child(auth.getCurrentUser().getUid()).setValue(musicianMain);
        }
    }
}
