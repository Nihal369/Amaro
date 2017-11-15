package com.amaro.amaro;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.DefaultSort;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class Appoinments extends AppCompatActivity {

    //Activity objects
    ImageView profilePic;
    TextView fullNameTextView, appointmentTextView;
    String fullName,email,phoneNumber;
    Uri profilePicUrl;

    //Firebase objects
    static DatabaseReference mRootRef,userRef,nameRef;
    ArrayList<AppointmentQueue> listOfAppointments;
    Map<String, AppointmentQueue> fireBaseMap;
    ArrayList<String> appointmentTexts;

    //Dynamic layout objects
    LinearLayout verticalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoinments);

        //Retrieve data from LocalDB
        fullName=LocalDB.getFullName();
        email=LocalDB.getEmail();
        phoneNumber=LocalDB.getEmail();
        profilePicUrl=LocalDB.getProfilePicUri();

        //View declerations
        profilePic=findViewById(R.id.appoinmentProfilePic);
        fullNameTextView=findViewById(R.id.userNameTextView);
        appointmentTextView =findViewById(R.id.appoinmentTextView);
        verticalLayout=findViewById(R.id.verticalLayout);

        //Set the name of the user
        fullNameTextView.setText(fullName);

        //Set the profile pic of the user
        Picasso.with(this).
                load(profilePicUrl)
                .placeholder(R.drawable.profpic)
                .error(R.drawable.profpic)
                .transform(new CircleTransform())
                .into(profilePic);


        if(!isNetworkAvailable())
        {
            Toasty.warning(Appoinments.this,"Please Check Your Internet Connection and Try Again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Refresh the appointment data
        getAppointmentFromFirebase();
        if(!isNetworkAvailable())
        {
            Toasty.warning(Appoinments.this,"Please Check Your Internet Connection and Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Set the dynamic layout as blank
        verticalLayout.removeAllViews();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Set the dynamic layout as blank
        verticalLayout.removeAllViews();
    }



    //************Static Fucntions****************************/

    //Static function that writes Appointments to Firebase realtime database
    public static void writeAppointmentToFirebase(String doctorName, String dayOfMonth, String monthName, String time, String year)
    {
        getDatabaseReference();

        //Each appointment of user is referred with a key
        String appointmentId = nameRef.push().getKey();

        //Create a new AppointmentQueue Object
        AppointmentQueue appointment=new AppointmentQueue(doctorName,dayOfMonth,monthName,time,year);

        //Add a new appointment to database
        nameRef.child(appointmentId).setValue(appointment);
    }

    private static void getDatabaseReference()
    {
        //Get the database reference
        mRootRef= FirebaseDatabase.getInstance().getReference();
        //Get the node reference
        userRef=mRootRef.child("Users");
        //Users->username
        nameRef=userRef.child(LocalDB.getFullName());
    }

    //************Static Functions****************************/


    //Function that retrieve data from the database
    void getAppointmentFromFirebase()
    {
        getDatabaseReference();
        //Appointment Queue Object to store AppointmentQueue Objects
        listOfAppointments=new ArrayList<>();

        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Erase the past data and sync realtime
                listOfAppointments.clear();

                //Map that stores retrived data from the DataSnapshot
                if(dataSnapshot!=null) {
                    fireBaseMap = (HashMap<String, AppointmentQueue>) dataSnapshot.getValue();
                }

                //Retrieve each value of the map
                retrieveDataFromFirebaseMap();

               //Get all appointment texts
                getAppointmentTexts();

                //Set appointment text
                setAppointmentTexts();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void retrieveDataFromFirebaseMap()
    {
        if(fireBaseMap!=null) {
            //Retrive data from the snapshot map
            for (String key : fireBaseMap.keySet()) {
                listOfAppointments.add(fireBaseMap.get(key));
            }
        }
    }

    private void getAppointmentTexts()
    {
        //Convert the AppointmentQueue objects to String
        //Looping through all
        if(fireBaseMap!=null) {
            Iterator iterator = listOfAppointments.iterator();


            //AppointmentQueue objects as strings are stored here
            appointmentTexts = new ArrayList<String>();

            //Convert each appointment to a string format
            while (iterator.hasNext()) {
                String str = iterator.next().toString();
                String stringToPass = "";
                stringToPass = stringToPass + StringUtils.substringBetween(str, "dayOfMonth=", ",");
                stringToPass = stringToPass + " " + StringUtils.substringBetween(str, "{monthName=", ",");
                stringToPass = stringToPass + " | " + StringUtils.substringBetween(str, "time=", ",");
                stringToPass = stringToPass + " | " + StringUtils.substringBetween(str, "doctorName=", ",");
                appointmentTexts.add(stringToPass);
            }

            //Sort the appointment texts
            Collections.sort(appointmentTexts);
        }
    }

    private void setAppointmentTexts()
    {
        //Set the dynamic layout with appointment texts
        if(fireBaseMap!=null) {
            for (String s : appointmentTexts) {
                //Card view of the dynamic layout
                CardView cardView = new CardView(Appoinments.this);
                LinearLayout.LayoutParams lp = (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        150));
                lp.setMargins(18, 20, 18, 20);
                cardView.setLayoutParams(lp);
                cardView.setCardBackgroundColor(0xff313445);
                cardView.setPadding(30, 30, 30, 30);

                //Text view of the dynamic layout
                TextView textView = new TextView(Appoinments.this);
                LinearLayout.LayoutParams layoutParams = (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                textView.setLayoutParams(layoutParams);
                textView.setGravity(Gravity.CENTER);
                textView.setText(s);
                textView.setTextColor(0xffffffff); // hex color 0xAARRGGBB
                textView.setTextSize(16);
                textView.setTypeface(Typeface.create("monospace", Typeface.NORMAL));

                //Add the text view to card view
                cardView.addView(textView);

                Animator spruceAnimator = new Spruce
                        .SpruceBuilder(verticalLayout)
                        .sortWith(new DefaultSort(/*interObjectDelay=*/50L))
                        .animateWith(new Animator[]{DefaultAnimations.shrinkAnimator(verticalLayout, /*duration=*/800)})
                        .start();

                //Add the card view to linear layout
                verticalLayout.addView(cardView);


            }

            //Set the number of appointments to a text view
            String numberOfAppointments = getString(R.string.appointment) + String.valueOf(listOfAppointments.size());
            appointmentTextView.setText(numberOfAppointments);
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void moveToDoctorList(View v)
   {
       startActivity(new Intent(Appoinments.this,DoctorList.class));
   }

}
