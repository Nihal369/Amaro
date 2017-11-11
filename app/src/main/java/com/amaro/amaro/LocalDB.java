package com.amaro.amaro;

import android.net.Uri;

public class LocalDB {

    private static String fullName,phoneNumber,email;
    static Uri profilePicUri;


    public static void setFullName(String value)
    {
        fullName=value;
    }

    public static void setPhoneNumber(String value)
    {
        phoneNumber=value;
    }

    public static void setEmail(String value)
    {
        email=value;
    }

    public static void setProfilePicUri(Uri value)
    {
        profilePicUri=value;
    }

    public static String getFullName()
    {
        return fullName;
    }

    public static String getPhoneNumber()
    {
        return "+91"+phoneNumber;
    }

    public static String getEmail()
    {
        return email;
    }

    public static Uri getProfilePicUri()
    {
        return  profilePicUri;
    }

}
