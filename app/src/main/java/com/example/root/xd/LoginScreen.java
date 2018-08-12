package com.example.root.xd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.philio.pinentry.PinEntryView;

/*
    PASSCODE 1 : Login Request
    PASSCODE 2 : User Data
    PASSCODE 3 : Update Password
    PASSCODE 4 : Update Name
    PASSCODE 5 : Update Mail
    PASSCODE 6 : Speed and Battery
    PASSCODE 7 : Update SOS
    PASSCODE 8 : Headlights control
    PASSCODE 9 : Change mode
                    1 - normal
                    2 - bike
                    3 - segway
/*

Places to update IP :

*/

public class LoginScreen extends AppCompatActivity {

    JSONParser jsonParser = new JSONParser();
    String URL = "http://13.232.177.132/htdocs/loginfile.php";

    public int passCorrect = 0;
    ProgressDialog dialog;

    private class AttemptLogin extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(LoginScreen.this,
                    "Signing In",
                    "Loading...");
            dialog.setCancelable(false);
            dialog.setIcon(R.drawable.ic_security);
        }

        @Override

        protected JSONObject doInBackground(String... args) {

            String email = args[2];
            String password = args[1];
            String name= args[0];


            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", name));
            params.add(new BasicNameValuePair("password", password));
            if(email.length()>0)
                params.add(new BasicNameValuePair("email",email));

            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);
            return json;
        }

        protected void onPostExecute(JSONObject result) {

            dialog.dismiss();
            try {
                if (result != null) {
                    int ret = result.getInt("auth");
                    if(ret == 1) {
                        Intent homeIntent = new Intent(LoginScreen.this, HomeScreen.class);
                        startActivity(homeIntent);
                        finish();
                        Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        final PinEntryView pinEntered = (PinEntryView) findViewById(R.id.pinEntryView);
        pinEntered.requestFocus();
        final TextView LoginLink = (TextView) findViewById(R.id.login_link);
        String link_to_page = "<a href='http://13.232.177.132/htdocs/help.html'>Problem&#x0020;logging&#x0020;in&#x0020;?</a>";
        LoginLink.setText(Html.fromHtml(link_to_page));
        LoginLink.setLinksClickable(true);
        LoginLink.setMovementMethod(LinkMovementMethod.getInstance());





        SharedPreferences ip_add = getSharedPreferences("IP", 0);
        SharedPreferences.Editor edit_ip = ip_add.edit();
        edit_ip.putString("url", URL);
        edit_ip.commit();


        pinEntered.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (pinEntered.getText().length() == 4) {
                    AttemptLogin attemptLogin = new AttemptLogin();
                    attemptLogin.execute("1", pinEntered.getText().toString(), "");
                }
            }
        });

    }

}
