package com.amaro.amaro;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import es.dmoral.toasty.Toasty;

public class GoogleSignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    SignInButton googleSignInButton;
    FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN=2;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth.AuthStateListener mAuthListener;


    static String phoneNumber;


    @Override
    protected void onStart() {
        super.onStart();
        //Add a listener to check for a google account during app start
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);

        //Decelerations
        googleSignInButton = findViewById(R.id.googleSignInButton);
        FirebaseApp.initializeApp(this);

        //Check if a user has previously logged in using their google account
        firebaseAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //If there exists a signed in user
                if(firebaseAuth.getCurrentUser() != null)
                {
                    //User has already logged in,Jump to Appointment activity
                    FirebaseUser user=firebaseAuth.getCurrentUser();

                    //Assign user details to LocalDB
                    LocalDB.setFullName(user.getDisplayName());
                    LocalDB.setEmail(user.getEmail());
                    LocalDB.setPhoneNumber(user.getPhoneNumber());
                    LocalDB.setProfilePicUri(user.getPhotoUrl());

                    //Jump to appointment activity
                    Intent intent=new Intent(GoogleSignIn.this,Appoinments.class);
                    startActivity(intent);
                    finish();
                }
            }
        };


        //OnClick listener of the google sign in button
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with the options specified by gso.
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toasty.error(GoogleSignIn.this,"Sign in failed",Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }


    //SignIn Function
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    //Result handler of the sign in function
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess())
            {
                //Sign into google account
                GoogleSignInAccount account=result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else
            {
                Toasty.error(this, "Sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Link the google account to firebase
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information;
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            Toasty.success(GoogleSignIn.this, "Success", Toast.LENGTH_SHORT).show();

                            //Retrieve google account data and store it in LocalDB
                            assert user != null;
                            LocalDB.setFullName(user.getDisplayName());
                            LocalDB.setEmail(user.getEmail());
                            LocalDB.setPhoneNumber(user.getPhoneNumber());
                            LocalDB.setProfilePicUri(user.getPhotoUrl());

                            //Continue user registration
                            Intent menuIntent = new Intent(GoogleSignIn.this, UserDetails.class);
                            startActivity(menuIntent);
                            finish();
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toasty.error(GoogleSignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
