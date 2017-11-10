package com.amaro.amaro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDetails extends AppCompatActivity {

    DatePicker datePicker;
    RadioGroup radioGroup;


    int day,month,year;
    String dateString;
    static Date date;
    static String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        EditText fullNameEditText=(EditText)findViewById(R.id.fullNameEditText);
        String fullName=GoogleSignIn.getFullName();
        if(fullName!=null)
        {
            fullNameEditText.setText(fullName);
        }

        datePicker=findViewById(R.id.datePicker);
        radioGroup=findViewById(R.id.radioGroup);
    }

    public void OnClick(View view) throws ParseException {
       day=datePicker.getDayOfMonth();
       month=datePicker.getMonth();
       year=datePicker.getYear();
       dateString = day + "/" + month + "/"+year;

       if(radioGroup.getCheckedRadioButtonId()==R.id.maleRadio)
       {
           gender="Male";
       }
       else
       {
           gender="Female";
       }

       Intent intent=new Intent(UserDetails.this,PhoneNumber.class);
       startActivity(intent);
    }
}
