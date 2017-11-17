package com.amaro.amaro;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class DoctorPage extends AppCompatActivity {

    //Object decelerations
    Bundle bundle;
    ImageView doctorImage;
    TextView doctorDepartment, doctorName, date1Text, date2Text, date3Text, date4Text;
    Calendar calendar;
    Date today;
    int[] month, dayOfMonth, dayOfWeek, year;
    int index;
    String[] monthName;
    String date1, date2, date3, date4, identificationTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_page);

        //Object initializations
        date1Text = findViewById(R.id.date1);
        date2Text = findViewById(R.id.date2);
        date3Text = findViewById(R.id.date3);
        date4Text = findViewById(R.id.date4);

        doctorImage = findViewById(R.id.doctorPicLarge);
        doctorName = findViewById(R.id.doctorName);
        doctorDepartment = findViewById(R.id.doctorDepartment);

        retrieveDataFromIntent();

        setDoctorPage();

        setDate();

        if (!isNetworkAvailable()) {
            Toasty.warning(DoctorPage.this, "Please Check Your Internet Connection and Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    private void retrieveDataFromIntent() {
        bundle = getIntent().getExtras();
        assert bundle != null;
        identificationTag = bundle.getString("tag");
    }

    private void setDoctorPage() {
        assert identificationTag != null;
        switch (identificationTag) {
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

    //Function that gets the next 4 working days for booking appointments
    void setDate() {
        //Declare calendar object
        calendar = Calendar.getInstance();
        today = calendar.getTime();

        //Set the calendar time
        calendar.setTime(today);

        //Initializing the loop variable to 0
        index = 0;

        //Array of  size 4 to store the next 4 working days
        month = new int[4];
        dayOfMonth = new int[4];
        dayOfWeek = new int[4];
        year = new int[4];
        monthName = new String[4];

        //Get the the next 4 working days
        for (; index < 4; index++) {
            //Only add 1 day to next 3 days
            if (index != 0) {
                calendar.add(Calendar.DATE, 1);
            }

            assignDates();
            dayOfWeek[index] = calendar.get(Calendar.DAY_OF_WEEK);

            //Check the day is a sunday or saturday and do the appropriate
            checkIfSundayOrSaturday();
        }

        setDateTexts();

    }

    private void assignDates() {
        dayOfMonth[index] = calendar.get(Calendar.DAY_OF_MONTH);
        month[index] = calendar.get(Calendar.MONTH);
        monthName[index] = getMonthName(month[index]);
        year[index] = calendar.get(Calendar.YEAR);
    }

    private void setDateTexts() {

        date1 = monthName[0] + " " + dayOfMonth[0];
        date2 = monthName[1] + " " + dayOfMonth[1];
        date3 = monthName[2] + " " + dayOfMonth[2];
        date4 = monthName[3] + " " + dayOfMonth[3];


        date1Text.setText(date1);
        date2Text.setText(date2);
        date3Text.setText(date3);
        date4Text.setText(date4);
    }

    //Check if the day is sunday or saturday and add 1 or 2 days if its true
    void checkIfSundayOrSaturday() {
        //Check if the day is sunday
        if (dayOfWeek[index] == Calendar.SUNDAY) {
            //Add 1 day to sunday,update the variables;
            calendar.add(Calendar.DATE, 1);
            assignDates();
        }
        //Check if the day is saturday
        else if (dayOfWeek[index] == Calendar.SATURDAY) {
            //Add 2 days to saturday,Update the variables
            calendar.add(Calendar.DATE, 2);
            assignDates();
        }

    }

    //Return month name to the corresponding month index
    String getMonthName(int month) {
        switch (month) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
            default:
                return "Jan";
        }
    }

    public void bookAppointment(final View view) {
        new AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setTitle("Confirm Appointment")
                .setMessage("Are you sure you want to make the Appointment")
                .setIcon(R.drawable.alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isNetworkAvailable()) {
                            switch (view.getId()) {
                                case R.id.tenCard1:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[0]), monthName[0]
                                            , "10:00", String.valueOf(year[0]));
                                    addEventToCalendar(year[0], month[0], dayOfMonth[0], 10,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 10:00 on " + dayOfMonth[0] + " " + monthName[0] + " " + year[0],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.elevenCard1:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[0]), monthName[0]
                                            , "11:00", String.valueOf(year[0]));
                                    addEventToCalendar(year[0], month[0], dayOfMonth[0], 11,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 11:00 on " + dayOfMonth[0] + " " + monthName[0] + " " + year[0],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.twelveCard1:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[0]), monthName[0]
                                            , "12:00", String.valueOf(year[0]));
                                    addEventToCalendar(year[0], month[0], dayOfMonth[0], 12,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 12:00 on " + dayOfMonth[0] + " " + monthName[0] + " " + year[0],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.fourteenCard1:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[0]), monthName[0]
                                            , "14:00", String.valueOf(year[0]));
                                    addEventToCalendar(year[0], month[0], dayOfMonth[0], 14,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 14:00 on " + dayOfMonth[0] + " " + monthName[0] + " " + year[0],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.fifteenCard1:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[0]), monthName[0]
                                            , "15:00", String.valueOf(year[0]));
                                    addEventToCalendar(year[0], month[0], dayOfMonth[0], 15,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 15:00 on " + dayOfMonth[0] + " " + monthName[0] + " " + year[0],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.sixteenCard1:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[0]), monthName[0]
                                            , "16:00", String.valueOf(year[0]));
                                    addEventToCalendar(year[0], month[0], dayOfMonth[0], 16,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 16:00 on " + dayOfMonth[0] + " " + monthName[0] + " " + year[0],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.tenCard2:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[1]), monthName[1]
                                            , "10:00", String.valueOf(year[1]));
                                    addEventToCalendar(year[1], month[1], dayOfMonth[1], 10,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 10:00 on " + dayOfMonth[1] + " " + monthName[1] + " " + year[1],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.elevenCard2:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[1]), monthName[1]
                                            , "11:00", String.valueOf(year[1]));
                                    addEventToCalendar(year[1], month[1], dayOfMonth[1], 11,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 11:00 on " + dayOfMonth[1] + " " + monthName[1] + " " + year[1],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.twelveCard2:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[1]), monthName[1]
                                            , "12:00", String.valueOf(year[1]));
                                    addEventToCalendar(year[1], month[1], dayOfMonth[1], 12,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 12:00 on " + dayOfMonth[1] + " " + monthName[1] + " " + year[1],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.fourteenCard2:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[1]), monthName[1]
                                            , "14:00", String.valueOf(year[1]));
                                    addEventToCalendar(year[1], month[1], dayOfMonth[1], 14,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 14:00 on " + dayOfMonth[1] + " " + monthName[1] + " " + year[1],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.fifteenCard2:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[1]), monthName[1]
                                            , "15:00", String.valueOf(year[1]));
                                    addEventToCalendar(year[1], month[1], dayOfMonth[1], 15,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 15:00 on " + dayOfMonth[1] + " " + monthName[1] + " " + year[1],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.sixteenCard2:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[1]), monthName[1]
                                            , "16:00", String.valueOf(year[1]));
                                    addEventToCalendar(year[1], month[1], dayOfMonth[1], 16,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 16:00 on " + dayOfMonth[1] + " " + monthName[1] + " " + year[1],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.tenCard3:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[2]), monthName[2]
                                            , "10:00", String.valueOf(year[2]));
                                    addEventToCalendar(year[2], month[2], dayOfMonth[2], 10,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 10:00 on " + dayOfMonth[2] + " " + monthName[2] + " " + year[2],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.elevenCard3:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[2]), monthName[2]
                                            , "11:00", String.valueOf(year[2]));
                                    addEventToCalendar(year[2], month[2], dayOfMonth[2], 11,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 11:00 on " + dayOfMonth[2] + " " + monthName[2] + " " + year[2],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.twelveCard3:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[2]), monthName[2]
                                            , "12:00", String.valueOf(year[2]));
                                    addEventToCalendar(year[2], month[2], dayOfMonth[2], 12,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 12:00 on " + dayOfMonth[2] + " " + monthName[2] + " " + year[2],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.fourteenCard3:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[2]), monthName[2]
                                            , "14:00", String.valueOf(year[2]));
                                    addEventToCalendar(year[2], month[2], dayOfMonth[2], 14,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 14:00 on " + dayOfMonth[2] + " " + monthName[2] + " " + year[2],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.fifteenCard3:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[2]), monthName[2]
                                            , "15:00", String.valueOf(year[2]));
                                    addEventToCalendar(year[2], month[2], dayOfMonth[2], 15,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 15:00 on " + dayOfMonth[2] + " " + monthName[2] + " " + year[2],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.sixteenCard3:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[2]), monthName[2]
                                            , "16:00", String.valueOf(year[2]));
                                    addEventToCalendar(year[2], month[2], dayOfMonth[2], 16,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 16:00 on " + dayOfMonth[2] + " " + monthName[2] + " " + year[2],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.tenCard4:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[3]), monthName[3]
                                            , "10:00", String.valueOf(year[3]));
                                    addEventToCalendar(year[3], month[3], dayOfMonth[3], 10,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 10:00 on " + dayOfMonth[3] + " " + monthName[3] + " " + year[3],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.elevenCard4:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[3]), monthName[3]
                                            , "11:00", String.valueOf(year[3]));
                                    addEventToCalendar(year[3], month[3], dayOfMonth[3], 11,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 11:00 on " + dayOfMonth[3] + " " + monthName[3] + " " + year[3],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.twelveCard4:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[3]), monthName[3]
                                            , "12:00", String.valueOf(year[3]));
                                    addEventToCalendar(year[3], month[3], dayOfMonth[3], 12,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 12:00 on " + dayOfMonth[3] + " " + monthName[3] + " " + year[3],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.fourteenCard4:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[3]), monthName[3]
                                            , "14:00", String.valueOf(year[3]));
                                    addEventToCalendar(year[3], month[3], dayOfMonth[3], 14,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 14:00 on " + dayOfMonth[3] + " " + monthName[3] + " " + year[3],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.fifteenCard4:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[3]), monthName[3]
                                            , "15:00", String.valueOf(year[3]));
                                    addEventToCalendar(year[3], month[3], dayOfMonth[3], 15,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 15:00 on " + dayOfMonth[3] + " " + monthName[3] + " " + year[3],
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.sixteenCard4:
                                    //Make appointment
                                    Appoinments.writeAppointmentToFirebase(getDocById(identificationTag), String.valueOf(dayOfMonth[3]), monthName[3]
                                            , "16:00", String.valueOf(year[3]));
                                    addEventToCalendar(year[3], month[3], dayOfMonth[3], 16,
                                            "Appointment With " + getDocById(identificationTag));
                                    Toasty.info(DoctorPage.this,
                                            "Appointment made for " + getDocById(identificationTag) + " at 15:00 on " + dayOfMonth[3] + " " + monthName[3] + " " + year[3],
                                            Toast.LENGTH_LONG).show();
                                    break;
                            }

                        } else {
                            Toasty.warning(DoctorPage.this, "Please Check Your Internet Connection and Try Again", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("No", null).show();
    }

    //Function that adds event to calendar
    private void addEventToCalendar(int year, int month, int day, int hour, String appointmentWithDoctor) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, 0);
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("endTime", cal.getTimeInMillis() + 3600000);
        intent.putExtra("title", appointmentWithDoctor);
        startActivity(intent);
    }

    //Get doctor name by passing the identification tag
    private String getDocById(String id)
    {
        switch (id)
        {
            case "doc1":return "Dr. Steve Becker";
            case "doc2":return "Dr. Sophia Aiden";
            case "doc3":return "Dr. Allison Reed";
            default:return "Dr. Steve Becker";
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
