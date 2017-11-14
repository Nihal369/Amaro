package com.amaro.amaro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import com.squareup.picasso.Picasso;
import java.text.ParseException;


public class UserDetails extends AppCompatActivity {

    //Object decelerations
    DatePicker datePicker;
    RadioGroup radioGroup;
    ImageView profilePic;
    EditText fullNameEditText;


    int day,month,year;
    String dateString;
    static String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        //Object initializations
        fullNameEditText= findViewById(R.id.fullNameEditText);
        profilePic=findViewById(R.id.profilePic);
        datePicker=findViewById(R.id.datePicker);
        radioGroup=findViewById(R.id.radioGroup);


        //Retrieve full name from LocalDB
        String fullName=LocalDB.getFullName();
        if(fullName!=null)
        {
            fullNameEditText.setText(fullName);
        }

        //Set profile pic
        Picasso.with(this).
                load(LocalDB.getProfilePicUri())
                .placeholder(R.drawable.profpic)
                .error(R.drawable.profpic)
                .transform(new CircleTransform())
                .into(profilePic);
    }

    //Retrieve user details and move to next activity
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
