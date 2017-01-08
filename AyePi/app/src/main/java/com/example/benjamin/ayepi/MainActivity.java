package com.example.benjamin.ayepi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class MainActivity extends Activity{
    private static final String TAG = MainActivity.class.getSimpleName();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            if (SharedPreferencesManager.getSharedValue(Config.TAG_USERNAME, this) == null){
                startActivity(new Intent(this,Login.class));
            } else {
                Log.d(TAG, "Got Here into main activty");
                startActivity(new Intent(this, MainStatus.class));

            }
        }catch(Exception e) {e.printStackTrace();}
    }
    @Override
    protected void onStart() {
        super.onStart();
        setVisible(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}
