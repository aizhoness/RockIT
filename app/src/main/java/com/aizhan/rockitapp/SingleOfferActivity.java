package com.aizhan.rockitapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aizhan.rockitapp.ClientClasses.ClientOffer;
import com.aizhan.rockitapp.ClientClasses.OfferInfo;
import com.aizhan.rockitapp.MusicianClasses.MusOffer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingleOfferActivity extends AppCompatActivity {
    ImageView offertype_iv, koala_iv, date_iv, cv_iv, location_iv, wallet_iv, info_iv;
    TextView offertype_tv, koala_tv, date_tv, cv_tv, location_tv, wallet_tv, info_tv;
    Button applyBtn;
    private FirebaseAuth auth;
    private DatabaseReference databaseUsers, dbMain, dbOffers;
    private static final String TAG = "MyActivity";
    String offerid;
    Integer exists = 0;
    String usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_offer);

        //Get Firebase auth instance
        offerid = getIntent().getStringExtra("offer_id");
        auth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("usersinfo");
        dbOffers = FirebaseDatabase.getInstance().getReference("offermusicians");

        offertype_iv = (ImageView) findViewById(R.id.type_iv);
        offertype_tv = (TextView) findViewById(R.id.type_tv);
        koala_iv = (ImageView) findViewById(R.id.koala_iv);
        koala_tv = (TextView) findViewById(R.id.koala_tv);
        date_iv = (ImageView) findViewById(R.id.date_iv);
        date_tv = (TextView) findViewById(R.id.date_tv);
        cv_iv = (ImageView) findViewById(R.id.cv_iv);
        cv_tv = (TextView) findViewById(R.id.cv_tv);
        location_iv = (ImageView) findViewById(R.id.location_iv);
        location_tv = (TextView) findViewById(R.id.location_tv);
        wallet_iv = (ImageView) findViewById(R.id.wallet_iv);
        wallet_tv = (TextView) findViewById(R.id.wallet_tv);
        info_iv = (ImageView) findViewById(R.id.info_iv);
        info_tv = (TextView) findViewById(R.id.info_tv);
        applyBtn = (Button) findViewById(R.id.applyBtn);

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i(TAG, "userid: "+userId);
        //check
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "dataSnapshot: "+dataSnapshot.toString());
                usertype = dataSnapshot.child(userId).child("typeofuser").getValue(String.class);
                Log.i(TAG, "usertype: "+usertype);
                if (usertype.equals("musician")){
                    dbMain = FirebaseDatabase.getInstance().getReference("musoffers");
                    dbMain.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.child(offerid).exists()){
                                exists = 0;
                                Log.i("EXISTS: ", exists.toString());
                            }else {
                                exists = 1;
                                Log.i("EXISTS: ", exists.toString());
                                applyBtn.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else if (usertype.equals("client")){
                    applyBtn.setVisibility(View.VISIBLE);
                    applyBtn.setBackgroundResource(R.drawable.next_btn);
                    applyBtn.setText("APPLIERS");
                }
                FirebaseDatabase.getInstance().getReference("offermain").child(offerid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            OfferInfo offerInfo = dataSnapshot.getValue(OfferInfo.class);
                            if (offerInfo.getOffertype().equals("team")){
                                offertype_iv.setBackgroundResource(R.drawable.team_iv);
                                offertype_tv.setText("Team member");
                                koala_tv.setText(offerInfo.getWho());
                                date_iv.setVisibility(View.GONE);
                                date_tv.setVisibility(View.GONE);
                                cv_tv.setText(offerInfo.getRequirements());
                                location_iv.setVisibility(View.GONE);
                                location_tv.setVisibility(View.GONE);
                                wallet_tv.setText(offerInfo.getSalary());
                                info_tv.setText(offerInfo.getAbout());
                            }else {//event
                                offertype_iv.setBackgroundResource(R.drawable.event_iv);
                                offertype_tv.setText("Order to event");
                                koala_tv.setText(offerInfo.getWho());
                                date_tv.setText(offerInfo.getWhen());
                                cv_iv.setVisibility(View.GONE);
                                cv_tv.setVisibility(View.GONE);
                                location_tv.setText(offerInfo.getWhere());
                                wallet_tv.setText(offerInfo.getSalary());
                                info_tv.setText(offerInfo.getAbout());

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usertype.equals("musician")){
                    //APPLY MUSICIAN
                    MusOffer musoffer = new MusOffer(offerid, "applied", "outbox");
                    dbMain.child(userId).child(offerid).setValue(musoffer).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("Success: ", "Applied successfully");
                            ClientOffer clientOffer = new ClientOffer(offerid, userId, "applied", "inbox");
                            dbOffers.child(offerid).child(userId).setValue(clientOffer).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.i("Success: ", "Applied successfully offer");
                                }
                            });
                        }
                    });
                }else if (usertype.equals("client")){
                    //startActivity(new Intent(SingleOfferActivity.this, OfferMusiciansActivity.class));
                }
            }
        });
    }
}
