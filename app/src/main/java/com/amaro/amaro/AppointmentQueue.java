package com.amaro.amaro;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

class AppointmentQueue {

    String appointment;
    AppointmentQueue appointmentQueue;
    static  DatabaseReference mRootRef,userRef,nameRef;
    static ArrayList<AppointmentQueue> listOfAppointments;
    static Map<String, AppointmentQueue> fireBaseMap;

    public AppointmentQueue()
    {

    }

    public AppointmentQueue(String appointment)
    {
        this.appointment=appointment;
    }



    public static void writeAppointmentToFirebase(String appointmentDetails)
    {
        mRootRef= FirebaseDatabase.getInstance().getReference();
        userRef=mRootRef.child("Users");
        nameRef=userRef.child(LocalDB.getFullName());
        String appointmentId = nameRef.push().getKey();
        AppointmentQueue appointment=new AppointmentQueue(appointmentDetails);
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

                fireBaseMap = (HashMap<String,AppointmentQueue>) dataSnapshot.getValue();
                for(String key:fireBaseMap.keySet())
                {
                    listOfAppointments.add(fireBaseMap.get(key));
                }
                Log.i("NIHAL",listOfAppointments.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
