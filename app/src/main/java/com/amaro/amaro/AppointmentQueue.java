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

    public AppointmentQueue(String doctorName,String dayOfMonth,String monthName,String time,String year)
    {
        this.doctorName=doctorName;
        this.dayOfMonth=dayOfMonth;
        this.monthName=monthName;
        this.time=time;
        this.year=year;
    }

}
