package com.amaro.amaro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DoctorList extends AppCompatActivity {

    ImageView doctorImage1,doctorImage2,doctorImage3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        doctorImage1=findViewById(R.id.doctorImage1);
        doctorImage2=findViewById(R.id.doctorImage2);
        doctorImage3=findViewById(R.id.doctorImage3);


        Picasso.with(this).
                load(R.drawable.doc1)
                .placeholder(R.drawable.profpic)
                .error(R.drawable.profpic)
                .transform(new CircleTransform())
                .into(doctorImage1);

        Picasso.with(this).
                load(R.drawable.doc2)
                .placeholder(R.drawable.profpic)
                .error(R.drawable.profpic)
                .transform(new CircleTransform())
                .into(doctorImage2);

        Picasso.with(this).
                load(R.drawable.doc3)
                .placeholder(R.drawable.profpic)
                .error(R.drawable.profpic)
                .transform(new CircleTransform())
                .into(doctorImage3);
    }

    public void goToDoctorPage(View view)
    {
        Intent intent=new Intent(DoctorList.this,DoctorPage.class);
        intent.putExtra("tag",view.getTag().toString());
        startActivity(intent);
    }
}
