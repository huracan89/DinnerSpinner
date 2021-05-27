package com.example.vpfed_000.dinnerspinner;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;


public class HomeActivity extends AppCompatActivity {

    public static Context contextOfApplication;

    Button classicButton, hardcoreButton, aboutButton, settingsButton, initializeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        contextOfApplication = HomeActivity.this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getApplicationContext(), "Please enable location permissions in your device settings.", Toast.LENGTH_LONG).show();
            return;
        }

        initializeButton=findViewById(R.id.initialize);
        initializeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=Success: press back.");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        classicButton=findViewById(R.id.classic);
        classicButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            //Add Link
                //Example for getting shared prefs
               Context context = getBaseContext();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("gamemode", "classic").apply();
               Intent intent = new Intent(getBaseContext(), SpinnerActivity.class);
               startActivity(intent);
            }
        });

        hardcoreButton=findViewById(R.id.hardcore);
        hardcoreButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Add Link
                Context context = getBaseContext();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("gamemode", "hardcore").apply();
                Intent intent = new Intent(getBaseContext(), SpinnerActivity.class);
                startActivity(intent);
            }
        });

        aboutButton=findViewById(R.id.about);
        aboutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Add Link
                Intent intent = new Intent(getBaseContext(), AboutActivity.class);
                startActivity(intent);
            }
        });

        settingsButton=findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Add Link
            Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(intent);
            }
        });

    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

}
