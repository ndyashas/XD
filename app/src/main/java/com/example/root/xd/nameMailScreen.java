package com.example.root.xd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.philio.pinentry.PinEntryView;

public class nameMailScreen extends AppCompatActivity {

    String URL; //= "http://192.168.225.22/loginfile.php";
    JSONParser jsonParser = new JSONParser();
    ProgressDialog dialog;

    private class UpdateValues extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(nameMailScreen.this,
                    "Updating Data",
                    "Loading...");
            dialog.setCancelable(false);
            dialog.setIcon(R.drawable.ic_update_data);
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

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            dialog.dismiss();
            try {
                if (result != null) {
                    Toast.makeText(getApplicationContext(),result.getString("success"),Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_name_mail_screen);
        getSupportActionBar().setTitle("Settings");

        final EditText updateName = (EditText)findViewById(R.id.update_name);
        final PinEntryView updatePassword = (PinEntryView) findViewById(R.id.update_password);
        final Button updateNameButton = (Button) findViewById(R.id.update_name_button);
        final Button updatePasswordButton = (Button) findViewById(R.id.update_password_button);
        SharedPreferences ip_addr = getSharedPreferences("IP", 0);
        URL = ip_addr.getString("url", "");

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameMailScreen.UpdateValues attemptedUpdate = new nameMailScreen.UpdateValues();
                attemptedUpdate.execute("3", updatePassword.getText().toString(), "");
            }
        });

        updateNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameMailScreen.UpdateValues attemptedUpdate = new nameMailScreen.UpdateValues();
                attemptedUpdate.execute("4", updateName.getText().toString(), "");
            }
        });

        //updatePasswordButton.setClickable(false);
        updatePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(updatePassword.getText().length() == 4){
                    updatePasswordButton.setClickable(true);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent change_to_settings = new Intent(nameMailScreen.this, HomeScreen.class);
        startActivity(change_to_settings);
        finish();
    }

}
