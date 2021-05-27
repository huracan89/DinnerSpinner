package com.example.vpfed_000.dinnerspinner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    Button saveButton;
    RadioButton distanceButton;
    RadioButton ratingButton;
    RatingBar ratingBar;
    NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        saveButton=findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Save Settings
                Context context = getBaseContext();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putFloat("userStarRating", ratingBar.getRating()).apply();
                editor.putInt("maxPrice", numberPicker.getValue()).apply();
                if(distanceButton.isChecked()) {
                    System.out.println("Went here");
                    editor.putString("sortByValue", "distance").apply();
                }
                else if(ratingButton.isChecked())
                {
                    editor.putString("sortByValue", "prominence").apply();
                }

                // Toast.makeText(getApplicationContext(), String.valueOf(ratingBar.getRating() + " " + sharedPref.getString("sortByValue", "NotSet")), Toast.LENGTH_LONG).show();
                //Return to Home activity
                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        ratingBar=findViewById(R.id.ratingBar);

        distanceButton=findViewById(R.id.radioButtonDistance);
        distanceButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Not sure if needed
            }
        });
        ratingButton=findViewById(R.id.radioButtonRating);
        ratingButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Not sure if needed
            }
        });
        numberPicker=findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(4);
        numberPicker.setMinValue(1);

    }

}
