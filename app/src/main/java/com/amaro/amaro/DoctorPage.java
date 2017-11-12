package com.amaro.amaro;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;

public class DoctorPage extends AppCompatActivity {

    ImageView doctorImage;
    TextView doctorDepartment,doctorName,date1Text,date2Text,date3Text,date4Text;
    Calendar calendar;
    Date today;
    int[] month,dayOfMonth,dayOfWeek,year;
    int index;
    String[] monthName;
    String date1,date2,date3,date4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_page);

        date1Text=findViewById(R.id.date1);
        date2Text=findViewById(R.id.date2);
        date3Text=findViewById(R.id.date3);
        date4Text=findViewById(R.id.date4);

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

        setDate();
    }

    void setDate()
    {
        calendar=Calendar.getInstance();
        today=calendar.getTime();
        calendar.setTime(today);

        index=0;

        month = new int[4];
        dayOfMonth = new int[4];
        dayOfWeek = new int[4];
        year = new int[4];
        monthName = new String[4];

        for(;index<4;index++) {
            if(index!=0)
            {
                calendar.add(Calendar.DATE,1);
            }
            dayOfMonth[index] = calendar.get(Calendar.DAY_OF_MONTH);
            month[index] = calendar.get(Calendar.MONTH);
            monthName[index] = getMonthName(month[index]);
            year[index] = calendar.get(Calendar.YEAR);
            dayOfWeek[index] = calendar.get(Calendar.DAY_OF_WEEK);
            checkIfSundayOrSaturday();
        }

        date1=monthName[0]+" "+dayOfMonth[0];
        date2=monthName[1]+" "+dayOfMonth[1];
        date3=monthName[2]+" "+dayOfMonth[2];
        date4=monthName[3]+" "+dayOfMonth[3];


        date1Text.setText(date1);
        date2Text.setText(date2);
        date3Text.setText(date3);
        date4Text.setText(date4);

    }

    void checkIfSundayOrSaturday()
    {
        if(dayOfWeek[index]==Calendar.SUNDAY)
        {
            calendar.add(Calendar.DATE,1);
            dayOfMonth[index]=calendar.get(Calendar.DAY_OF_MONTH);
            month[index] = calendar.get(Calendar.MONTH);
            monthName[index] = getMonthName(month[index]);
            year[index] = calendar.get(Calendar.YEAR);
        }
        else if(dayOfWeek[index]==Calendar.SATURDAY)
        {
            calendar.add(Calendar.DATE,2);
            dayOfMonth[index]=calendar.get(Calendar.DAY_OF_MONTH);
            month[index] = calendar.get(Calendar.MONTH);
            monthName[index] = getMonthName(month[index]);
            year[index] = calendar.get(Calendar.YEAR);
        }

    }

    String getMonthName(int month)
    {
        switch (month)
        {
            case 0:return "Jan";
            case 1:return "Feb";
            case 2:return "Mar";
            case 3:return "Apr";
            case 4:return "May";
            case 5:return "Jun";
            case 6:return "Jul";
            case 7:return "Aug";
            case 8:return "Sep";
            case 9:return "Oct";
            case 10:return "Nov";
            case 11:return "Dec";
            default:return "Jan";
        }
    }

    public void bookAppointment(View view)
    {
       new AlertDialog.Builder(this,R.style.MyDialogTheme)
               .setTitle("Confirm Appointment")
               .setMessage("Please confirm your appointment with Dr Philips")
               .setIcon(R.drawable.alert)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                   }
               })
               .setNegativeButton("No",null).show();
    }
}
