package com.example.root.xd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Timer;

public class SettingsScreen extends AppCompatActivity {


    String URL;
    final JSONParser jsonParser = new JSONParser();
    ProgressDialog dialog;
    Context context = this;
    public EditText mailupdate;
    public EditText nameupdate;
    public EditText passwordupdate;
    public EditText sosupdate;

    public Button button_segway;
    public Button button_bike;
    public Button button_normal;


    private class UpdateValues extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(SettingsScreen.this,
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


    final public void update_to_xd(String update, String value){
        UpdateValues attemptedUpdate = new UpdateValues();
        attemptedUpdate.execute(update, value, "");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
        getSupportActionBar().setTitle("Settings");

        SharedPreferences ip_addr = getSharedPreferences("IP", 0);
        URL = ip_addr.getString("url", "");

        mailupdate = (EditText)findViewById(R.id.mailupdate);
        mailupdate.setVisibility(View.INVISIBLE);
        nameupdate = (EditText)findViewById(R.id.nameupdate);
        nameupdate.setVisibility(View.INVISIBLE);
        passwordupdate = (EditText)findViewById(R.id.passwordupdate);
        passwordupdate.setVisibility(View.INVISIBLE);
        sosupdate = (EditText)findViewById(R.id.sosupdate);
        sosupdate.setVisibility(View.INVISIBLE);
        button_segway = (Button) findViewById(R.id.button_segway);
        button_bike = (Button) findViewById(R.id.button_bike);
        button_normal = (Button) findViewById(R.id.button_normal);

        button_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsScreen.this);
                builder.setMessage("Are you sure on changing the mode to normal ?");
                builder.setIcon(R.drawable.ic_security);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UpdateValues changemode = new UpdateValues();
                        changemode.execute("9", "1", "");
                        //Toast.makeText(getApplicationContext(), "yup here", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        button_bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsScreen.this);
                builder.setMessage("Are you sure on changing the mode to bike ?");
                builder.setIcon(R.drawable.ic_security);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UpdateValues changemode = new UpdateValues();
                        changemode.execute("9", "2", "");
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        button_segway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsScreen.this);
                builder.setMessage("Are you sure on changing the mode to segway ?");
                builder.setIcon(R.drawable.ic_security);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UpdateValues changemode = new UpdateValues();
                        changemode.execute("9", "3", "");
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        final TextView updatemail = (TextView)findViewById(R.id.update_mail);
        final TextView updatename = (TextView)findViewById(R.id.update_name);
        final TextView updatepassword = (TextView)findViewById(R.id.update_password);
        final TextView updatesos = (TextView)findViewById(R.id.update_sos);

        updatesos.setVisibility(View.INVISIBLE);

        updatemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsview = li.inflate(R.layout.update_mail_screen, null);

                AlertDialog.Builder alertDialogueBuilder = new AlertDialog.Builder(context);
                alertDialogueBuilder.setView(promptsview);

                final EditText new_mail = (EditText)promptsview.findViewById(R.id.new_mail);

                alertDialogueBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //result.setText(new_mail.getText());
                        String hi = new_mail.getText().toString();
                        mailupdate.setText(hi);
                    }
                });

                alertDialogueBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogueBuilder.create();
                alertDialog.show();
            }
        });

        updatename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsview = li.inflate(R.layout.update_name_screen, null);

                AlertDialog.Builder alertDialogueBuilder = new AlertDialog.Builder(context);
                alertDialogueBuilder.setView(promptsview);

                final EditText new_mail = (EditText)promptsview.findViewById(R.id.new_name);

                alertDialogueBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //result.setText(new_mail.getText());
                        String hi = new_mail.getText().toString();
                        nameupdate.setText(hi);
                    }
                });

                alertDialogueBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogueBuilder.create();
                alertDialog.show();
            }
        });

        updatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsview = li.inflate(R.layout.update_password_screen, null);

                AlertDialog.Builder alertDialogueBuilder = new AlertDialog.Builder(context);
                alertDialogueBuilder.setView(promptsview);

                final EditText new_mail = (EditText)promptsview.findViewById(R.id.new_password);

                alertDialogueBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //result.setText(new_mail.getText());
                        String hi = new_mail.getText().toString();
                        passwordupdate.setText(hi);
                    }
                });

                alertDialogueBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogueBuilder.create();
                alertDialog.show();
            }
        });

        updatesos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsview = li.inflate(R.layout.update__sos_screen, null);

                AlertDialog.Builder alertDialogueBuilder = new AlertDialog.Builder(context);
                alertDialogueBuilder.setView(promptsview);

                final EditText new_mail = (EditText)promptsview.findViewById(R.id.new_sos);

                alertDialogueBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //result.setText(new_mail.getText());
                        String hi = new_mail.getText().toString();
                        sosupdate.setText(hi);
                    }
                });

                alertDialogueBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogueBuilder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(!passwordupdate.getText().toString().matches("")) {
            UpdateValues attemptedUpdate = new UpdateValues();
            attemptedUpdate.execute("3", passwordupdate.getText().toString(), "");
        }

        if(!nameupdate.getText().toString().matches("")) {
            //Toast.makeText(getApplicationContext(), "called succesfully",Toast.LENGTH_LONG).show();
            UpdateValues attemptedUpdate = new UpdateValues();
            attemptedUpdate.execute("4", nameupdate.getText().toString(), "");
        }

        if(!mailupdate.getText().toString().matches("")) {
            UpdateValues attemptedUpdate = new UpdateValues();
            attemptedUpdate.execute("5", mailupdate.getText().toString(), "");
        }

        if(!sosupdate.getText().toString().matches("")) {
            UpdateValues attemptedUpdate = new UpdateValues();
            attemptedUpdate.execute("7", sosupdate.getText().toString(), "");
        }

        Intent change_to_settings = new Intent(SettingsScreen.this, HomeScreen.class);
        startActivity(change_to_settings);
        finish();
    }
}