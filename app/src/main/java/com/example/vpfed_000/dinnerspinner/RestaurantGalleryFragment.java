package com.example.vpfed_000.dinnerspinner;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.widget.GridLayout.HORIZONTAL;

public class RestaurantGalleryFragment extends Fragment {

    private static final String TAG = "ResGalFragment";

    private RecyclerView mPhotoRecyclerView;

    private List<GalleryItem> mItems = new ArrayList<>();

    public static RestaurantGalleryFragment newInstance() {
        return new RestaurantGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mItems = bundle.getParcelableArrayList("items");
            // String longitude = bundle.getString("longitude", "default");
            // String[] myTaskParams = { latitude, longitude };
            // new FetchItemsTask().execute(myTaskParams);
            Log.i(TAG, "Array of items: " + mItems);
            // Log.i(TAG, "ID for item 2: " + mItems.get(2).getId());
            // mItems = items;
            // setupAdapter();
        }
        // new FetchItemsTask().execute(latitude, longitude);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.photo_recycler_view);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        mPhotoRecyclerView.addItemDecoration(itemDecoration);

        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        setupAdapter();

        return v;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;

        public PhotoHolder(final View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView;

        }

        public void bindGalleryItem(final GalleryItem item) {
            String sourceString = "<strong><big>Name: " + item.toString() + "</big></strong><br>" +
                                  "<big>Rating: " + item.getRating() + "</big><br>" +
                                  "<big>Price Level: " + item.getPrice() + "</big><br>" +
                                  "<big>Address: " + item.getAddress() + "</big>";
            mTitleTextView.setText(Html.fromHtml(sourceString,Html.FROM_HTML_MODE_LEGACY));
            // Log.i(TAG, "testing address: " + item.getAddress());

            mTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(getContext(), "This is a message displayed in a Toast", Toast.LENGTH_SHORT).show();
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + item.toString());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });

        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            TextView textView = new TextView(getActivity());
            return new PhotoHolder(textView);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);
            photoHolder.bindGalleryItem(galleryItem);
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }

}
