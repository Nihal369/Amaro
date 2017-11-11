package com.amaro.amaro;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.net.URI;

public class Appoinments extends AppCompatActivity {

    ImageView profilePic;
    TextView fullNameTextView, AppointmentTextView;
    String fullName,email,phoneNumber;
    Uri profilePicUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoinments);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        fullName = bundle.getString("fullName");
        email=bundle.getString("email");
        phoneNumber=bundle.getString("phoneNumber");
        profilePicUrl= Uri.parse(bundle.getString("profilePicUrl"));


        profilePic=findViewById(R.id.appoinmentProfilePic);
        fullNameTextView=findViewById(R.id.userNameTextView);
        AppointmentTextView =findViewById(R.id.appoinmentTextView);


        Picasso.with(this).
                load(profilePicUrl)
                .placeholder(R.drawable.profpic)
                .error(R.drawable.profpic)
                .transform(new CircleTransform())
                .into(profilePic);

        fullNameTextView.setText(fullName);
    }


   public void moveToDoctorList(View v)
   {
       startActivity(new Intent(Appoinments.this,DoctorList.class));
   }
}
