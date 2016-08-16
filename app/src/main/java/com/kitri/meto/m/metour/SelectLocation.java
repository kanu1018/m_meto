package com.kitri.meto.m.metour;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Administrator on 2016-08-16.
 */
public class SelectLocation extends Activity implements OnMapReadyCallback{
    static final LatLng SEOUL = new LatLng(37.541, 126.986);
    private GoogleMap googleMap;
    private EditText latitude, longitude, search;
    private Button select_ok;
    private Marker select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_location);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        latitude = (EditText)findViewById(R.id.map_latitude);
        longitude = (EditText)findViewById(R.id.map_longitude);
        search = (EditText)findViewById(R.id.search_place);

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
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                select.remove();

                latitude.setText(""+latLng.latitude);
                longitude.setText(""+latLng.longitude);

                LatLng newLoca = new LatLng(latLng.latitude, latLng.longitude);
                select = googleMap.addMarker(new MarkerOptions().position(newLoca).title("선택"));
                select.showInfoWindow();

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

}
