package com.aizhan.rockitapp;


import android.graphics.Color;
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
import android.widget.Toast;

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
public class ClientProfileFragment extends Fragment {
    private CircleImageView profphoto;
    private TextView clientname;
    private TextView location;
    private TextView status;
    private FirebaseAuth auth;
    private DatabaseReference clientinfoDB, databasePhoto;
    private String userID;
    private static final String TAG = "MyActivity";
    private LinearLayout lm, lh = null;


    public ClientProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_profile, container, false);
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
        clientinfoDB = FirebaseDatabase.getInstance().getReference("clientinfo");
        databasePhoto = FirebaseDatabase.getInstance().getReference("clientphoto");

        profphoto = (CircleImageView) view.findViewById(R.id.ProfPhoto);
        clientname = (TextView) view.findViewById(R.id.company_name);
        location = (TextView) view.findViewById(R.id.location);
        status = (TextView) view.findViewById(R.id.client_type);

        //Set Profile Photo
        databasePhoto.addValueEventListener(new ValueEventListener() {
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Fill data of user
        clientinfoDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clientname.setText(dataSnapshot.child(userID).child("client_name").getValue(String.class));
                location.setText(dataSnapshot.child(userID).child("country").getValue(String.class)+", "+dataSnapshot.child(userID).child("city").getValue(String.class));
                status.setText(dataSnapshot.child(userID).child("client_type").getValue(String.class));

                String phone = dataSnapshot.child(userID).child("phone_number").getValue(String.class);
                String gmail = dataSnapshot.child(userID).child("gmail").getValue(String.class);
                String facebook = dataSnapshot.child(userID).child("facebook").getValue(String.class);
                String site = dataSnapshot.child(userID).child("site").getValue(String.class);
                String insta = dataSnapshot.child(userID).child("insta").getValue(String.class);
                lm = (LinearLayout) view.findViewById(R.id.ref_layout);

                ContextThemeWrapper themeContext = new ContextThemeWrapper(getActivity(), R.style.MusRefHorizontalLayout);
                ContextThemeWrapper themeContext2 = new ContextThemeWrapper(getActivity(), R.style.RefImageView);
                ContextThemeWrapper themeContext3 = new ContextThemeWrapper(getActivity(), R.style.RefTextView);

                if (!phone.equals("-")){
                    Log.i(TAG, "phone: "+phone);
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
                    bt.setBackgroundResource(R.drawable.phone_btn);
                    //Add TextView
                    TextView tv = new TextView(getActivity());
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext3, null
                    ));
                    tv.setTextColor(Color.parseColor("#0F1F38"));
                    tv.setTextSize(20);
                    tv.setText(phone);

                    lh.addView(bt);
                    lh.addView(tv);
                    lm.addView(lh);
                }
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
                    tv.setTextColor(Color.parseColor("#0F1F38"));
                    tv.setText(gmail);

                    lh.addView(bt);
                    lh.addView(tv);
                    lm.addView(lh);
                }
                if (!facebook.equals("-")){
                    Log.i(TAG, "facebook: "+facebook);
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
                    bt.setBackgroundResource(R.drawable.facebook_btn);
                    //Add TextView
                    TextView tv = new TextView(getActivity());
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext3, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(Color.parseColor("#0F1F38"));
                    tv.setText(facebook);

                    lh.addView(bt);
                    lh.addView(tv);
                    lm.addView(lh);
                }
                if (!site.equals("-")){
                    Log.i(TAG, "site: "+site);
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
                    bt.setBackgroundResource(R.drawable.site_btn);
                    //Add TextView
                    TextView tv = new TextView(getActivity());
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext3, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(Color.parseColor("#0F1F38"));
                    tv.setText(site);

                    lh.addView(bt);
                    lh.addView(tv);
                    lm.addView(lh);
                }
                if (!insta.equals("-")){
                    Log.i(TAG, "insta: "+insta);
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
                    bt.setBackgroundResource(R.drawable.insta_btn);
                    //Add TextView
                    TextView tv = new TextView(getActivity());
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            themeContext3, null
                    ));
                    tv.setTextSize(20);
                    tv.setTextColor(Color.parseColor("#0F1F38"));
                    tv.setText(insta);

                    lh.addView(bt);
                    lh.addView(tv);
                    lm.addView(lh);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error in musician info DB, check your connection", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
