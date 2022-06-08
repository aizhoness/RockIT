package com.aizhan.rockitapp;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aizhan.rockitapp.ClientClasses.ClientOffer;
import com.aizhan.rockitapp.ClientClasses.OfferInfo;
import com.aizhan.rockitapp.FanClasses.FanSub;
import com.aizhan.rockitapp.MusicianClasses.MusOffer;
import com.aizhan.rockitapp.MusicianClasses.MusSub;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleMusicianActivity extends AppCompatActivity {
    private CircleImageView profphoto;
    private TextView profname;
    private TextView location;
    private TextView about;
    private TextView fanamount;
    private TextView rateamount;
    private TextView audioamount;
    private TextView videoamount;
    private Button actionBtn;
    private ArrayList<String> offers, offerids;
    private LinearLayout lm, lv, lh = null;

    private FirebaseAuth auth;
    private DatabaseReference museduDB, musexpDB, musperDB, musskillsDB, musinfoDB, databaseMusmain, usermain, clientoffers, offermusicians, musoffers, fansubs, mussubs;
    private String userID;
    private String musID;
    private String user_type;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_musician);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser().getUid()!= null){
            userID = auth.getCurrentUser().getUid();
        }

        museduDB = FirebaseDatabase.getInstance().getReference("MusicianEducation");
        musexpDB = FirebaseDatabase.getInstance().getReference("MusicianExperience");
        musperDB = FirebaseDatabase.getInstance().getReference("MusicianPerformance");
        musskillsDB = FirebaseDatabase.getInstance().getReference("MusicianSkills");
        musinfoDB = FirebaseDatabase.getInstance().getReference("musicianinfo");
        databaseMusmain = FirebaseDatabase.getInstance().getReference("musmain");
        usermain = FirebaseDatabase.getInstance().getReference("usersinfo");
        offermusicians = FirebaseDatabase.getInstance().getReference("offermusicians");
        musoffers = FirebaseDatabase.getInstance().getReference("musoffers");
        clientoffers = FirebaseDatabase.getInstance().getReference("clientoffers");
        fansubs = FirebaseDatabase.getInstance().getReference("fansubs");
        mussubs = FirebaseDatabase.getInstance().getReference("mussubs");



        profphoto = (CircleImageView) findViewById(R.id.ProfPhoto);
        profname = (TextView) findViewById(R.id.prof_name);
        location = (TextView) findViewById(R.id.location);
        about = (TextView) findViewById(R.id.about);
        fanamount = (TextView) findViewById(R.id.fantext);
        rateamount = (TextView) findViewById(R.id.ratetext);
        audioamount = (TextView) findViewById(R.id.audiotext);
        videoamount = (TextView) findViewById(R.id.videotext);
        actionBtn = (Button) findViewById(R.id.action_Btn);

        musID = getIntent().getStringExtra("mus_id");
        Log.d(TAG, "musID: "+musID);

        //Get Profile Photo
        databaseMusmain.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image_url = dataSnapshot.child(musID).child("imageUri").getValue(String.class);
                Picasso.with(SingleMusicianActivity.this)
                        .load(image_url)
                        .fit()
                        .centerCrop()
                        .into(profphoto);
                fanamount.setText(dataSnapshot.child(musID).child("subscribes").getValue(Integer.class).toString());
                rateamount.setText(String.valueOf(dataSnapshot.child(musID).child("rating").getValue(Double.class)));
                audioamount.setText(dataSnapshot.child(musID).child("audios").getValue(Integer.class).toString());
                videoamount.setText(dataSnapshot.child(musID).child("videos").getValue(Integer.class).toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Fill data of user
        musinfoDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profname.setText(dataSnapshot.child(musID).child("name").getValue(String.class)+" "+dataSnapshot.child(musID).child("surname").getValue(String.class));
                //drawerprofname.setText(dataSnapshot.child(userID).child("name").getValue(String.class)+" "+dataSnapshot.child(userID).child("surname").getValue(String.class));
                location.setText(dataSnapshot.child(musID).child("country").getValue(String.class)+", "+dataSnapshot.child(musID).child("city").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SingleMusicianActivity.this, "Error in musician info DB, check your connection", Toast.LENGTH_SHORT).show();
            }
        });
        museduDB.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String edu_howlong = dataSnapshot.child(musID).child("howlong").getValue(String.class).toString();
                String edu_where = dataSnapshot.child(musID).child("where").getValue(String.class);
                String edu_who = dataSnapshot.child(musID).child("who").getValue(String.class);

                String[] edu_howlong_arr = edu_howlong.split("%");
                String[] edu_where_arr = edu_where.split("%");
                String[] edu_who_arr = edu_who.split("%");
                lm = (LinearLayout) findViewById(R.id.eduprof_layout);
                for (int i = 0; i < edu_howlong_arr.length; i++) {
                    ContextThemeWrapper themeContext = new ContextThemeWrapper(SingleMusicianActivity.this, R.style.ProfileMusHorizontalLayout);
                    ContextThemeWrapper themeContext2 = new ContextThemeWrapper(SingleMusicianActivity.this, R.style.ProfEducation);
                    ContextThemeWrapper themeContext3 = new ContextThemeWrapper(SingleMusicianActivity.this, R.style.ProfileMusVerticalLayout);
                    ContextThemeWrapper themeContext4 = new ContextThemeWrapper(SingleMusicianActivity.this, R.style.ProfEducationLess);

                    // add Horizontal Linear Layout
                    lh = new LinearLayout(SingleMusicianActivity.this);
                    lh.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext, null
                    ));
                    //add Textview
                    TextView tv = new TextView(SingleMusicianActivity.this);
                    tv.setPadding(30, 20, 0, 0);
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext2, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(R.color.colorOfDarkBlue);
                    tv.setText(edu_howlong_arr[i]+" month(es)");
                    //add Vertical Linear Layout
                    lv = new LinearLayout(SingleMusicianActivity.this);
                    lv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext3, null
                    ));
                    lv.setOrientation(LinearLayout.VERTICAL);
                    lv.setPadding(0, 20, 0, 0);
                    //add TextView
                    TextView tv2 = new TextView(SingleMusicianActivity.this);
                    tv2.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    tv2.setTextSize(20);
                    tv2.setTextColor(R.color.colorOfDarkBlue);
                    tv2.setText(edu_where_arr[i]);
                    //add TextView
                    TextView tv3 = new TextView(SingleMusicianActivity.this);
                    tv3.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    tv3.setTextSize(18);
                    tv3.setTextColor(R.color.colorOfDarkBlue);
                    tv3.setPadding(0, 10, 0, 0);
                    tv3.setText(edu_who_arr[i]);

                    lm.addView(lh);
                    lh.addView(tv);
                    lh.addView(lv);
                    lv.addView(tv2);
                    lv.addView(tv3);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SingleMusicianActivity.this, "Error in musician education DB, check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        musexpDB.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String exp_howlong = dataSnapshot.child(musID).child("howlong").getValue(String.class).toString();
                String exp_where = dataSnapshot.child(musID).child("where").getValue(String.class);
                String exp_who = dataSnapshot.child(musID).child("who").getValue(String.class);

                String[] exp_howlong_arr = exp_howlong.split("%");
                String[] exp_where_arr = exp_where.split("%");
                String[] exp_who_arr = exp_who.split("%");
                lm = (LinearLayout) findViewById(R.id.expprof_layout);
                for (int i = 0; i < exp_howlong_arr.length; i++) {
                    ContextThemeWrapper themeContext = new ContextThemeWrapper(SingleMusicianActivity.this, R.style.ProfileMusHorizontalLayout);
                    ContextThemeWrapper themeContext2 = new ContextThemeWrapper(SingleMusicianActivity.this, R.style.ProfEducation);
                    ContextThemeWrapper themeContext3 = new ContextThemeWrapper(SingleMusicianActivity.this, R.style.ProfileMusVerticalLayout);
                    ContextThemeWrapper themeContext4 = new ContextThemeWrapper(SingleMusicianActivity.this, R.style.ProfEducationLess);

                    // add Horizontal Linear Layout
                    lh = new LinearLayout(SingleMusicianActivity.this);
                    lh.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext, null
                    ));
                    //add Textview
                    TextView tv = new TextView(SingleMusicianActivity.this);
                    tv.setPadding(30, 20, 0, 0);
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext2, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(R.color.colorOfDarkBlue);
                    tv.setText(exp_howlong_arr[i]+" month(es)");
                    //add Vertical Linear Layout
                    lv = new LinearLayout(SingleMusicianActivity.this);
                    lv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext3, null
                    ));
                    lv.setOrientation(LinearLayout.VERTICAL);
                    lv.setPadding(0, 20, 0, 0);
                    //add TextView
                    TextView tv2 = new TextView(SingleMusicianActivity.this);
                    tv2.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    tv2.setTextSize(20);
                    tv2.setTextColor(R.color.colorOfDarkBlue);
                    tv2.setText(exp_where_arr[i]);
                    //add TextView
                    TextView tv3 = new TextView(SingleMusicianActivity.this);
                    tv3.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    tv3.setTextSize(18);
                    tv3.setTextColor(R.color.colorOfDarkBlue);
                    tv3.setPadding(0, 10, 0, 0);
                    tv3.setText(exp_who_arr[i]);

                    lv.addView(tv2);
                    lv.addView(tv3);
                    lh.addView(tv);
                    lh.addView(lv);
                    lm.addView(lh);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SingleMusicianActivity.this, "Error in musician experience DB, check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        musperDB.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String per_where = dataSnapshot.child(musID).child("where").getValue(String.class);
                String per_activity = dataSnapshot.child(musID).child("activity").getValue(String.class);

                String[] per_where_arr = per_where.split("%");
                String[] per_activity_arr = per_activity.split("%");

                lm = (LinearLayout) findViewById(R.id.perprof_layout);
                for (int i = 0; i < per_where_arr.length; i++) {
                    ContextThemeWrapper themeContext = new ContextThemeWrapper(SingleMusicianActivity.this, R.style.ProfPerformance);

                    //add Textview
                    TextView tv = new TextView(SingleMusicianActivity.this);
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(R.color.colorOfDarkBlue);

                    tv.setText(per_where_arr[i]);
                    //add TextView
                    TextView tv2 = new TextView(SingleMusicianActivity.this);
                    tv2.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext, null
                    ));
                    tv2.setTextSize(18);
                    tv2.setTextColor(R.color.colorOfDarkBlue);
                    tv2.setText(per_activity_arr[i]);

                    lm.addView(tv);
                    lm.addView(tv2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SingleMusicianActivity.this, "Error in musician performance DB, check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        musskillsDB.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                about.setText(dataSnapshot.child(musID).child("about").getValue(String.class));
                String skills = dataSnapshot.child(musID).child("skills").getValue(String.class);
                String email = dataSnapshot.child(musID).child("email").getValue(String.class).trim();
                String facebook = dataSnapshot.child(musID).child("facebook").getValue(String.class).trim();
                String insta = dataSnapshot.child(musID).child("insta").getValue(String.class).trim();
                String phone = dataSnapshot.child(musID).child("phone").getValue(String.class).trim();
                String soundcloud = dataSnapshot.child(musID).child("soundcloud").getValue(String.class).trim();
                String other_skills = "";
                String[] skills_arr = skills.split("%");
                List skills_list = new ArrayList(Arrays.asList(skills_arr));

                lm = (LinearLayout) findViewById(R.id.skill_layout);
                ContextThemeWrapper themeContext = new ContextThemeWrapper(SingleMusicianActivity.this, R.style.SkillIcon);
                ContextThemeWrapper themeContext2 = new ContextThemeWrapper(SingleMusicianActivity.this, R.style.MusSkillHorizontalLayout);
                ContextThemeWrapper themeContext3 = new ContextThemeWrapper(SingleMusicianActivity.this, R.style.ProfPerformanceSkill);
                ContextThemeWrapper themeContext4 = new ContextThemeWrapper(SingleMusicianActivity.this, R.style.MusRefHorizontalLayout);
                ContextThemeWrapper themeContext5 = new ContextThemeWrapper(SingleMusicianActivity.this, R.style.RefImageView);
                ContextThemeWrapper themeContext6 = new ContextThemeWrapper(SingleMusicianActivity.this, R.style.RefTextView);

                // add Horizontal Linear Layout
                lh = new LinearLayout(SingleMusicianActivity.this);
                lh.setLayoutParams(new LinearLayout.LayoutParams(
                        themeContext2, null
                ));
                lh.setPadding(0, 16, 0, 0);

                if (skills_list.contains("drum")){
                    //add TextView
                    Button bt = new Button(SingleMusicianActivity.this);
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext, null
                    ));
                    bt.setBackgroundResource(R.drawable.drum_btn);
                    lh.addView(bt);
                    skills_list.remove("drum");
                }
                if (skills_list.contains("guitar")){
                    //add TextView
                    Button bt = new Button(SingleMusicianActivity.this);
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext, null
                    ));
                    bt.setBackgroundResource(R.drawable.guitar_btn);
                    lh.addView(bt);
                    skills_list.remove("guitar");
                }
                if (skills_list.contains("piano")){
                    //add TextView
                    Button bt = new Button(SingleMusicianActivity.this);
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext, null
                    ));
                    bt.setBackgroundResource(R.drawable.piano_btn);
                    lh.addView(bt);
                    skills_list.remove("piano");
                }
                if (skills_list.contains("saxophone")){
                    //add TextView
                    Button bt = new Button(SingleMusicianActivity.this);
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext, null
                    ));
                    bt.setBackgroundResource(R.drawable.saxophone_btn);
                    lh.addView(bt);
                    skills_list.remove("saxophone");
                }
                lm.addView(lh);

                for (int i = 0; i < skills_list.toArray().length; i++) {
                    if (other_skills==""){
                        other_skills = skills_list.toArray()[i].toString();
                    }else{
                        other_skills = other_skills + ", "+skills_list.toArray()[i].toString();
                    }
                }
                if (other_skills!=""){
                    //add Textview
                    TextView tv = new TextView(SingleMusicianActivity.this);
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext3, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(R.color.colorOfDarkBlue);
                    tv.setText(other_skills);
                    lm.addView(tv);
                }
                ///ADD REFERENCES
                lm = (LinearLayout) findViewById(R.id.ref_layout);
                if (!email.equals("-")){
                    Log.i(TAG, "email: "+email);
                    // add Horizontal Linear Layout
                    lh = new LinearLayout(SingleMusicianActivity.this);
                    lh.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    //Add Button
                    Button bt = new Button(SingleMusicianActivity.this);
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext5, null
                    ));
                    bt.setBackgroundResource(R.drawable.gmail_btn);
                    //Add TextView
                    TextView tv = new TextView(SingleMusicianActivity.this);
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext6, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(R.color.colorOfDarkBlue);
                    tv.setText(email);

                    lh.addView(bt);
                    lh.addView(tv);
                    lm.addView(lh);
                }
                if (!facebook.equals("-")){
                    Log.i(TAG, "facebook: "+facebook);
                    // add Horizontal Linear Layout
                    lh = new LinearLayout(SingleMusicianActivity.this);
                    lh.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    //Add Button
                    Button bt = new Button(SingleMusicianActivity.this);
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext5, null
                    ));
                    bt.setBackgroundResource(R.drawable.facebook_btn);
                    //Add TextView
                    TextView tv = new TextView(SingleMusicianActivity.this);
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext6, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(R.color.colorOfDarkBlue);
                    tv.setText(facebook);

                    lh.addView(bt);
                    lh.addView(tv);
                    lm.addView(lh);
                }
                if (!insta.equals("-")){
                    Log.i(TAG, "insta: "+insta);
                    // add Horizontal Linear Layout
                    lh = new LinearLayout(SingleMusicianActivity.this);
                    lh.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    //Add Button
                    Button bt = new Button(SingleMusicianActivity.this);
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext5, null
                    ));
                    bt.setBackgroundResource(R.drawable.insta_btn);

                    //Add TextView
                    TextView tv = new TextView(SingleMusicianActivity.this);
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext6, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(R.color.colorOfDarkBlue);
                    tv.setText(insta);

                    lh.addView(bt);
                    lh.addView(tv);
                    lm.addView(lh);
                }
                if (!phone.equals("-")){
                    Log.i(TAG, "phone: "+phone);
                    // add Horizontal Linear Layout
                    lh = new LinearLayout(SingleMusicianActivity.this);
                    lh.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    //Add Button
                    Button bt = new Button(SingleMusicianActivity.this);
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext5, null
                    ));
                    bt.setBackgroundResource(R.drawable.phone_btn);
                    //Add TextView
                    TextView tv = new TextView(SingleMusicianActivity.this);
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext6, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(R.color.colorOfDarkBlue);
                    tv.setText(phone);

                    lh.addView(bt);
                    lh.addView(tv);
                    lm.addView(lh);
                }
                if (!soundcloud.equals("-")){
                    Log.i(TAG, "soundcloud: "+soundcloud);
                    // add Horizontal Linear Layout
                    lh = new LinearLayout(SingleMusicianActivity.this);
                    lh.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    //Add Button
                    Button bt = new Button(SingleMusicianActivity.this);
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext5, null
                    ));
                    bt.setBackgroundResource(R.drawable.soundcloud_btn);
                    //Add TextView
                    TextView tv = new TextView(SingleMusicianActivity.this);
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext6, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(R.color.colorOfDarkBlue);
                    tv.setText(soundcloud);

                    lh.addView(bt);
                    lh.addView(tv);
                    lm.addView(lh);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SingleMusicianActivity.this, "Error in musician skills DB, check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        //GET TYPE OF USER
        usermain.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_type = dataSnapshot.child(musID).child("typeofuser").getValue(String.class);
                if (user_type.equals("musician")){
                    actionBtn.setVisibility(View.INVISIBLE);
                }else if (user_type.equals("client")){
                    actionBtn.setVisibility(View.VISIBLE);
                }else if (user_type.equals("fan")){
                    actionBtn.setVisibility(View.VISIBLE);
                    actionBtn.setText("SUBSCRIBE!");
                }else{
                    Toast.makeText(SingleMusicianActivity.this, "This musician doesn't exist anymore...", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*//Get Client Offers
        DatabaseReference clientoffers = FirebaseDatabase.getInstance().getReference("clientoffers");
        clientoffers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot clientSnapshot : dataSnapshot.child(userID).getChildren()){
                        Log.i("Snapshot KEY: ", clientSnapshot.getKey());
                        Log.i("Snapshot VALUE: ", clientSnapshot.getValue().toString());
                        offerids.add(clientSnapshot.child("offerid").getValue(String.class));
                        offers.add(clientSnapshot.child("who").getValue(String.class));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        //CLICK ACTION BUTTON
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_type.equals("client")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SingleMusicianActivity.this);
                    View mview = getLayoutInflater().inflate(R.layout.dialog_offer, null);
                    final Spinner offerSpinner = (Spinner) mview.findViewById(R.id.offerSpinner);
                    Button hireBtn = (Button) mview.findViewById(R.id.hire_Btn);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                            SingleMusicianActivity.this, android.R.layout.simple_spinner_item, offers);
                    spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                    offerSpinner.setAdapter(spinnerArrayAdapter);

                    hireBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String selectedOffer = offerSpinner.getSelectedItem().toString().trim();
                            if (selectedOffer.isEmpty()){
                                Toast.makeText(SingleMusicianActivity.this, "Please, select the offer", Toast.LENGTH_SHORT).show();
                            }else {
                                //Add Musician to Offer
                                Integer index = offers.indexOf(selectedOffer);
                                String offerID = offerids.get(index);
                                ClientOffer clientOffer = new ClientOffer(offerID, musID, "unread", "outbox");
                                MusOffer musOffer = new MusOffer(offerID, "unread", "inbox");
                                offermusicians.child(offerID).child(musID).setValue(clientOffer);
                                musoffers.child(musID).child(offerID).setValue(musOffer);
                                finish();

                            }
                        }
                    });

                }else if (user_type.equals("fan")){
                    FanSub fanSub = new FanSub(musID, 0);
                    MusSub musSub = new MusSub(userID, 0);
                    fansubs.child(userID).child(musID).setValue(fanSub);
                    mussubs.child(musID).child(userID).setValue(musSub);
                    Toast.makeText(SingleMusicianActivity.this, "You Subscribed This Musician!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser().getUid()!= null){
            userID = auth.getCurrentUser().getUid();
        }
    }
}
