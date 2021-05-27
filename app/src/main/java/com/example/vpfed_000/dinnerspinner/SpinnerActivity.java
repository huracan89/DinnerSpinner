package com.example.vpfed_000.dinnerspinner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

public class SpinnerActivity extends AppCompatActivity {

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.spinner_activity_fragment;
    }

    private List<GalleryItem> mItems = new ArrayList<>();

    private static final String TAG = "SpinAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // Toast.makeText(getApplicationContext(), "Please enable location permission in device settings.", Toast.LENGTH_LONG).show();
            return;
        }
        // Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final Location location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if (location == null) {
            Toast.makeText(getApplicationContext(), "Please initialize your location first on the home screen.", Toast.LENGTH_LONG).show();
        }

        else {
            final String longitude = String.valueOf(location.getLongitude());
            final String latitude = String.valueOf(location.getLatitude());

            final ImageView iView = (ImageView) findViewById(R.id.loading);
            Glide.with(this).asGif().load(R.drawable.nugget).into(iView);
            iView.setVisibility(View.INVISIBLE);

            final ImageView iView2 = (ImageView) findViewById(R.id.loadingText);
            Glide.with(this).asGif().load(R.drawable.loading).into(iView2);
            // iView2.bringToFront();
            iView2.setVisibility(View.INVISIBLE);

            final Button button1 = (Button) findViewById(R.id.button1);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    button1.setVisibility(View.INVISIBLE);
                    iView.setVisibility(View.VISIBLE);
                    iView2.setVisibility(View.VISIBLE);

                    class FetchItemsTask extends AsyncTask<String,Void,List<GalleryItem>> {
                        @Override
                        protected List<GalleryItem> doInBackground(String... params) {
                            String param1, param2;
                            param1 = params[0];
                            param2 = params[1];
                            return new QueryPlaces().fetchItems(param1, param2);
                        }

                        @Override
                        protected void onPostExecute(List<GalleryItem> items) {
                            Log.i(TAG, "onQueryComplete: " + items);
                            mItems = items;
                            Intent intent = new Intent(view.getContext(), RestaurantGalleryActivity.class);
                            intent.putParcelableArrayListExtra("items", (ArrayList) mItems);
                            view.getContext().startActivity(intent);
                            // iView.setVisibility(View.INVISIBLE);
                        }
                    }
                    String[] myTaskParams = { latitude, longitude };
                    new FetchItemsTask().execute(myTaskParams);
                }
            });
        }

    }

}
