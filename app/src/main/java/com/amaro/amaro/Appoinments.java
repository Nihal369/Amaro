package com.amaro.amaro;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Appoinments extends AppCompatActivity {

    //Old section
    ImageView profilePic;
    TextView fullNameTextView, appointmentTextView;
    String fullName,email,phoneNumber;
    Uri profilePicUrl;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    //Firebase section
    static DatabaseReference mRootRef,userRef,nameRef;
    ArrayList<AppointmentQueue> listOfAppointments;
    Map<String, AppointmentQueue> fireBaseMap;
    ArrayList<String> appointmentTexts;

    //Dynamic layout
    LinearLayout verticalLayout;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoinments);

        fullName=LocalDB.getFullName();
        email=LocalDB.getEmail();
        phoneNumber=LocalDB.getEmail();
        profilePicUrl=LocalDB.getProfilePicUri();


        profilePic=findViewById(R.id.appoinmentProfilePic);
        fullNameTextView=findViewById(R.id.userNameTextView);
        appointmentTextView =findViewById(R.id.appoinmentTextView);
        verticalLayout=findViewById(R.id.verticalLayout);


        Picasso.with(this).
                load(profilePicUrl)
                .placeholder(R.drawable.profpic)
                .error(R.drawable.profpic)
                .transform(new CircleTransform())
                .into(profilePic);

        fullNameTextView.setText(fullName);

        getAppointmentFromFirebase();
    }


    public static void writeAppointmentToFirebase(String doctorName,String dayOfMonth,String monthName,String time,String year)
    {
        mRootRef= FirebaseDatabase.getInstance().getReference();
        userRef=mRootRef.child("Users");
        nameRef=userRef.child(LocalDB.getFullName());
        String appointmentId = nameRef.push().getKey();
        AppointmentQueue appointment=new AppointmentQueue(doctorName,dayOfMonth,monthName,time,year);
        nameRef.child(appointmentId).setValue(appointment);
    }

    void getAppointmentFromFirebase()
    {
        mRootRef= FirebaseDatabase.getInstance().getReference();
        userRef=mRootRef.child("Users");
        nameRef=userRef.child(LocalDB.getFullName());
        listOfAppointments=new ArrayList<>();
        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listOfAppointments.clear();
                fireBaseMap = (HashMap<String,AppointmentQueue>) dataSnapshot.getValue();
                for(String key:fireBaseMap.keySet())
                {
                    listOfAppointments.add(fireBaseMap.get(key));
                }
                Log.i("NIHAL",listOfAppointments.toString());
                Iterator iterator=listOfAppointments.iterator();
                int index=0;
                while (iterator.hasNext()) {
                    //APPLY REGEX AND PASS IT TO APPOINTMENTS.CLASS
                    String str = iterator.next().toString();
                    String stringToPass="";
                    stringToPass=stringToPass + StringUtils.substringBetween(str,"dayOfMonth=",",");
                    stringToPass=stringToPass + " " + StringUtils.substringBetween(str,"{monthName=",",");
                    stringToPass=stringToPass +" | "+StringUtils.substringBetween(str,"time=",",");
                    stringToPass=stringToPass +" | "+StringUtils.substringBetween(str,"doctorName=",",");
                    CardView cardView=new CardView(Appoinments.this);
                    LinearLayout.LayoutParams lp=(new LinearLayout .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            150));
                    lp.setMargins(18,20,18,20);
                    cardView.setLayoutParams(lp);
                    cardView.setCardBackgroundColor(0xff313445);
                    cardView.setPadding(30,30,30,30);
                    TextView textView1 = new TextView(Appoinments.this);
                    LinearLayout.LayoutParams layoutParams=(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));
                    textView1.setLayoutParams(layoutParams);
                    textView1.setGravity(Gravity.CENTER);
                    textView1.setText(stringToPass);
                    textView1.setTextColor(0xffffffff); // hex color 0xAARRGGBB
                    textView1.setTextSize(16);
                    textView1.setTypeface(Typeface.create("monospace", Typeface.NORMAL));
                    cardView.addView(textView1);
                    verticalLayout.addView(cardView);
                    index++;
                }
                String numberOfAppointments=getString(R.string.appointment)+index;
                appointmentTextView.setText(numberOfAppointments);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public void moveToDoctorList(View v)
   {
       startActivity(new Intent(Appoinments.this,DoctorList.class));
   }

}
