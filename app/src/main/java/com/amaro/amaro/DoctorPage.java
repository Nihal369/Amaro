package com.amaro.amaro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DoctorPage extends AppCompatActivity {

    ImageView doctorImage;
    TextView doctorDepartment,doctorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_page);

        doctorImage=findViewById(R.id.doctorPicLarge);
        doctorName=findViewById(R.id.doctorName);
        doctorDepartment=findViewById(R.id.doctorDepartment);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String identificationTag=bundle.getString("tag");

        assert identificationTag != null;
        switch (identificationTag)
        {
            case "doc1":
                doctorImage.setBackgroundResource(R.drawable.doc1);
                doctorName.setText(R.string.doc1);
                doctorDepartment.setText(R.string.physician);
                break;

            case "doc2":
                doctorImage.setBackgroundResource(R.drawable.doc2);
                doctorName.setText(R.string.doc2);
                doctorDepartment.setText(R.string.neurologist);
                break;

            case "doc3":
                doctorImage.setBackgroundResource(R.drawable.doc3);
                doctorName.setText(R.string.doc3);
                doctorDepartment.setText(R.string.oncologist);
                break;

        }


    }
}
