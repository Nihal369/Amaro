package com.amaro.amaro;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class AppointmentQueue {
    private String doctorName,monthName,dayOfMonth,time,year;
    AppointmentQueue appointmentQueue;

    public AppointmentQueue()
    {

    }

    public AppointmentQueue(String doctorName,String monthName,String dayOfMonth,String time,String year)
    {
        this.doctorName=doctorName;
        this.monthName=monthName;
        this.dayOfMonth=dayOfMonth;
        this.time=time;
        this.year=year;
    }



    public static void writeAppointmentToFirebase(String appointmentDetails)
    {
        DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef=mRootRef.child("Users");
        DatabaseReference nameRef=userRef.child(LocalDB.getFullName());
        String appointmentId = nameRef.push().getKey();
        nameRef.child(appointmentId).setValue(appointmentDetails);
    }
}
