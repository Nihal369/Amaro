package com.amaro.amaro;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;


public class OTP extends AppCompatActivity {

    //Object decelerations
    private static final String TAG = "OTP";
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;

    String OTPNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        //Get firebaseAuth Instance
        mAuth = FirebaseAuth.getInstance();


        //Instantiate a phone verification process
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                mVerificationInProgress = false;
                Toast.makeText(OTP.this, "Verification Complete", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e(TAG,String.valueOf(e));
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
        if(Objects.equals(credential.getSmsCode(), OTPNumber)) {
            Intent intent = new Intent(OTP.this, Appoinments.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            OTP.this.finish();
        }
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(OTP.this,"Verification Done",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(OTP.this, Appoinments.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            OTP.this.finish();
                            // ...
                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(OTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

}
