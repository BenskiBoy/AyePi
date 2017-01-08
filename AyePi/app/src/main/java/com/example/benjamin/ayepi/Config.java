package com.example.benjamin.ayepi;


/**
 * Created by Belal on 10/24/2015.
 */
public class Config {

    //Address of our scripts of the CRUD
    public static final String URL_LOCK_GET_STATUS="http://169.254.81.81/Android/AyePi/getLockStatus.php";
    public static final String URL_MOTION_GET_STATUS="http://169.254.81.81/Android/AyePi/getMotionStatus.php";
    public static final String URL_USERS_GET_STATUS="http://169.254.81.81/Android/AyePi/getLocalUsers.php";

    public static final String URL_UPDATE_ARRIVE = "http://169.254.81.81/Android/AyePi/updateArrive.php";
    public static final String URL_UPDATE_LEAVE = "http://169.254.81.81/Android/AyePi/updateLeave.php";
    public static final String URL_ADD_USER = "http://169.254.81.81/Android/AyePi/addUser.php";
    public static final String URL_IS_USER  = "http://169.254.81.81/Android/AyePi/isUser.php";

    //Keys that will be used to send the request to php scripts
    public static final String KEY_AYE_ID = "_userId";
    public static final String KEY_AYE_TIME_STAMP = "_timeStamp";
    public static final String KEY_AYE_STATUS = "_status";
    public static final String KEY_AYE_USERNAME = "_username";
    public static final String KEY_AYE_PASSWORD = "_password";
    public static final String KEY_AYE_MACADRESS ="_mac";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "_id";
    public static final String TAG_STAMP = "_timeStamp";
    public static final String TAG_STATUS = "_status";
    public static final String TAG_USERNAME = "_username";
    public static final String TAG_PASSWORD = "_password";

    //Messages
    public static final String MSG_LOGIN_FAIL = "Invalid Username or Password, Please Re-Enter";
    public static final String MSG_REGISTER_FAIL = "Invalid Username or Password, Please Re-Enter";

    //Program Name
    public static final String PRGM_NAME = "AyePi";
}
