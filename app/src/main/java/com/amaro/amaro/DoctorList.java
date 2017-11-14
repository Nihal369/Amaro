package com.amaro.amaro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DoctorList extends AppCompatActivity {

    //Object decelerations
    ImageView doctorImage1,doctorImage2,doctorImage3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        //Object initializations
        doctorImage1=findViewById(R.id.doctorImage1);
        doctorImage2=findViewById(R.id.doctorImage2);
        doctorImage3=findViewById(R.id.doctorImage3);


        //Set image of first doctor
        Picasso.with(this).
                load(R.drawable.doc1)
                .placeholder(R.drawable.profpic)
                .error(R.drawable.profpic)
                .transform(new CircleTransform())
                .into(doctorImage1);


        //Set image of second doctor
        Picasso.with(this).
                load(R.drawable.doc2)
                .placeholder(R.drawable.profpic)
                .error(R.drawable.profpic)
                .transform(new CircleTransform())
                .into(doctorImage2);

        //Set image of third doctor
        Picasso.with(this).
                load(R.drawable.doc3)
                .placeholder(R.drawable.profpic)
                .error(R.drawable.profpic)
                .transform(new CircleTransform())
                .into(doctorImage3);
    }

    //Move to DoctorPage Activity
    public void goToDoctorPage(View view)
    {
        Intent intent=new Intent(DoctorList.this,DoctorPage.class);
        intent.putExtra("tag",view.getTag().toString());
        startActivity(intent);
    }
}
