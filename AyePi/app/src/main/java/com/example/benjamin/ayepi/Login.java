package com.example.benjamin.ayepi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Benjamin on 30/11/2015.
 */
public class Login extends Activity implements View.OnClickListener {

    private EditText editTextUsername;
    private EditText editTextPassword;

    private Button buttonLogin;
    private Button buttonRegister;

    //Used to see if login process was success
    //easier then returning value from async task
    private boolean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initializing views
        editTextUsername = (EditText) findViewById(R.id.loginUsernameText);
        editTextPassword = (EditText) findViewById(R.id.loginPasswordText);

        buttonLogin = (Button) findViewById(R.id.loginButton);
        buttonRegister = (Button) findViewById(R.id.loginRegisterButton);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
    }

    //TODO check for password stuff
    @Override
    public void onClick(View v) {
        if (v == buttonLogin) {
            login();
            if (result) {
                SharedPreferencesManager.setSharedValue(Config.TAG_USERNAME, editTextUsername.getText().toString(), this);
                startActivity(new Intent(this, MainStatus.class));
            } else {
                editTextUsername.setText("");
                editTextPassword.setText("");
            }
        }

        if (v == buttonRegister) {
            startActivity(new Intent(this, Register.class));
        }
    }

    private void login() {
        class LoginProcess extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Login.this, "Checking...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                int returnValue;

                alertView("Message thing works");

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

                    alertView("Return value was 0");

                    Toast.makeText(getApplicationContext(),
                            Config.MSG_LOGIN_FAIL, Toast.LENGTH_LONG).show();
                    result = false;
                } else {
                    result = true;
                    alertView("TRUE");
                }
            }

            @Override
            /*Send the username and get back if valid user*/
            protected String doInBackground(Void... v) {

                RequestHandler rh = new RequestHandler();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_AYE_USERNAME, username);
                hashMap.put(Config.KEY_AYE_PASSWORD, password);

                String s = rh.sendPostRequest(Config.URL_IS_USER, hashMap);
                return s;
            }
        }
        LoginProcess lg = new LoginProcess();
        lg.execute();
    }

    private void alertView(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Hello")
                .setMessage(message)
//  .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//      public void onClick(DialogInterface dialoginterface, int i) {
//          dialoginterface.cancel();
//          }})
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                }).show();
    }
}