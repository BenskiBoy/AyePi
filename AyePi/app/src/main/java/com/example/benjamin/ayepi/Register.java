package com.example.benjamin.ayepi;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Benjamin on 30/11/2015.
 */


public class Register extends Activity implements View.OnClickListener{

    private EditText editTextUsername;
    private EditText editTextPassword;

    private Button buttonRegister;

    //Used to see if login process was success
    //easier then returning value from async task
    private boolean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = (EditText) findViewById(R.id.registerUsername);
        editTextPassword = (EditText) findViewById(R.id.registerPassword);

        buttonRegister = (Button) findViewById(R.id.registerRegisterButton);

        buttonRegister.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        register();
        if (result)
        {
            SharedPreferencesManager.setSharedValue(Config.TAG_USERNAME, editTextUsername.getText().toString(), this);
            SharedPreferencesManager.setSharedValue(Config.TAG_PASSWORD, editTextPassword.getText().toString(), this);
            startActivity(new Intent(this, MainStatus.class));
        }
        else
        {   }
    }

    private void register() {
        class RegisterProcess extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();
            String macAddress = getMAC();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "Registering...", "Wait...", false, false);
            }

            //TODO Extra function to check to see if username taken.
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                int returnValue;

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    returnValue = jsonObject.getInt("result");

                } catch (JSONException e) {
                    e.printStackTrace();
                    returnValue = 0;
                }

                if (returnValue == 0) {
                    editTextPassword.setText("");
                    editTextUsername.setText("");
                    Toast.makeText(getApplicationContext(),
                            Config.MSG_REGISTER_FAIL, Toast.LENGTH_LONG).show();
                    result = false;
                } else {
                    result = true;
                }
            }

            @Override
            /*Send the username and get back if valid user*/
            protected String doInBackground(Void... v) {

                RequestHandler rh = new RequestHandler();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_AYE_USERNAME, username);
                hashMap.put(Config.KEY_AYE_PASSWORD, password);
                hashMap.put(Config.KEY_AYE_MACADRESS, macAddress);

                String s = rh.sendPostRequest(Config.URL_ADD_USER, hashMap);
                return s;
            }
        }
        RegisterProcess rp = new RegisterProcess();
        rp.execute();
    }


    protected String getMAC(){
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }
}
