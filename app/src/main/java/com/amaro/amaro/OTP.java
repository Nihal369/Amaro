package com.amaro.amaro;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import es.dmoral.toasty.Toasty;


public class OTP extends AppCompatActivity {

    //Object decelerations
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    Intent intent;
    String OTPNumber,sentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


        sentNumber="";

        //Check for the OTP
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                sentNumber=phoneAuthCredential.getSmsCode();
                Toasty.success(OTP.this, "Verification Success", Toast.LENGTH_SHORT).show();
                moveToNextActivity();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toasty.error(OTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // Log.d(TAG, "onCodeSent:" + verificationId);
                Toasty.info(OTP.this, "OTP has been send to your number", Toast.LENGTH_SHORT).show();
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


        if(!isNetworkAvailable())
        {
            Toasty.warning(OTP.this,"Please Check Your Internet Connection and Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    //Function attached to verify button
    public void verifyOTP(View view)
    {
        EditText editText=findViewById(R.id.OTPEditText);
        OTPNumber=editText.getText().toString();

        //If the sms code matches the user entered text
        if(OTPNumber.equals(sentNumber)) {
            moveToNextActivity();
        }
        else
        {
            Toasty.error(OTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
        }
    }

    private void moveToNextActivity()
    {
        intent = new Intent(OTP.this, Appoinments.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        OTP.this.finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
