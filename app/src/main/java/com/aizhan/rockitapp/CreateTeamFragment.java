package com.aizhan.rockitapp;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aizhan.rockitapp.ClientClasses.OfferInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateTeamFragment extends Fragment {
    private FirebaseAuth auth;
    private DatabaseReference databaseOffers, databaseMyOffers;
    private EditText whoedit;
    private EditText requirementsedit;
    private EditText fromedit;
    private EditText toedit;
    private EditText aboutedit;
    private Button savebtn;
    protected FragmentActivity mActivity;

    private static final String TAG = "MyActivity";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            mActivity = (FragmentActivity) context;
        }
    }


    public CreateTeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_team, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        databaseOffers = FirebaseDatabase.getInstance().getReference("offermain");
        databaseMyOffers = FirebaseDatabase.getInstance().getReference("clientoffers");

        //Get views
        whoedit = (EditText) view.findViewById(R.id.whoEdit);
        requirementsedit = (EditText) view.findViewById(R.id.requirementsEdit);
        fromedit = (EditText) view.findViewById(R.id.fromEdit);
        toedit = (EditText) view.findViewById(R.id.toEdit);
        aboutedit = (EditText) view.findViewById(R.id.aboutEdit);
        savebtn = (Button) view.findViewById(R.id.saveBtn);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String who = whoedit.getText().toString().trim();
                String requirements = requirementsedit.getText().toString().trim();
                String from = fromedit.getText().toString().trim();
                String to = toedit.getText().toString().trim();
                String about = aboutedit.getText().toString().trim();

                if (who.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter the offer name", Toast.LENGTH_SHORT).show();
                }else {
                    if (requirements.isEmpty()){
                        requirements = "Not filled";
                    }
                    String salary = "Not selected";
                    if (from.isEmpty() && !to.isEmpty()){
                        salary = to+"KZT";
                    }else if (to.isEmpty() && !from.isEmpty()){
                        salary = from+"KZT";
                    }else if (!from.isEmpty() && !to.isEmpty()){
                        salary = from+"-"+to+"KZT";
                    }
                    if (about.isEmpty()){
                        about = "Not filled";
                    }

                    //Save info
                    final String offerid = databaseOffers.push().getKey();
                    final OfferInfo offerInfo = new OfferInfo(offerid, who, "-", "-", salary, requirements, about, "team");
                    databaseOffers.child(offerid).setValue(offerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(mActivity, "Offer info added!",
                                        Toast.LENGTH_SHORT).show();
                                databaseMyOffers.child(auth.getCurrentUser().getUid()).child(offerid).setValue(offerInfo);
                                mActivity.finish();
                            }else{
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
