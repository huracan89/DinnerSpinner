package com.example.vpfed_000.dinnerspinner;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QueryPlaces {

    private static final String TAG = "QueryPlaces";

    private static final String API_KEY = "AIzaSyC1Je8WSZL-cIiZIMaB6FlwRcoZwz0joZE";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> fetchItems(String param1, String param2) {

        List<GalleryItem> items = new ArrayList<>();

        String latitude, longitude;
        latitude = param1;
        longitude = param2;
        Log.i(TAG, "Received latitude: " + latitude);
        Log.i(TAG, "Received longitude: " + longitude);

        try {

            Context applicationContext = HomeActivity.getContextOfApplication();
            SharedPreferences prefs = applicationContext.getSharedPreferences(
                    "com.dinnerSpinner.fileKey", Context.MODE_PRIVATE);

            String sortPref = prefs.getString("sortByValue", "distance");

            String url;
            if (sortPref.equals("distance")) {
                 url = Uri.parse("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                        .buildUpon()
                        .appendQueryParameter("location", latitude + "," + longitude)
                        // .appendQueryParameter("radius", "40000")
                        .appendQueryParameter("rankby", sortPref )
                        .appendQueryParameter("type", "restaurant")
                        .appendQueryParameter("keyword", "")
                        .appendQueryParameter("key", API_KEY)
                        .build().toString();
            }
            else {
                url = Uri.parse("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                        .buildUpon()
                        .appendQueryParameter("location", latitude + "," + longitude)
                        .appendQueryParameter("radius", "40000")
                        .appendQueryParameter("rankby", sortPref )
                        .appendQueryParameter("type", "restaurant")
                        .appendQueryParameter("keyword", "")
                        .appendQueryParameter("key", API_KEY)
                        .build().toString();
            }

            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return items;
    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonBody)
            throws IOException, JSONException {

        JSONArray photosJsonArray = jsonBody.getJSONArray("results");

        Context applicationContext = HomeActivity.getContextOfApplication();
        SharedPreferences prefs = applicationContext.getSharedPreferences(
                "com.dinnerSpinner.fileKey", Context.MODE_PRIVATE);

        String sortPref = prefs.getString("sortByValue", "distance");
        float minRating = prefs.getFloat("userStarRating", 0);
        String mode = prefs.getString("gamemode", "classic");
        int maxPrice = prefs.getInt("maxPrice", 4);

        Log.i(TAG, "sortPref: " + sortPref);
        Log.i(TAG, "min rating: " + minRating);
        Log.i(TAG, "game mode: " + mode);
        Log.i(TAG, "maxPrice: " + maxPrice);

        Log.i(TAG, "photosJsonArray: " + photosJsonArray);
        Log.i(TAG, "photosJsonArray.length(): " + photosJsonArray.length());

        Random r = new Random();
        int random = r.nextInt(photosJsonArray.length());

        if (mode.equals("hardcore")) {
            for (int i = 0; i < photosJsonArray.length(); i++) {
                if (i == random) {
                    JSONObject photoJsonObject = photosJsonArray.getJSONObject(i);
                    GalleryItem item = new GalleryItem();
                    item.setId(photoJsonObject.getString("id"));
                    item.setCaption(photoJsonObject.getString("name"));
                    if (photoJsonObject.has("rating")) {
                        item.setRating(photoJsonObject.getString("rating"));
                    }
                    else {
                        item.setRating("Not available");
                    }
                    if (photoJsonObject.has("price_level")) {
                        item.setPrice(photoJsonObject.getString("price_level"));
                    }
                    else {
                        item.setPrice("Not available");
                    }
                    if (photoJsonObject.has("vicinity")) {
                        item.setAddress(photoJsonObject.getString("vicinity"));
                    }
                    else {
                        item.setAddress("Not available");
                    }
                    items.add(item);
                }
                else {
                    continue;
                }
            }
        }
        else {
            for (int i = 0; i < photosJsonArray.length(); i++) {
                JSONObject photoJsonObject = photosJsonArray.getJSONObject(i);

                Log.i(TAG, "JSON Object " + i + ": " + photoJsonObject);

                GalleryItem item = new GalleryItem();
                item.setId(photoJsonObject.getString("id"));
                item.setCaption(photoJsonObject.getString("name"));

                if (!photoJsonObject.has("rating")) {
                    continue;
                }
                else {
                    if (Float.parseFloat(photoJsonObject.getString("rating")) > minRating) {
                        item.setRating(photoJsonObject.getString("rating"));
                    }
                    else {
                        continue;
                    }
                }

                if (!photoJsonObject.has("price_level")) {
                    continue;
                }
                else {
                    if (Integer.parseInt(photoJsonObject.getString("price_level")) <= maxPrice) {
                        item.setPrice(photoJsonObject.getString("price_level"));
                    }
                    else {
                        continue;
                    }
                }

                if (photoJsonObject.has("vicinity")) {
                    item.setAddress(photoJsonObject.getString("vicinity"));
                }


                if (!photoJsonObject.has("icon")) {
                    continue;
                }
                item.setUrl(photoJsonObject.getString("icon"));

                Log.i(TAG, "item number " + i + ": " + item);
                // Log.i(TAG, "item number " + i + ": " + item.getId());

                items.add(item);

            }
        }

    }

}
