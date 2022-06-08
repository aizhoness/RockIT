package com.aizhan.rockitapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class MusicianProfileFragment extends Fragment {
    private CircleImageView profphoto;
    private TextView profname;
    private TextView location;
    private TextView about;
    private TextView fanamount;
    private TextView rateamount;
    private TextView audioamount;
    private TextView videoamount;
    private TextView offeramount;
    private LinearLayout lm, lv, lh = null;

    private FirebaseAuth auth;
    private DatabaseReference museduDB, musexpDB, musperDB, musskillsDB, musinfoDB, databaseMusmain;
    private String userID;
    private static final String TAG = "MyActivity";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_musician, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null){
            userID = auth.getCurrentUser().getUid();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        museduDB = FirebaseDatabase.getInstance().getReference("MusicianEducation");
        musexpDB = FirebaseDatabase.getInstance().getReference("MusicianExperience");
        musperDB = FirebaseDatabase.getInstance().getReference("MusicianPerformance");
        musskillsDB = FirebaseDatabase.getInstance().getReference("MusicianSkills");
        musinfoDB = FirebaseDatabase.getInstance().getReference("musicianinfo");
        databaseMusmain = FirebaseDatabase.getInstance().getReference("musmain");

        profphoto = (CircleImageView) view.findViewById(R.id.ProfPhoto);
        profname = (TextView) view.findViewById(R.id.prof_name);
        location = (TextView) view.findViewById(R.id.location);
        about = (TextView) view.findViewById(R.id.about);
        fanamount = (TextView) view.findViewById(R.id.fantext);
        rateamount = (TextView) view.findViewById(R.id.ratetext);
        audioamount = (TextView) view.findViewById(R.id.audiotext);
        videoamount = (TextView) view.findViewById(R.id.videotext);
        offeramount = (TextView) view.findViewById(R.id.offertext);


        //Get Profile Photo
        databaseMusmain.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image_url = dataSnapshot.child(userID).child("imageUri").getValue(String.class);
                if (!image_url.equals("-")){
                    Picasso.with(getActivity())
                            .load(image_url)
                            .fit()
                            .centerCrop()
                            .into(profphoto);
                }

                fanamount.setText(dataSnapshot.child(userID).child("subscribes").getValue(Integer.class).toString());
                rateamount.setText(String.valueOf(dataSnapshot.child(userID).child("rating").getValue(Double.class)));
                audioamount.setText(dataSnapshot.child(userID).child("audios").getValue(Integer.class).toString());
                videoamount.setText(dataSnapshot.child(userID).child("videos").getValue(Integer.class).toString());
                offeramount.setText("0");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Fill data of user
        musinfoDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profname.setText(dataSnapshot.child(userID).child("name").getValue(String.class)+" "+dataSnapshot.child(userID).child("surname").getValue(String.class));
                //drawerprofname.setText(dataSnapshot.child(userID).child("name").getValue(String.class)+" "+dataSnapshot.child(userID).child("surname").getValue(String.class));
                location.setText(dataSnapshot.child(userID).child("country").getValue(String.class)+", "+dataSnapshot.child(userID).child("city").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error in musician info DB, check your connection", Toast.LENGTH_SHORT).show();
            }
        });
        museduDB.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String edu_howlong = dataSnapshot.child(userID).child("howlong").getValue(String.class).toString();
                String edu_where = dataSnapshot.child(userID).child("where").getValue(String.class);
                String edu_who = dataSnapshot.child(userID).child("who").getValue(String.class);

                String[] edu_howlong_arr = edu_howlong.split("%");
                String[] edu_where_arr = edu_where.split("%");
                String[] edu_who_arr = edu_who.split("%");
                lm = (LinearLayout) view.findViewById(R.id.eduprof_layout);
                for (int i = 0; i < edu_howlong_arr.length; i++) {
                    ContextThemeWrapper themeContext = new ContextThemeWrapper(getActivity(), R.style.ProfileMusHorizontalLayout);
                    ContextThemeWrapper themeContext2 = new ContextThemeWrapper(getActivity(), R.style.ProfEducation);
                    ContextThemeWrapper themeContext3 = new ContextThemeWrapper(getActivity(), R.style.ProfileMusVerticalLayout);
                    ContextThemeWrapper themeContext4 = new ContextThemeWrapper(getActivity(), R.style.ProfEducationLess);

                    // add Horizontal Linear Layout
                    lh = new LinearLayout(getActivity());
                    lh.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext, null
                    ));
                    //add Textview
                    TextView tv = new TextView(getActivity());
                    tv.setPadding(30, 20, 0, 0);
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext2, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(Color.parseColor("#0F1F38"));
                    tv.setTextColor(R.color.colorOfDarkBlue);
                    tv.setText(edu_howlong_arr[i]+" month(es)");
                    //add Vertical Linear Layout
                    lv = new LinearLayout(getActivity());
                    lv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext3, null
                    ));
                    lv.setOrientation(LinearLayout.VERTICAL);
                    lv.setPadding(0, 20, 0, 0);
                    //add TextView
                    TextView tv2 = new TextView(getActivity());
                    tv2.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    tv2.setTextSize(20);
                    tv2.setTextColor(Color.parseColor("#0F1F38"));
                    tv2.setText(edu_where_arr[i]);
                    //add TextView
                    TextView tv3 = new TextView(getActivity());
                    tv3.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    tv3.setTextSize(18);
                    tv3.setTextColor(Color.parseColor("#0F1F38"));
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
                Toast.makeText(getActivity(), "Error in musician education DB, check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        musexpDB.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String exp_howlong = dataSnapshot.child(userID).child("howlong").getValue(String.class).toString();
                String exp_where = dataSnapshot.child(userID).child("where").getValue(String.class);
                String exp_who = dataSnapshot.child(userID).child("who").getValue(String.class);

                String[] exp_howlong_arr = exp_howlong.split("%");
                String[] exp_where_arr = exp_where.split("%");
                String[] exp_who_arr = exp_who.split("%");
                lm = (LinearLayout) view.findViewById(R.id.expprof_layout);
                for (int i = 0; i < exp_howlong_arr.length; i++) {
                    ContextThemeWrapper themeContext = new ContextThemeWrapper(getActivity(), R.style.ProfileMusHorizontalLayout);
                    ContextThemeWrapper themeContext2 = new ContextThemeWrapper(getActivity(), R.style.ProfEducation);
                    ContextThemeWrapper themeContext3 = new ContextThemeWrapper(getActivity(), R.style.ProfileMusVerticalLayout);
                    ContextThemeWrapper themeContext4 = new ContextThemeWrapper(getActivity(), R.style.ProfEducationLess);

                    // add Horizontal Linear Layout
                    lh = new LinearLayout(getActivity());
                    lh.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext, null
                    ));
                    //add Textview
                    TextView tv = new TextView(getActivity());
                    tv.setPadding(30, 20, 0, 0);
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext2, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(Color.parseColor("#0F1F38"));
                    tv.setText(exp_howlong_arr[i]+" month(es)");
                    //add Vertical Linear Layout
                    lv = new LinearLayout(getActivity());
                    lv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext3, null
                    ));
                    lv.setOrientation(LinearLayout.VERTICAL);
                    lv.setPadding(0, 20, 0, 0);
                    //add TextView
                    TextView tv2 = new TextView(getActivity());
                    tv2.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    tv2.setTextSize(20);
                    tv2.setTextColor(Color.parseColor("#0F1F38"));
                    tv2.setText(exp_where_arr[i]);
                    //add TextView
                    TextView tv3 = new TextView(getActivity());
                    tv3.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    tv3.setTextSize(18);
                    tv3.setTextColor(Color.parseColor("#0F1F38"));
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
                Toast.makeText(getActivity(), "Error in musician experience DB, check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        musperDB.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String per_where = dataSnapshot.child(userID).child("where").getValue(String.class);
                String per_activity = dataSnapshot.child(userID).child("activity").getValue(String.class);

                String[] per_where_arr = per_where.split("%");
                String[] per_activity_arr = per_activity.split("%");

                lm = (LinearLayout) view.findViewById(R.id.perprof_layout);
                for (int i = 0; i < per_where_arr.length; i++) {
                    ContextThemeWrapper themeContext = new ContextThemeWrapper(getActivity(), R.style.ProfPerformance);

                    //add Textview
                    TextView tv = new TextView(getActivity());
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(Color.parseColor("#0F1F38"));

                    tv.setText(per_where_arr[i]);
                    //add TextView
                    TextView tv2 = new TextView(getActivity());
                    tv2.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext, null
                    ));
                    tv2.setTextSize(18);
                    tv2.setTextColor(Color.parseColor("#0F1F38"));
                    tv2.setText(per_activity_arr[i]);

                    lm.addView(tv);
                    lm.addView(tv2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error in musician performance DB, check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        musskillsDB.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                about.setText(dataSnapshot.child(userID).child("about").getValue(String.class));
                String skills = dataSnapshot.child(userID).child("skills").getValue(String.class);
                String email = dataSnapshot.child(userID).child("email").getValue(String.class).trim();
                String facebook = dataSnapshot.child(userID).child("facebook").getValue(String.class).trim();
                String insta = dataSnapshot.child(userID).child("insta").getValue(String.class).trim();
                String phone = dataSnapshot.child(userID).child("phone").getValue(String.class).trim();
                String soundcloud = dataSnapshot.child(userID).child("soundcloud").getValue(String.class).trim();
                String other_skills = "";
                String[] skills_arr = skills.split("%");
                List skills_list = new ArrayList(Arrays.asList(skills_arr));

                lm = (LinearLayout) view.findViewById(R.id.skill_layout);
                ContextThemeWrapper themeContext = new ContextThemeWrapper(getActivity(), R.style.SkillIcon);
                ContextThemeWrapper themeContext2 = new ContextThemeWrapper(getActivity(), R.style.MusSkillHorizontalLayout);
                ContextThemeWrapper themeContext3 = new ContextThemeWrapper(getActivity(), R.style.ProfPerformanceSkill);
                ContextThemeWrapper themeContext4 = new ContextThemeWrapper(getActivity(), R.style.MusRefHorizontalLayout);
                ContextThemeWrapper themeContext5 = new ContextThemeWrapper(getActivity(), R.style.RefImageView);
                ContextThemeWrapper themeContext6 = new ContextThemeWrapper(getActivity(), R.style.RefTextView);

                // add Horizontal Linear Layout
                lh = new LinearLayout(getActivity());
                lh.setLayoutParams(new LinearLayout.LayoutParams(
                        themeContext2, null
                ));
                lh.setPadding(0, 16, 0, 0);

                if (skills_list.contains("drum")){
                    //add TextView
                    Button bt = new Button(getActivity());
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext, null
                    ));
                    bt.setBackgroundResource(R.drawable.drum_btn);
                    lh.addView(bt);
                    skills_list.remove("drum");
                }
                if (skills_list.contains("guitar")){
                    //add TextView
                    Button bt = new Button(getActivity());
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext, null
                    ));
                    bt.setBackgroundResource(R.drawable.guitar_btn);
                    lh.addView(bt);
                    skills_list.remove("guitar");
                }
                if (skills_list.contains("piano")){
                    //add TextView
                    Button bt = new Button(getActivity());
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext, null
                    ));
                    bt.setBackgroundResource(R.drawable.piano_btn);
                    lh.addView(bt);
                    skills_list.remove("piano");
                }
                if (skills_list.contains("saxophone")){
                    //add TextView
                    Button bt = new Button(getActivity());
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
                    TextView tv = new TextView(getActivity());
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext3, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(Color.parseColor("#0F1F38"));
                    tv.setText(other_skills);
                    lm.addView(tv);
                }
                ///ADD REFERENCES
                lm = (LinearLayout) view.findViewById(R.id.ref_layout);
                if (!email.equals("-")){
                    Log.i(TAG, "email: "+email);
                    // add Horizontal Linear Layout
                    lh = new LinearLayout(getActivity());
                    lh.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    //Add Button
                    Button bt = new Button(getActivity());
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext5, null
                    ));
                    bt.setBackgroundResource(R.drawable.gmail_btn);
                    //Add TextView
                    TextView tv = new TextView(getActivity());
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext6, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(Color.parseColor("#0F1F38"));
                    tv.setText(email);

                    lh.addView(bt);
                    lh.addView(tv);
                    lm.addView(lh);
                }
                if (!facebook.equals("-")){
                    Log.i(TAG, "facebook: "+facebook);
                    // add Horizontal Linear Layout
                    lh = new LinearLayout(getActivity());
                    lh.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    //Add Button
                    Button bt = new Button(getActivity());
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext5, null
                    ));
                    bt.setBackgroundResource(R.drawable.facebook_btn);
                    //Add TextView
                    TextView tv = new TextView(getActivity());
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext6, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(Color.parseColor("#0F1F38"));
                    tv.setText(facebook);

                    lh.addView(bt);
                    lh.addView(tv);
                    lm.addView(lh);
                }
                if (!insta.equals("-")){
                    Log.i(TAG, "insta: "+insta);
                    // add Horizontal Linear Layout
                    lh = new LinearLayout(getActivity());
                    lh.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    //Add Button
                    Button bt = new Button(getActivity());
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext5, null
                    ));
                    bt.setBackgroundResource(R.drawable.insta_btn);

                    //Add TextView
                    TextView tv = new TextView(getActivity());
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext6, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(Color.parseColor("#0F1F38"));
                    tv.setText(insta);

                    lh.addView(bt);
                    lh.addView(tv);
                    lm.addView(lh);
                }
                if (!phone.equals("-")){
                    Log.i(TAG, "phone: "+phone);
                    // add Horizontal Linear Layout
                    lh = new LinearLayout(getActivity());
                    lh.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    //Add Button
                    Button bt = new Button(getActivity());
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext5, null
                    ));
                    bt.setBackgroundResource(R.drawable.phone_btn);
                    //Add TextView
                    TextView tv = new TextView(getActivity());
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext6, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(Color.parseColor("#0F1F38"));
                    tv.setText(phone);

                    lh.addView(bt);
                    lh.addView(tv);
                    lm.addView(lh);
                }
                if (!soundcloud.equals("-")){
                    Log.i(TAG, "soundcloud: "+soundcloud);
                    // add Horizontal Linear Layout
                    lh = new LinearLayout(getActivity());
                    lh.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext4, null
                    ));
                    //Add Button
                    Button bt = new Button(getActivity());
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext5, null
                    ));
                    bt.setBackgroundResource(R.drawable.soundcloud_btn);
                    //Add TextView
                    TextView tv = new TextView(getActivity());
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext6, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(Color.parseColor("#0F1F38"));
                    tv.setText(soundcloud);

                    lh.addView(bt);
                    lh.addView(tv);
                    lm.addView(lh);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error in musician skills DB, check your connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
