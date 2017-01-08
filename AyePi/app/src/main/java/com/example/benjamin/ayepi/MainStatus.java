package com.example.benjamin.ayepi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by Benjamin on 30/11/2015.
 */
public class MainStatus extends Activity implements View.OnClickListener{
    //Defining views
    private EditText editLockStatus;
    private EditText editLockTime;

    private EditText editMotionDetection;
    private ListView editListView;

    private Button buttonReset;

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int RESULT_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_status);
        showUserSettings();

        //Initializing views
        editLockStatus = (EditText) findViewById(R.id.lockStatus);
        editLockTime = (EditText) findViewById(R.id.lockTime);

        editMotionDetection = (EditText) findViewById(R.id.motionDetection);

        editListView = (ListView) findViewById(R.id.usersList);

        buttonReset = (Button) findViewById(R.id.buttonReset);

        buttonReset.setOnClickListener(this);

        Log.d(TAG, "this: " + SharedPreferencesManager.getSharedValue("prefSyncFrequency", this));

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        new Refresh().execute();
                    }
                });
            }
        };

        Log.d(TAG, "this: " + Integer.parseInt(SharedPreferencesManager.getSharedValue("prefSyncFrequency", this)));

        timer.schedule(task, 0, Integer.parseInt(SharedPreferencesManager.getSharedValue("prefSyncFrequency", this))*1000);
    }

    //refresh status
        class Refresh extends AsyncTask<Void,Void,String[]> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(com.example.benjamin.ayepi.MainStatus.this,"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String[] s) {
                super.onPostExecute(s);
                loading.dismiss();
                changeView(s);
            }

            @Override
            protected String[] doInBackground(Void... v) {

                RequestHandler rh = new RequestHandler();
                String[] s = new String[3];
                s[0] = rh.sendGetRequest(Config.URL_LOCK_GET_STATUS);
                s[1] = rh.sendGetRequest(Config.URL_MOTION_GET_STATUS);
                s[2] = rh.sendGetRequest(Config.URL_USERS_GET_STATUS);
                return s;
            }
        }

    public void changeView(String[] json)
    {
        ArrayList<HashMap<String, String>> data;
        try {
            JSONObject jsonObjectLock = new JSONObject(json[0]);
            JSONObject jsonObjectMotion = new JSONObject(json[1]);

            JSONArray lockResult = jsonObjectLock.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONArray motionResult = jsonObjectMotion.getJSONArray(Config.TAG_JSON_ARRAY);

            JSONObject c = lockResult.getJSONObject(0);
            String time = c.getString(Config.TAG_STAMP);
            String status = c.getString(Config.TAG_STATUS);

            if (status.equals("1")){
                editLockStatus.setText("Locked");
            }
            else {
                editLockStatus.setText("Open");
            }
            editLockTime.setText(time);

            JSONObject d = motionResult.getJSONObject(0);
            String time_d = d.getString(Config.TAG_STAMP);
            String status_d = d.getString(Config.TAG_STATUS);

            if (status_d.equals("0")){
                editMotionDetection.setText("No Movement Detected");
            }
            else {
                editMotionDetection.setText(time_d);
            }

            data = new ArrayList<HashMap<String, String>>();
            JSONObject jObject = new JSONObject(json[2]);

            JSONArray jArray = jObject.getJSONArray("result");

            for (int i = 0; i < jArray.length(); i++){
                HashMap<String,String> datum = new HashMap<String, String>();
                JSONObject jObj = jArray.getJSONObject(i);

                datum.put("Name", jObj.getString(Config.KEY_AYE_USERNAME));
                datum.put("Date", jObj.getString(Config.KEY_AYE_TIME_STAMP));
                data.add(datum);

            }
            SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[] {"Name", "Date"}, new int[] {android.R.id.text1, android.R.id.text2});
            editListView.setAdapter(adapter);
        }
        catch(JSONException e)
        {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_settings:
                Intent i = new Intent(this, UserSettingActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "that: " + resultCode);

        switch (requestCode) {
            case RESULT_SETTINGS:
                Log.d(TAG, "this: " + resultCode);
                showUserSettings();
                break;
        }
    }

    private void showUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        StringBuilder builder = new StringBuilder();

        builder.append("\n Username: "
                + sharedPrefs.getString("prefUsername", "NULL"));

        builder.append("\n Send report:"
                + sharedPrefs.getBoolean("prefSendReport", false));

        builder.append("\n Sync Frequency: "
                + sharedPrefs.getString("prefSyncFrequency", "NULL"));

        TextView settingsTextView = (TextView) findViewById(R.id.textUserSettings);

        settingsTextView.setText(builder.toString());
    }
    @Override
    public void onClick(View v) {
       if(v == buttonReset) {
           new Refresh().execute();
        }
    }
}
