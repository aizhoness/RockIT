package com.aizhan.rockitapp;


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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FanProfileFragment extends Fragment {
    private CircleImageView profphoto;
    private TextView fanname;
    private TextView location;
    private FirebaseAuth auth;
    private DatabaseReference faninfoDB, databaseMain;
    private TextView subscribeamount;
    private TextView audioamount;
    private TextView videoamount;
    private String userID;
    private static final String TAG = "MyActivity";
    private LinearLayout lm, lh = null;


    public FanProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fan_profile, container, false);
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
        faninfoDB = FirebaseDatabase.getInstance().getReference("faninfo");
        databaseMain = FirebaseDatabase.getInstance().getReference("fanmain");

        profphoto = (CircleImageView) view.findViewById(R.id.ProfPhoto);
        fanname = (TextView) view.findViewById(R.id.fan_name);
        location = (TextView) view.findViewById(R.id.location);
        subscribeamount = (TextView) view.findViewById(R.id.fantext);
        audioamount = (TextView) view.findViewById(R.id.audiotext);
        videoamount = (TextView) view.findViewById(R.id.videotext);


        databaseMain.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image_url = dataSnapshot.child(userID).child("image_url").getValue(String.class);
                if (!image_url.equals("-")){
                    Picasso.with(getActivity())
                            .load(image_url)
                            .fit()
                            .centerCrop()
                            .into(profphoto);
                }

                subscribeamount.setText(dataSnapshot.child(userID).child("subscribes").getValue(Integer.class).toString());
                audioamount.setText(dataSnapshot.child(userID).child("audios").getValue(Integer.class).toString());
                videoamount.setText(dataSnapshot.child(userID).child("videos").getValue(Integer.class).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        faninfoDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fanname.setText(dataSnapshot.child(userID).child("name").getValue(String.class)+" "+dataSnapshot.child(userID).child("surname").getValue(String.class));
                location.setText(dataSnapshot.child(userID).child("country").getValue(String.class)+", "+dataSnapshot.child(userID).child("city").getValue(String.class));
                String gmail = dataSnapshot.child(userID).child("gmail").getValue(String.class);
                lm = (LinearLayout) view.findViewById(R.id.ref_layout);

                ContextThemeWrapper themeContext = new ContextThemeWrapper(getActivity(), R.style.MusRefHorizontalLayout);
                ContextThemeWrapper themeContext2 = new ContextThemeWrapper(getActivity(), R.style.RefImageView);
                ContextThemeWrapper themeContext3 = new ContextThemeWrapper(getActivity(), R.style.RefTextView);
                if (!gmail.equals("-")){
                    Log.i(TAG, "gmail: "+gmail);
                    // add Horizontal Linear Layout
                    lh = new LinearLayout(getActivity());
                    lh.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext, null
                    ));
                    //Add Button
                    Button bt = new Button(getActivity());
                    bt.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext2, null
                    ));
                    bt.setBackgroundResource(R.drawable.gmail_btn);
                    //Add TextView
                    TextView tv = new TextView(getActivity());
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext3, null
                    ));
                    tv.setTextSize(20);
                    tv.setText(gmail);

                    lh.addView(bt);
                    lh.addView(tv);
                    lm.addView(lh);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
