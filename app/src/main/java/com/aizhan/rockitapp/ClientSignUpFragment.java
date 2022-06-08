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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.aizhan.rockitapp.ClientClasses.ClientInfo;
import com.aizhan.rockitapp.ClientClasses.ClientPhoto;
import com.aizhan.rockitapp.MusicianClasses.MusicianMain;
import com.aizhan.rockitapp.UserClasses.UserRoleType;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClientSignUpFragment extends Fragment {
    private FirebaseAuth auth;
    private DatabaseReference databaseClients, databaseUsers, databasePhoto;
    private StorageReference clientstorage;
    private EditText client_name;
    private Spinner client_type;
    private CircleImageView prof_photo;
    private Button choose_btn;
    private Uri imageUri;
    private Spinner mcountry;
    private Spinner mcity;
    private EditText mphone;
    private EditText mgmail;
    private EditText mfacebook;
    private EditText mSite;
    private EditText mInsta;
    private EditText mlogin;
    private EditText mpassword;
    private EditText mrepassword;
    private Button mbtnSave;
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

    public ClientSignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_sign_up, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        databaseClients = FirebaseDatabase.getInstance().getReference("clientinfo");
        databaseUsers = FirebaseDatabase.getInstance().getReference("usersinfo");
        databasePhoto = FirebaseDatabase.getInstance().getReference("clientphoto");
        clientstorage = FirebaseStorage.getInstance().getReference("profilephotos");

        //Define all elements
        prof_photo = (CircleImageView) view.findViewById(R.id.profile_image);
        prof_photo.setClipToOutline(true);
        choose_btn = (Button) view.findViewById(R.id.choose_btn);
        client_name = (EditText) view.findViewById(R.id.client_Edit);
        client_type = (Spinner) view.findViewById(R.id.client_type_Spinner);
        mcountry = (Spinner) view.findViewById(R.id.country_Spinner);
        mcity = (Spinner) view.findViewById(R.id.city_Spinner);
        mphone = (EditText) view.findViewById(R.id.phone_edit);
        mgmail = (EditText) view.findViewById(R.id.gmail_edit);
        mSite = (EditText) view.findViewById(R.id.site_edit);
        mInsta = (EditText) view.findViewById(R.id.insta_edit);
        mfacebook = (EditText) view.findViewById(R.id.facebook_edit);
        mlogin = (EditText) view.findViewById(R.id.login_edit);
        mpassword = (EditText) view.findViewById(R.id.password_edit);
        mrepassword = (EditText) view.findViewById(R.id.repassword_edit);
        mbtnSave = (Button) view.findViewById(R.id.save_Btn);

        choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFileChooser();
            }
        });

        //Save Client Data
        mbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String clientname = client_name.getText().toString().trim();
                final String clienttype = client_type.getSelectedItem().toString();
                final String clientcountry = mcountry.getSelectedItem().toString();
                final String clientcity = mcity.getSelectedItem().toString();
                final String clientlogin = mlogin.getText().toString().trim();
                final String clientpassword = mpassword.getText().toString().trim();
                final String clientrepass = mrepassword.getText().toString().trim();
                String clientphone = mphone.getText().toString().trim();
                String clientgmail = mgmail.getText().toString().trim();
                String clientfacebook = mfacebook.getText().toString().trim();
                String clientsite = mSite.getText().toString().trim();
                String clientinsta = mInsta.getText().toString().trim();

                //check the fields
                if (TextUtils.isEmpty(clientphone)) {
                    clientphone = "-";
                }
                if (TextUtils.isEmpty(clientgmail)) {
                    clientgmail = "-";
                }
                if (TextUtils.isEmpty(clientfacebook)) {
                    clientfacebook = "-";
                }
                if (TextUtils.isEmpty(clientsite)){
                    clientsite = "-";
                }
                if (TextUtils.isEmpty(clientinsta)){
                    clientinsta = "-";
                }

                if (TextUtils.isEmpty(clientname)) {
                    Toast.makeText(getActivity(), "Enter the client name!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(clienttype)) {
                    Toast.makeText(getActivity(), "Select the client type!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(clientcountry)) {
                    Toast.makeText(getActivity(), "Select the country!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(clientcity)) {
                    Toast.makeText(getActivity(), "Select the city!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(clientlogin)) {
                    Toast.makeText(getActivity(), "Enter the login!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(clientrepass)) {
                    Toast.makeText(getActivity(), "Retype your password!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!clientrepass.equals(clientpassword)){
                    Toast.makeText(getActivity(), "Passwords are not matching!", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //create Client
                    final String finalClientphone = clientphone;
                    final String finalClientgmail = clientgmail;
                    final String finalClientfacebook = clientfacebook;
                    final String finalClientsite = clientsite;
                    final String finalClientinsta = clientinsta;
                    auth.createUserWithEmailAndPassword(clientlogin, clientpassword)
                            .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(mActivity, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    if (!task.isSuccessful()){
                                        Toast.makeText(mActivity, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }else{
                                        ClientInfo clientInfo = new ClientInfo(clientname, clienttype, clientcountry, clientcity, finalClientphone, finalClientgmail, finalClientfacebook, finalClientsite, finalClientinsta);
                                        databaseClients.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(clientInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(mActivity, "Client info added!",
                                                            Toast.LENGTH_SHORT).show();
                                                    //add Userid to separate it as Client
                                                    UserRoleType userRoleType = new UserRoleType(FirebaseAuth.getInstance().getCurrentUser().getUid().toString(), "client");
                                                    databaseUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .setValue(userRoleType).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Log.i(TAG, "user role saved! ");
                                                                uploadFile();
                                                                startActivity(new Intent(mActivity, ClientProfileActivity.class));
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
    public void onStart() {
        super.onStart();
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

    private void uploadFile(){
        if (imageUri != null){
            final StorageReference fileRef = clientstorage.child(auth.getCurrentUser().getUid()).child(System.currentTimeMillis()
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
                        ClientPhoto clientPhoto = new ClientPhoto(downloadUri.toString());
                        databasePhoto.child(auth.getCurrentUser().getUid()).setValue(clientPhoto);
                    }else {
                        Toast.makeText(getActivity(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();

            ClientPhoto clientPhoto = new ClientPhoto("-");
            databasePhoto.child(auth.getCurrentUser().getUid()).setValue(clientPhoto).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.e(TAG, "then: " + "Uri is empty ans task is successfull");
                }
            });
        }
    }
}
