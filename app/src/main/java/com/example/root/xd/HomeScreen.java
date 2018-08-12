package com.example.root.xd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.anastr.speedviewlib.AwesomeSpeedometer;
import com.github.anastr.speedviewlib.Gauge;
import com.github.anastr.speedviewlib.SpeedView;
import com.github.anastr.speedviewlib.util.OnSpeedChangeListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;


public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    String URL ;
    JSONParser jsonParser = new JSONParser();
    ProgressDialog dialog;
    SpeedView speedometer;
    TextView batteryperc;
    Switch headlamp;

    static double currSpeed;
    static int battery_percent;
    static String user_name_dame, user_mail_dame, SOS_contacts;

    public class DataAccesss extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(HomeScreen.this,
                    "Loading Data",
                    "Loading...");
            dialog.setCancelable(false);
            dialog.setIcon(R.drawable.ic_loading_data);
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
                    user_name_dame = result.getString("name");
                    user_mail_dame = result.getString("mail");
                    SOS_contacts = result.getString("contacts");
                    set_values();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class UpdateValues extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(HomeScreen.this,
                    "Setting Headlight state",
                    "please wait ...");
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
                    Toast.makeText(getApplicationContext(),result.getString("updated ?"),Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),result.getString("success"),Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class SpeedAccess extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

            JSONObject json1 = jsonParser.makeHttpRequest(URL, "POST", params);
            return json1;
        }

        protected void onPostExecute(JSONObject result) {

            try {
                if (result != null) {
                    currSpeed = result.getDouble("curspeed");
                    battery_percent = result.getInt("battery");
                    set_speed();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void set_values(){
        getSupportActionBar().setTitle(user_name_dame);
        //Toast.makeText(getApplicationContext(), user_name_dame, Toast.LENGTH_LONG).show();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView display_draw_name = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        TextView display_draw_mail = navigationView.getHeaderView(0).findViewById(R.id.user_mail);

        display_draw_name.setText(user_name_dame);
        display_draw_mail.setText(user_mail_dame);
    }

    public void set_speed(){
        //Toast.makeText(getApplicationContext(),String.valueOf(currSpeed),Toast.LENGTH_LONG).show();
        SpeedView speedometer = findViewById(R.id.speedView);
        speedometer.speedTo(Float.valueOf(String.valueOf(currSpeed)) , 1500);
        TextView batteryperc = findViewById(R.id.battery_perc);
        batteryperc.setText(String.valueOf(battery_percent));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences ip_addr = getSharedPreferences("IP", 0);
        URL = ip_addr.getString("url", "");

        speedometer = (SpeedView) findViewById(R.id.speedView);
        batteryperc = (TextView) findViewById(R.id.battery_perc);
        headlamp = (Switch) findViewById(R.id.headlamp);

        HomeScreen.DataAccesss attemptLogin = new HomeScreen.DataAccesss();
        attemptLogin.execute("2","1234", "");
        // All the data has been procured

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sending SOS ..", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                sendSMS(SOS_contacts, "Check message");
            }
        });

        fab.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        speedometer.setTrembleData(1, 5000);
        speedometer.speedTo(0, 1000);
        headlamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean switchon) {
                if(switchon){
                    // headlights on
                    HomeScreen.UpdateValues attemptLogin = new HomeScreen.UpdateValues();
                    attemptLogin.execute("8","1", "");
                }
                else{
                    // headlights off
                    HomeScreen.UpdateValues attemptLogin = new HomeScreen.UpdateValues();
                    attemptLogin.execute("8","0", "");
                }
            }
        });

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SpeedAccess speedreq = new HomeScreen.SpeedAccess();
                                speedreq.execute("6", "1234", "");
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.about_us) {
            //------------------------------------------------------------------------------------//
            Intent change_to_about = new Intent(HomeScreen.this, AboutUSScreen.class);
            startActivity(change_to_about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_location) {

        }*/ if (id == R.id.nav_settings) {
            Intent change_to_setting = new Intent(HomeScreen.this, SettingsScreen.class);
            startActivity(change_to_setting);
            finish();
        /*} else if (id == R.id.nav_mode) {
            Intent change_to_setting = new Intent(HomeScreen.this, ModeScreen.class);
            startActivity(change_to_setting);*/

        } else if (id == R.id.nav_aboutus) {
            Intent change_to_setting = new Intent(HomeScreen.this, AboutUSScreen.class);
            startActivity(change_to_setting);

        } else if (id == R.id.nav_exit) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
