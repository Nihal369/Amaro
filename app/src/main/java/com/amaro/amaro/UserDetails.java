package com.amaro.amaro;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.text.ParseException;

import es.dmoral.toasty.Toasty;


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
        if(isNetworkAvailable()) {
            day = datePicker.getDayOfMonth();
            month = datePicker.getMonth();
            year = datePicker.getYear();

            dateString = day + "/" + month + "/" + year;

            if (radioGroup.getCheckedRadioButtonId() == R.id.maleRadio) {
                gender = "Male";
            } else {
                gender = "Female";
            }

            Intent intent = new Intent(UserDetails.this, PhoneNumber.class);
            startActivity(intent);
        }
        else
        {
            Toasty.warning(UserDetails.this,"Please Check Your Internet Connection and Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
