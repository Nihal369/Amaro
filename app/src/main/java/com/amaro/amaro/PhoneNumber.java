package com.amaro.amaro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PhoneNumber extends AppCompatActivity {

    TextView phoneNumberText;
    EditText phoneNumberForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        phoneNumberText=findViewById(R.id.phoneNumberTextView);
        phoneNumberForm=findViewById(R.id.phoneNumberEditText);
    }

    public void goToOtp(View view)
    {
        phoneNumberText.setText(R.string.otp);
        phoneNumberForm.setText("");
        phoneNumberForm.setHint(R.string.otp_hint);
    }
}
