package com.example.a6locationinfoapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView latitudeTextView , longitudeTextView ,
            accuracyTextView , altitudeTextView ,
            postalCodeTextView , mainAreaTextView , countryTextView;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    private Geocoder mGeoCoder;

    private Double last_latitude;
    private Double last_longitude;
    private Float Accuracy;
    private Double Altitude;

    private String postal_code;

    private String MainArea;
    private String countryName;

    private void initUI(){
        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        accuracyTextView = findViewById(R.id.accuracyTextView);
        altitudeTextView = findViewById(R.id.altitudeTextView);
        postalCodeTextView = findViewById(R.id.PostalTextView);
        mainAreaTextView = findViewById(R.id.mainAreaTextView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,mLocationListener);
            }
        }
    }

    private void init_location() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mGeoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        init_location();

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.e("LOG * * * * " , location.getLatitude() + " , "+ location.getLongitude());
                last_latitude = location.getLatitude();
                last_longitude = location.getLongitude();
                Accuracy = location.getAccuracy();
                Altitude = location.getAltitude();

                latitudeTextView.setText(String.valueOf(last_latitude));
                longitudeTextView.setText(String.valueOf(last_longitude));
                accuracyTextView.setText(String.valueOf(Accuracy));
                altitudeTextView.setText(String.valueOf(Altitude));

                try {
                    List<android.location.Address> addressList = mGeoCoder.getFromLocation(last_latitude,last_longitude,1);

                    if (mGeoCoder.isPresent() && addressList != null && addressList.size() > 0){
                        if (addressList.get(0).getPostalCode() != null)
                            postal_code = addressList.get(0).getPostalCode();
                        if (addressList.get(0).getSubAdminArea() != null)
                            MainArea = addressList.get(0).getSubAdminArea();
                        if (addressList.get(0).getCountryName() != null)
                            countryName = addressList.get(0).getCountryName();

                        postalCodeTextView.setText(postal_code);
                        mainAreaTextView.setText(MainArea);
                        countryTextView.setText(countryName);
                     }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("GeoCoder Excaption","here!");
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.ACCESS_FINE_LOCATION} , 1);
        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,mLocationListener);
        }
    }
}
