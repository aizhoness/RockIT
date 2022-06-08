package com.aizhan.rockitapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.aizhan.rockitapp.VideoClasses.MusVideo;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadVideoActivity extends AppCompatActivity {
    VideoView video;
    EditText vname;
    Button choose, upload;
    private FirebaseAuth auth;
    private DatabaseReference databaseVideos;
    private StorageReference videostorage;
    private Uri videoUri;
    MediaController mc;
    public static final int KITKAT_VALUE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        databaseVideos= FirebaseDatabase.getInstance().getReference("musvideos");
        videostorage = FirebaseStorage.getInstance().getReference("musicianvideos");


        video = (VideoView) findViewById(R.id.uploaded_video);
        vname = (EditText) findViewById(R.id.video_name);
        choose = (Button) findViewById(R.id.choose_video);
        upload = (Button) findViewById(R.id.upload_video);

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                        mc = new MediaController(UploadVideoActivity.this);
                        video.setMediaController(mc);
                        mc.setAnchorView(video);
                    }
                });
            }
        });

        video.start();

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFileChooser();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });
    }

    private void OpenFileChooser(){
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a video"), KITKAT_VALUE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("video/*");
            startActivityForResult(Intent.createChooser(intent, "Select a video"), KITKAT_VALUE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KITKAT_VALUE && resultCode == RESULT_OK && data != null){
            videoUri = data.getData();
            video.setVideoURI(videoUri);

        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = UploadVideoActivity.this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private void uploadFile(){
        if (videoUri != null){
            final StorageReference fileRef = videostorage.child(auth.getCurrentUser().getUid()).child(System.currentTimeMillis()
                    +"."+getFileExtension(videoUri));
            fileRef.putFile(videoUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>(){

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
                        Log.e("Video uri", "then: " + downloadUri.toString());
                        String videoname = vname.getText().toString().trim();
                        if (videoname.isEmpty()){
                            videoname = "Unknown";
                        }
                        MusVideo musVideo = new MusVideo(videoname, downloadUri.toString());
                        final String videoid = databaseVideos.push().getKey();
                        databaseVideos.child(auth.getCurrentUser().getUid()).child(videoid).setValue(musVideo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(UploadVideoActivity.this, "upload succeed! ", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }else {
                        Toast.makeText(UploadVideoActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(UploadVideoActivity.this, "No file selected", Toast.LENGTH_SHORT).show();


        }
    }
}
