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
import java.util.Map;

class AppointmentQueue {

    String appointment;
    AppointmentQueue appointmentQueue;
    static  DatabaseReference mRootRef,userRef,nameRef;

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


    
}
