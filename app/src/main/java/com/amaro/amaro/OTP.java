package com.amaro.amaro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


public class OTP extends AppCompatActivity {

    //Object decelerations
    private static final String TAG = "OTP";
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;

    Intent intent;
    String OTPNumber,sentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        //Get firebaseAuth Instance
        mAuth = FirebaseAuth.getInstance();

        sentNumber="";

        //Check for the OTP
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                sentNumber=phoneAuthCredential.getSmsCode();
                Toast.makeText(OTP.this, "Verification Success", Toast.LENGTH_SHORT).show();
                moveToNextActivity();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(OTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(OTP.this, "OTP has been send to your number", Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        //Phone Auth Parameters
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                LocalDB.getPhoneNumber(),
                60,
                java.util.concurrent.TimeUnit.SECONDS,
                OTP.this,
                mCallbacks);

    }

    //Function attached to verify button
    public void verifyOTP(View view)
    {
        EditText editText=findViewById(R.id.OTPEditText);
        OTPNumber=editText.getText().toString();

        //Get the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,OTPNumber);

        //If the sms code matches the user entered text
        if(OTPNumber.equals(sentNumber)) {
            moveToNextActivity();
        }
        else
        {
            Toast.makeText(OTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
        }
    }

    private void moveToNextActivity()
    {
        intent = new Intent(OTP.this, Appoinments.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        OTP.this.finish();
    }

}
