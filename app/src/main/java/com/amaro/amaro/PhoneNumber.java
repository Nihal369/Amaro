package com.amaro.amaro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PhoneNumber extends AppCompatActivity {

    //Object decelerations
    String phoneNumber;
    EditText phoneNumberForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        //Declare the phone number form
        phoneNumberForm=findViewById(R.id.phoneNumberEditText);
    }

    //Change activity
    public void goToOtp(View view)
    {
        phoneNumber=phoneNumberForm.getText().toString();
        LocalDB.setPhoneNumber(phoneNumber);
        Intent intent=new Intent(this,OTP.class);
        startActivity(intent);
    }
}
