package com.amaro.amaro;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class AppointmentQueue {

    String doctorName,dayOfMonth,monthName,year,time;
    AppointmentQueue appointmentQueue;
    static  DatabaseReference mRootRef,userRef,nameRef;
    static ArrayList<AppointmentQueue> listOfAppointments;
    static Map<String, AppointmentQueue> fireBaseMap;


    public AppointmentQueue(String doctorName,String dayOfMonth,String monthName,String time,String year)
    {
        this.doctorName=doctorName;
        this.dayOfMonth=dayOfMonth;
        this.monthName=monthName;
        this.time=time;
        this.year=year;
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

    public static void getAppointmentFromFirebase()
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
                while (iterator.hasNext()) {
                    //APPLY REGEX AND PASS IT TO APPOINTMENTS.CLASS
                    String str = iterator.next().toString();
                    String stringToPass="";
                    stringToPass=stringToPass+ StringUtils.substringBetween(str,"dayOfMonth=",",");
                    stringToPass=stringToPass + " " + StringUtils.substringBetween(str,"{monthName=",",");
                    stringToPass=stringToPass+" | "+StringUtils.substringBetween(str,"time=",",");
                    stringToPass=stringToPass+" | "+StringUtils.substringBetween(str,"doctorName=",",");
                    Log.i("NIHAL",stringToPass);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
