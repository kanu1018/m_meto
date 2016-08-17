package com.kitri.meto.m.metour;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-08-16.
 */
public class SelectLocation extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
    static final LatLng SEOUL = new LatLng(37.541, 126.986);
    private GoogleMap googleMap;
    private EditText latitude, longitude;
    private Button select_ok, search_ok;
    private Marker select;
    private AutoCompleteTextView search;

    //검색 API
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private ArrayList<AutocompletePrediction> list;
    private static final LatLngBounds BOUNDS_GREATER_SEOUL = new LatLngBounds(
            new LatLng(37.417123, 126.808246), new LatLng(37.709950, 127.161182));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        setContentView(R.layout.select_location);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        latitude = (EditText)findViewById(R.id.map_latitude);
        longitude = (EditText)findViewById(R.id.map_longitude);
        search = (AutoCompleteTextView)findViewById(R.id.search_place);
        search.setOnItemClickListener(mAutocompleteClickListener);

        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SEOUL, null);
        search.setAdapter(mAdapter);

        select_ok = (Button) findViewById(R.id.map_select_ok);
        select_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("latitude",Double.parseDouble(latitude.getText().toString()));
                intent.putExtra("longitude",Double.parseDouble(longitude.getText().toString()));
                intent.putExtra("place", search.getText().toString()); //// TODO: 2016-08-16
                setResult(200, intent);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        select = googleMap.addMarker(new MarkerOptions().position(SEOUL)
                .title("Seoul"));
        select.showInfoWindow();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom( SEOUL, 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 500, null);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                select.remove();

                latitude.setText(""+latLng.latitude);
                longitude.setText(""+latLng.longitude);

                LatLng newLoca = new LatLng(latLng.latitude, latLng.longitude);
                select = googleMap.addMarker(new MarkerOptions().position(newLoca).title("선택"));
                select.showInfoWindow();

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom( newLoca, 16));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 700, null);

            }
        });

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        googleMap.setMyLocationEnabled(true);


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("onConnectionFailed", "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i("item selected", "Autocomplete item selected: " + primaryText);

            Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId).setResultCallback(new ResultCallback<PlaceBuffer>() {
                public void onResult(PlaceBuffer places) {

                    if (places.getStatus().isSuccess()) {

                        final Place myPlace = places.get(0);
                        LatLng select_latlng = myPlace.getLatLng();
                        Log.i("place find", "place find" + select_latlng.latitude + "/" + select_latlng.longitude);
                        latitude.setText(""+select_latlng.latitude);
                        longitude.setText(""+select_latlng.longitude);

                        select.remove();
                        select = googleMap.addMarker(new MarkerOptions().position(select_latlng).title(myPlace.getName().toString()));
                        select.showInfoWindow();

                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom( select_latlng, 16));
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 700, null);

                    }
                    places.release();

                }

            });

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
           // placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Log.i("Called getPlaceById", "Called getPlaceById to get Place details for " + placeId);
        }
    };

}
