package com.amaro.amaro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PhoneNumber extends AppCompatActivity {

    String phoneNumber;
    EditText phoneNumberForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        phoneNumberForm=findViewById(R.id.phoneNumberEditText);
    }

    public void goToOtp(View view)
    {
        phoneNumber=phoneNumberForm.getText().toString();
        GoogleSignIn.setPhoneNumber(phoneNumber);
        Intent intent=new Intent(this,OTP.class);
        startActivity(intent);
    }
}
