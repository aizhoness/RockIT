package com.aizhan.rockitapp;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.aizhan.rockitapp.FanClasses.FanInfo;
import com.aizhan.rockitapp.FanClasses.FanMain;
import com.aizhan.rockitapp.MusicianClasses.MusicianMain;
import com.aizhan.rockitapp.UserClasses.UserRoleType;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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
public class FanSignUpFragment extends Fragment {
    private FirebaseAuth auth;
    private DatabaseReference databaseFans, databaseUsers, databaseMain;
    private StorageReference fanstorage;
    private CircleImageView prof_photo;
    private Button choose_btn;
    private Uri imageUri;
    private EditText fanname;
    private EditText fansurname;
    private Spinner fanage;
    private Spinner fancountry;
    private Spinner fancity;
    private EditText fangmail;
    private EditText fanlogin;
    private EditText fanpassword;
    private EditText fanrepassword;
    private Button saveBtn;
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

    public FanSignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fan_sign_up, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        databaseFans = FirebaseDatabase.getInstance().getReference("faninfo");
        databaseUsers = FirebaseDatabase.getInstance().getReference("usersinfo");
        databaseMain = FirebaseDatabase.getInstance().getReference("fanmain");
        fanstorage = FirebaseStorage.getInstance().getReference("profilephotos");

        //Define all elements
        prof_photo = (CircleImageView) view.findViewById(R.id.profile_image);
        prof_photo.setClipToOutline(true);
        choose_btn = (Button) view.findViewById(R.id.choose_btn);
        fanname = (EditText) view.findViewById(R.id.fan_name);
        fansurname = (EditText) view.findViewById(R.id.fan_surname);
        fanage = (Spinner) view.findViewById(R.id.fan_age_Spinner);
        fancountry = (Spinner) view.findViewById(R.id.fan_country_Spinner);
        fancity = (Spinner) view.findViewById(R.id.fan_city_Spinner);
        fangmail = (EditText) view.findViewById(R.id.fan_gmail);
        fanlogin = (EditText) view.findViewById(R.id.fan_login);
        fanpassword = (EditText) view.findViewById(R.id.fan_password);
        fanrepassword = (EditText) view.findViewById(R.id.fan_repassword);
        saveBtn = (Button) view.findViewById(R.id.save_Btn);

        //Fill Age Spinner
        List age = new ArrayList<Integer>();
        for (int i = 1; i <= 90; i++) {
            age.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                getActivity(), android.R.layout.simple_spinner_item, age);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        fanage.setAdapter(spinnerArrayAdapter);

        choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFileChooser();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String namef = fanname.getText().toString().trim();
                final String surnamef = fansurname.getText().toString().trim();
                Integer agef = 0;
                final String countryf = fancountry.getSelectedItem().toString();
                final String cityf = fancity.getSelectedItem().toString();
                String gmailf = fangmail.getText().toString().trim();
                final String loginf = fanlogin.getText().toString().trim();
                final String passwordf = fanpassword.getText().toString().trim();
                final String repasswordf = fanrepassword.getText().toString().trim();

                if (TextUtils.isEmpty(namef)) {
                    Toast.makeText(getActivity(), "Enter your name!", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(surnamef)) {
                    Toast.makeText(getActivity(), "Enter your surname!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(fanage.getSelectedItem().toString())) {
                    Toast.makeText(getActivity(), "Select your age!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(countryf)) {
                    Toast.makeText(getActivity(), "Select your country!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(cityf)) {
                    Toast.makeText(getActivity(), "Select your city!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(loginf)) {
                    Toast.makeText(getActivity(), "Create your login!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(passwordf)) {
                    Toast.makeText(getActivity(), "Create your password!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(repasswordf)) {
                    Toast.makeText(getActivity(), "Retype your password!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!repasswordf.equals(passwordf)) {
                    Toast.makeText(getActivity(), "Passwords are not matching!", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if (TextUtils.isEmpty(gmailf)) {
                        gmailf = "-";
                    }
                    agef = Integer.parseInt(fanage.getSelectedItem().toString());
                    //create fan
                    final Integer finalAgef = agef;
                    final String finalGmailf = gmailf;
                    auth.createUserWithEmailAndPassword(loginf, passwordf)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(mActivity, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    if (!task.isSuccessful()){
                                        Toast.makeText(mActivity, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }else{
                                        FanInfo fanInfo = new FanInfo(namef, surnamef, finalAgef, countryf, cityf, finalGmailf);
                                        databaseFans.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(fanInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(mActivity, "Fan info added!",
                                                            Toast.LENGTH_SHORT).show();
                                                    //add Userid to separate it as Client
                                                    UserRoleType userRoleType = new UserRoleType(FirebaseAuth.getInstance().getCurrentUser().getUid().toString(), "fan");
                                                    databaseUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .setValue(userRoleType).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Log.i(TAG, "user role saved! ");
                                                                uploadFile(namef, surnamef);
                                                                startActivity(new Intent(getActivity(), FanProfileActivity.class));
                                                                getActivity().finish();
                                                            }else {
                                                                Log.i(TAG, task.getException().toString());
                                                            }
                                                        }
                                                    });
                                                }else {
                                                    Toast.makeText(mActivity, task.getException().toString(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                    }
                                }
                            });
                }

            }
        });

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
        if (requestCode == KITKAT_VALUE && resultCode == RESULT_OK && data != null && data.getData() != null){
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
            final StorageReference fileRef = fanstorage.child(auth.getCurrentUser().getUid()).child(System.currentTimeMillis()
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
                        Uri downloadUri = task.getResult();Log.e(TAG, "then: " + downloadUri.toString());
                        Log.e(TAG, "then: " + downloadUri.toString());
                        FanMain fanMain = new FanMain(downloadUri.toString(), name+" "+surname, 0, 0, 0);
                        databaseMain.child(auth.getCurrentUser().getUid()).setValue(fanMain);
                    }else {
                        Toast.makeText(getActivity(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
            FanMain fanMain = new FanMain("-", name+" "+surname, 0, 0, 0);
            databaseMain.child(auth.getCurrentUser().getUid()).setValue(fanMain);
        }
    }
}
