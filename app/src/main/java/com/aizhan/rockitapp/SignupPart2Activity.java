package com.aizhan.rockitapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.aizhan.rockitapp.MusicianClasses.MusicianEducation;
import com.aizhan.rockitapp.MusicianClasses.MusicianExperience;
import com.aizhan.rockitapp.MusicianClasses.MusicianPerformance;
import com.aizhan.rockitapp.MusicianClasses.MusicianSkills;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_spinner_item;

public class SignupPart2Activity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference museduDB, musexpDB, musperDB, musskillsDB, databaseMusmain;
    private Button drum;
    private Button guitar;
    private Button piano;
    private Button saxophone;
    private Button addedu, addexp, addper, addskill;
    private EditText email;
    private EditText phone;
    private EditText insta;
    private EditText facebook;
    private EditText soundcloud;
    private EditText aboutEdit;
    private String skills = "";
    private LinearLayout lm = null;

    private Integer drumcount = 0;
    private Integer guitarcount = 0;
    private Integer pianocount = 0;
    private Integer saxophonecount = 0;
    private Integer edu_count = 0;
    private Integer exp_count = 0;
    private Integer per_count = 0;
    private Integer skill_count = 0;
    private List<Integer> edu_arr = new ArrayList<>();
    private List<Integer> exp_arr = new ArrayList<>();
    private List<Integer> per_arr = new ArrayList<>();
    private List<Integer> skill_arr = new ArrayList<>();
    private static final String TAG = "MyActivity";

    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_part2);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        museduDB = FirebaseDatabase.getInstance().getReference("MusicianEducation");
        musexpDB = FirebaseDatabase.getInstance().getReference("MusicianExperience");
        musperDB = FirebaseDatabase.getInstance().getReference("MusicianPerformance");
        musskillsDB = FirebaseDatabase.getInstance().getReference("MusicianSkills");
        databaseMusmain = FirebaseDatabase.getInstance().getReference("musmain");

        //Define all element
        addedu = (Button) findViewById(R.id.AddEdu);
        addexp = (Button) findViewById(R.id.AddExp);
        addper = (Button) findViewById(R.id.AddPer);
        addskill = (Button) findViewById(R.id.add_skill);

        drum = (Button) findViewById(R.id.drum_ic);
        final Drawable drumBack = drum.getBackground();
        guitar = (Button)findViewById(R.id.guitar_ic);
        final Drawable guitarBack = guitar.getBackground();
        piano = (Button) findViewById(R.id.piano_ic);
        final Drawable pianoBack = piano.getBackground();
        saxophone = (Button) findViewById(R.id.saxophone_ic);
        final Drawable saxophoneBack = saxophone.getBackground();

        email = (EditText) findViewById(R.id.emailEdit);
        phone = (EditText) findViewById(R.id.phoneEdit);
        insta = (EditText) findViewById(R.id.instaEdit);
        facebook = (EditText) findViewById(R.id.facebookEdit);
        soundcloud = (EditText) findViewById(R.id.soundcloudEdit);
        aboutEdit = (EditText) findViewById(R.id.aboutEdit);

        //If More Education clicked add fields to New Education
        addedu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edu_count++;
                edu_arr.add(edu_count);

                lm = (LinearLayout)findViewById(R.id.edu_layout);
                ContextThemeWrapper themeContext = new ContextThemeWrapper(SignupPart2Activity.this, R.style.EditBox);
                ContextThemeWrapper themeContext2 = new ContextThemeWrapper(SignupPart2Activity.this, R.style.SpinnerBox);
                ContextThemeWrapper themeContext3 = new ContextThemeWrapper(SignupPart2Activity.this, R.style.ButtonStyle);
                // add edittext for Where?
                EditText et = new EditText(SignupPart2Activity.this);
                et.setPadding(15, 0, 0, 0);
                et.setBackgroundResource(R.drawable.name_edit_frame);
                et.setLayoutParams(new LinearLayout.LayoutParams(
                        themeContext, null
                ));
                et.setHint("Where?");
                et.setTag("whereEduEdit-"+edu_count.toString());
                // add edittext for Who?
                EditText et2 = new EditText(SignupPart2Activity.this);
                et2.setPadding(15, 0, 0, 0);
                et2.setBackgroundResource(R.drawable.name_edit_frame);
                et2.setLayoutParams(new LinearLayout.LayoutParams(
                        themeContext, null
                ));
                et2.setHint("Who?");
                et2.setTag("whoEduEdit-"+edu_count.toString());
                // add spinner for How Long?
                Spinner sp = new Spinner(SignupPart2Activity.this);
                sp.setPadding(15, 0, 10, 0);
                sp.setBackgroundResource(R.drawable.spinner_background);
                sp.setLayoutParams(new LinearLayout.LayoutParams(
                        themeContext2, null
                ));
                sp.setTag("howlongEduSpinner-"+edu_count.toString());
                ///ADD scroll elements
                final List howlong = new ArrayList<Integer>();
                for (int i = 1; i <= 100; i++) {
                    howlong.add(Integer.toString(i));
                }
                ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                        SignupPart2Activity.this, simple_spinner_item, howlong);
                spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                sp.setAdapter(spinnerArrayAdapter);

                //Add Delete button
                Button bt = new Button(SignupPart2Activity.this);
                bt.setBackgroundResource(R.drawable.name_edit_frame);
                bt.setLayoutParams(new LinearLayout.LayoutParams(
                        themeContext3, null
                ));
                bt.setText("-");
                bt.setTag("DelEdu-"+edu_count.toString());

                lm.addView(et);
                lm.addView(et2);
                lm.addView(sp);
                lm.addView(bt);

                //add onClickListener to button "-" To DELETE
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View viewl = (View) findViewById(R.id.edu_layout);
                        lm = (LinearLayout)findViewById(R.id.edu_layout);
                        String tagname = view.getTag().toString();
                        String[] separated = tagname.split("-");
                        Integer clicked = Integer.parseInt(separated[1]);
                        EditText editTextwhere = (EditText) viewl.findViewWithTag("whereEduEdit-"+clicked.toString());
                        EditText editTextwho = (EditText) viewl.findViewWithTag("whoEduEdit-"+clicked.toString());
                        Spinner spinnerhowlong = (Spinner) viewl.findViewWithTag("howlongEduSpinner-"+clicked.toString());
                        Button delbutton = (Button) viewl.findViewWithTag("DelEdu-"+clicked.toString());
                        Log.i(TAG, "clicked: "+clicked.toString());

                        //remove
                        edu_arr.remove(clicked);
                        lm.removeView(editTextwhere);
                        lm.removeView(editTextwho);
                        lm.removeView(spinnerhowlong);
                        lm.removeView(delbutton);
                    }
                });
            }
        });
        //Add More Experience
        addexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exp_count++;
                exp_arr.add(exp_count);

                lm = (LinearLayout)findViewById(R.id.exp_layout);
                ContextThemeWrapper themeContext = new ContextThemeWrapper(SignupPart2Activity.this, R.style.EditBox);
                ContextThemeWrapper themeContext2 = new ContextThemeWrapper(SignupPart2Activity.this, R.style.SpinnerBox);
                ContextThemeWrapper themeContext3 = new ContextThemeWrapper(SignupPart2Activity.this, R.style.ButtonStyle);
                // add edittext for Where?
                EditText et = new EditText(SignupPart2Activity.this);
                et.setPadding(15, 0, 0, 0);
                et.setBackgroundResource(R.drawable.name_edit_frame);
                et.setLayoutParams(new LinearLayout.LayoutParams(
                        themeContext, null
                ));
                et.setHint("Where?");
                et.setTag("whereExpEdit-"+exp_count.toString());
                // add edittext for Who?
                EditText et2 = new EditText(SignupPart2Activity.this);
                et2.setPadding(15, 0, 0, 0);
                et2.setBackgroundResource(R.drawable.name_edit_frame);
                et2.setLayoutParams(new LinearLayout.LayoutParams(
                        themeContext, null
                ));
                et2.setHint("Who?");
                et2.setTag("whoExpEdit-"+exp_count.toString());
                // add spinner for How Long?
                Spinner sp = new Spinner(SignupPart2Activity.this);
                sp.setPadding(15, 0, 10, 0);
                sp.setBackgroundResource(R.drawable.spinner_background);
                sp.setLayoutParams(new LinearLayout.LayoutParams(
                        themeContext2, null
                ));
                sp.setTag("howlongExpSpinner-"+exp_count.toString());
                ///ADD scroll elements
                final List howlong = new ArrayList<Integer>();
                for (int i = 1; i <= 100; i++) {
                    howlong.add(Integer.toString(i));
                }
                ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                        SignupPart2Activity.this, simple_spinner_item, howlong);
                spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                sp.setAdapter(spinnerArrayAdapter);

                //Add Delete button
                Button bt = new Button(SignupPart2Activity.this);
                bt.setBackgroundResource(R.drawable.name_edit_frame);
                bt.setLayoutParams(new LinearLayout.LayoutParams(
                        themeContext3, null
                ));
                bt.setText("-");
                bt.setTag("DelExp-"+exp_count.toString());

                lm.addView(et);
                lm.addView(et2);
                lm.addView(sp);
                lm.addView(bt);

                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View viewl = (View) findViewById(R.id.exp_layout);
                        lm = (LinearLayout)findViewById(R.id.exp_layout);
                        String tagname = view.getTag().toString();
                        String[] separated = tagname.split("-");
                        Integer clicked = Integer.parseInt(separated[1]);
                        EditText editTextwhere = (EditText) viewl.findViewWithTag("whereExpEdit-"+clicked.toString());
                        EditText editTextwho = (EditText) viewl.findViewWithTag("whoExpEdit-"+clicked.toString());
                        Spinner spinnerhowlong = (Spinner) viewl.findViewWithTag("howlongExpSpinner-"+clicked.toString());
                        Button delbutton = (Button) viewl.findViewWithTag("DelExp-"+clicked.toString());
                        //remove
                        exp_arr.remove(clicked);
                        lm.removeView(editTextwhere);
                        lm.removeView(editTextwho);
                        lm.removeView(spinnerhowlong);
                        lm.removeView(delbutton);
                    }
                });

            }
        });

        //Add More Performance to Musician
        addper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per_count++;
                per_arr.add(per_count);

                lm = (LinearLayout)findViewById(R.id.per_layout);
                ContextThemeWrapper themeContext = new ContextThemeWrapper(SignupPart2Activity.this, R.style.EditBox);
                ContextThemeWrapper themeContext2 = new ContextThemeWrapper(SignupPart2Activity.this, R.style.ButtonStyle);

                // add edittext for Where?
                EditText et = new EditText(SignupPart2Activity.this);
                et.setPadding(15, 0, 0, 0);
                et.setBackgroundResource(R.drawable.name_edit_frame);
                et.setLayoutParams(new LinearLayout.LayoutParams(
                        themeContext, null
                ));
                et.setHint("Where?");
                et.setTag("wherePerEdit-"+per_count.toString());
                // add edittext for Activity?
                EditText et2 = new EditText(SignupPart2Activity.this);
                et2.setPadding(15, 0, 0, 0);
                et2.setBackgroundResource(R.drawable.name_edit_frame);
                et2.setLayoutParams(new LinearLayout.LayoutParams(
                        themeContext, null
                ));
                et2.setHint("Activity?");
                et2.setTag("actPerEdit-"+per_count.toString());
                //Add Delete button
                Button bt = new Button(SignupPart2Activity.this);
                bt.setBackgroundResource(R.drawable.name_edit_frame);
                bt.setLayoutParams(new LinearLayout.LayoutParams(
                        themeContext2, null
                ));
                bt.setText("-");
                bt.setTag("DelPer-"+per_count.toString());

                lm.addView(et);
                lm.addView(et2);
                lm.addView(bt);

                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View viewl = (View) findViewById(R.id.per_layout);
                        lm = (LinearLayout)findViewById(R.id.per_layout);
                        String tagname = view.getTag().toString();
                        String[] separated = tagname.split("-");
                        Integer clicked = Integer.parseInt(separated[1]);
                        EditText editTextwhere = (EditText) viewl.findViewWithTag("wherePerEdit-"+clicked.toString());
                        EditText editTextact = (EditText) viewl.findViewWithTag("actPerEdit-"+clicked.toString());
                        Button delbutton = (Button) viewl.findViewWithTag("DelPer-"+clicked.toString());
                        //remove
                        exp_arr.remove(clicked);
                        lm.removeView(editTextwhere);
                        lm.removeView(editTextact);
                        lm.removeView(delbutton);
                    }
                });
            }
        });

        //Add New Skill
        addskill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skill_count++;
                skill_arr.add(skill_count);

                lm = (LinearLayout)findViewById(R.id.new_skills);
                ContextThemeWrapper themeContext = new ContextThemeWrapper(SignupPart2Activity.this, R.style.SkillEditText);

                // add edittext for Where?
                final EditText et = new EditText(SignupPart2Activity.this);
                et.setPadding(15, 0, 0, 0);
                et.setBackgroundResource(R.drawable.name_edit_frame);
                Drawable img = SignupPart2Activity.this.getResources().getDrawable( R.drawable.ic_delete);
                et.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);
                et.setLayoutParams(new LinearLayout.LayoutParams(
                        themeContext, null
                ));
                et.setCompoundDrawablePadding(10);
                et.setHint("Instrument");
                et.setTag("newSkillEdit-"+skill_count.toString());

                lm.addView(et);
                et.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        final int DRAWABLE_LEFT = 0;
                        final int DRAWABLE_TOP = 1;
                        final int DRAWABLE_RIGHT = 2;
                        final int DRAWABLE_BOTTOM = 3;
                        lm = (LinearLayout)findViewById(R.id.new_skills);
                        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            if (motionEvent.getRawX() >= (et.getRight() - et.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                String tagname = view.getTag().toString();
                                String[] separated = tagname.split("-");
                                Integer clicked = Integer.parseInt(separated[1]);
                                EditText editTextskill = (EditText) view.findViewWithTag("newSkillEdit-"+clicked.toString());
                                //remove
                                skill_arr.remove(clicked);
                                lm.removeView(editTextskill);
                                return true;
                            }
                        }
                        return false;
                    }
                });

            }
        });

        drum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drumcount==0){
                    drumcount = 1;
                    drum.setBackgroundResource(R.drawable.drumwhite_btn);
                }else{
                    drumcount = 0;
                    drum.setBackgroundDrawable(drumBack);
                }
            }
        });

        guitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (guitarcount==0){
                    guitarcount = 1;
                    guitar.setBackgroundResource(R.drawable.guitarwhite_btn);
                }else{
                    guitarcount = 0;
                    guitar.setBackgroundDrawable(guitarBack);
                }
            }
        });

        piano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pianocount==0){
                    pianocount = 1;
                    piano.setBackgroundResource(R.drawable.pianowhite_btn);
                }else{
                    pianocount = 0;
                    piano.setBackgroundDrawable(pianoBack);
                }
            }
        });
        saxophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saxophonecount==0){
                    saxophonecount = 1;
                    saxophone.setBackgroundResource(R.drawable.saxophonewhite_btn);
                }else{
                    saxophonecount = 0;
                    saxophone.setBackgroundDrawable(saxophoneBack);
                }
            }
        });


        btnSave = (Button) findViewById(R.id.saveBtn);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String whereedu = "";
                String whoedu = "";
                String howlongedu = "";
                String whereexp = "";
                String whoexp = "";
                String howlongexp = "";
                String whereper = "";
                String actper = "";

                String emailt = email.getText().toString().trim();
                String phonet = phone.getText().toString().trim();
                String instat = insta.getText().toString().trim();
                String facebookt = facebook.getText().toString().trim();
                String soundcloudt = soundcloud.getText().toString().trim();
                String about = aboutEdit.getText().toString().trim();
                View view1 = (View) findViewById(R.id.edu_layout);
                View view2 = (View) findViewById(R.id.exp_layout);
                View view3 = (View) findViewById(R.id.per_layout);
                View view4 = (View) findViewById(R.id.new_skills);


                //Info Of Education
                for (int i = 0; i < edu_arr.toArray().length; i++) {
                    EditText where = (EditText) view1.findViewWithTag("whereEduEdit-"+edu_arr.toArray()[i].toString());
                    EditText who = (EditText) view1.findViewWithTag("whoEduEdit-"+edu_arr.toArray()[i].toString());
                    Spinner howlong = (Spinner) view1.findViewWithTag("howlongEduSpinner-"+edu_arr.toArray()[i].toString());
                    if (!where.getText().toString().isEmpty() && !who.getText().toString().isEmpty() && !howlong.getSelectedItem().toString().isEmpty()){
                        if (whereedu.isEmpty()){
                            whereedu = where.getText().toString().trim();
                        }else{
                            whereedu = whereedu+"%"+where.getText().toString().trim();
                        }
                        if (whoedu.isEmpty()){
                            whoedu = who.getText().toString().trim();
                        }else{
                            whoedu = whoedu+"%"+who.getText().toString().trim();
                        }
                        if (howlongedu.isEmpty()){
                            howlongedu = howlong.getSelectedItem().toString();
                        }else{
                            howlongedu = howlongedu+"%"+howlong.getSelectedItem().toString();
                        }
                    }
                }

                //Exp Info of Musician
                for (int i = 0; i < exp_arr.toArray().length; i++) {
                    EditText where = (EditText) view2.findViewWithTag("whereExpEdit-"+exp_arr.toArray()[i].toString());
                    EditText who = (EditText) view2.findViewWithTag("whoExpEdit-"+exp_arr.toArray()[i].toString());
                    Spinner howlong = (Spinner) view2.findViewWithTag("howlongExpSpinner-"+exp_arr.toArray()[i].toString());
                    if (!where.getText().toString().isEmpty() && !who.getText().toString().isEmpty() && !howlong.getSelectedItem().toString().isEmpty()){
                        if (whereexp.isEmpty()){
                            whereexp = where.getText().toString().trim();
                        }else{
                            whereexp = whereexp+"%"+where.getText().toString().trim();
                        }
                        if (whoexp.isEmpty()){
                            whoexp = who.getText().toString().trim();
                        }else{
                            whoexp = whoexp+"%"+who.getText().toString().trim();
                        }
                        if (howlongexp.isEmpty()){
                            howlongexp = howlong.getSelectedItem().toString();
                        }else{
                            howlongexp = howlongexp+"%"+howlong.getSelectedItem().toString();
                        }
                    }
                }

                //Performance Info of Musician
                for (int i = 0; i < per_arr.toArray().length; i++) {
                    EditText where = (EditText) view3.findViewWithTag("wherePerEdit-"+per_arr.toArray()[i].toString());
                    EditText act = (EditText) view3.findViewWithTag("actPerEdit-"+per_arr.toArray()[i].toString());
                    if (!where.getText().toString().isEmpty() && !act.getText().toString().isEmpty()){
                        if (whereper.isEmpty()){
                            whereper = where.getText().toString().trim();
                        }else{
                            whereper = whereper+"%"+where.getText().toString().trim();
                        }
                        if (actper.isEmpty()){
                            actper = act.getText().toString().trim();
                        }else{
                            actper = actper+"%"+act.getText().toString().trim();
                        }

                    }
                }

                //Get skills from clicked buttons
                if (drumcount==1){
                    if (TextUtils.isEmpty(skills)){
                        skills = "drum";
                    }else{
                        skills = skills+"%drum";
                    }
                }
                if (guitarcount==1){
                    if (TextUtils.isEmpty(skills)){
                        skills = "guitar";
                    }else{
                        skills = skills+"%guitar";
                    }
                }
                if (pianocount==1){
                    if (TextUtils.isEmpty(skills)){
                        skills = "piano";
                    }else{
                        skills = skills+"%piano";
                    };
                }
                if (saxophonecount==1){
                    if (TextUtils.isEmpty(skills)){
                        skills = "saxophone";
                    }else{
                        skills = skills+"%saxophone";
                    }
                }
                //Skill Info of Musician
                for (int i = 0; i < skill_arr.toArray().length; i++) {
                    EditText instrument = (EditText) view4.findViewWithTag("newSkillEdit-"+skill_arr.toArray()[i].toString());
                    if (!instrument.getText().toString().trim().isEmpty()){
                        if (skills.isEmpty()){
                            skills = instrument.getText().toString().trim();
                        }else{
                            skills = skills+"%"+instrument.getText().toString().trim();
                        }

                    }
                }

                //replace Contacts to not be empty when storing to db

                if (TextUtils.isEmpty(whereedu)){
                    whereedu = "-";
                }
                if (TextUtils.isEmpty(whereexp)){
                    whereexp = "-";
                }
                if (TextUtils.isEmpty(whereper)){
                    whereper = "-";
                }
                if (TextUtils.isEmpty(whoedu)){
                    whoedu = "-";
                }
                if (TextUtils.isEmpty(howlongedu)){
                    howlongedu = "-";
                }
                if (TextUtils.isEmpty(whoexp)){
                    whoexp = "-";
                }
                if (TextUtils.isEmpty(howlongexp)){
                    howlongexp = "-";
                }
                if (TextUtils.isEmpty(actper)){
                    actper = "-";
                }
                if (TextUtils.isEmpty(emailt)){
                    emailt = "-";
                }
                if (TextUtils.isEmpty(phonet)){
                    phonet = "-";
                }
                if (TextUtils.isEmpty(instat)){
                    instat = "-";
                }
                if (TextUtils.isEmpty(facebookt)){
                    facebookt = "-";
                }
                if (TextUtils.isEmpty(soundcloudt)){
                    soundcloudt = "-";
                }
                if (TextUtils.isEmpty(about)){
                    about = "-";
                }

                //SAVE DATA TO DB
                MusicianEducation musicianEducation = new MusicianEducation(whereedu, whoedu, howlongedu);
                final String finalEmailt = emailt;
                final String finalPhonet = phonet;
                final String finalInstat = instat;
                final String finalFacebookt = facebookt;
                final String finalSoundcloudt = soundcloudt;
                final String finalAbout = about;
                final String finalWhereexp = whereexp;
                final String finalWhoexp = whoexp;
                final String finalHowlongexp = howlongexp;
                final String finalWhereper = whereper;
                final String finalActper = actper;
                museduDB.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(musicianEducation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                MusicianExperience musicianExperience = new MusicianExperience(finalWhereexp, finalWhoexp, finalHowlongexp);
                                musexpDB.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(musicianExperience).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            MusicianPerformance musicianPerformance = new MusicianPerformance(finalWhereper, finalActper);
                                            musperDB.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(musicianPerformance).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        MusicianSkills musicianSkills = new MusicianSkills(skills, finalEmailt, finalPhonet, finalInstat, finalFacebookt, finalSoundcloudt, finalAbout);
                                                        musskillsDB.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                .setValue(musicianSkills).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Toast.makeText(SignupPart2Activity.this, "User data added",
                                                                            Toast.LENGTH_SHORT).show();
                                                                    databaseMusmain.child(auth.getCurrentUser().getUid()).child("skills").setValue(skills);
                                                                    startActivity(new Intent(SignupPart2Activity.this, MusicianProfileActivity.class));
                                                                }else {
                                                                    Toast.makeText(SignupPart2Activity.this, task.getException().toString(),
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }else{
                                                        Toast.makeText(SignupPart2Activity.this, task.getException().toString(),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else{
                                            Toast.makeText(SignupPart2Activity.this, task.getException().toString(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(SignupPart2Activity.this, task.getException().toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
