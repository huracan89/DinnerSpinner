package com.example.vpfed_000.dinnerspinner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RestaurantGalleryActivity extends AppCompatActivity {

    private static final String TAG = "ResGalAct";

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        Bundle b = getIntent().getExtras();
        List<GalleryItem> items = b.getParcelableArrayList("items");
        Log.i(TAG, "test worked: " + items);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = RestaurantGalleryFragment.newInstance();

            Bundle arguments = new Bundle();
            arguments.putParcelableArrayList("items", (ArrayList) items);
            fragment.setArguments(arguments);

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getBaseContext(), HomeActivity.class);
        startActivity(intent);
        // Toast.makeText(getApplicationContext(), "This is a message displayed in a Toast", Toast.LENGTH_SHORT).show();

    }

}
