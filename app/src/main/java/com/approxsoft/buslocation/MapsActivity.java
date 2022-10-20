package com.approxsoft.buslocation;

import static com.approxsoft.buslocation.R.drawable.bus_icon;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.approxsoft.buslocation.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap,mMap2;
    private ActivityMapsBinding binding;




    private DatabaseReference reference,reference2;
    private LocationManager manager;

    private final int MIN_TIME = 1000;// 1sec
    private final int MIN_Distance = 1; // 1 meter

    Marker myMarker,myMarker2;

    double lan ;
    double lon ;
    String mapCondition = "null";
    String markerCondition = "1st";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);


        reference = FirebaseDatabase.getInstance().getReference().child("Bus Location");
        //reference2 = FirebaseDatabase.getInstance().getReference().child("User-101");




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getLocationUpdates();

        readChanges();
    }

    private void readChanges() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    try {
                        MyLocation location = snapshot.getValue(MyLocation.class);
                        if (location !=null)
                        {
                            myMarker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));

                        }
                    }catch (Exception e)
                    {
                       // Toast.makeText(MapsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLocationUpdates() {
        if (manager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_Distance, this);
                } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_Distance, this);
                } else {
                    Toast.makeText(this, "No provider enabled", Toast.LENGTH_SHORT).show();
                }
            }else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},101);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getLocationUpdates();
            }else
            {
                Toast.makeText(this,"Permission Request",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    double latitude = Double.parseDouble(Objects.requireNonNull(snapshot.child("latitude").getValue()).toString());
                    double longitude = Double.parseDouble(Objects.requireNonNull(snapshot.child("longitude").getValue()).toString());

                    lan = 23.86898427;
                    lon = 90.3736527778;

                    mMap2 = googleMap;
                    mMap = googleMap;

                    if (mapCondition.equals("1st"))
                    {


                        // Add a marker in Sydney and move the camera
                        LatLng dhaka = new LatLng(latitude,longitude);
                        myMarker = mMap.addMarker(new MarkerOptions().position(dhaka).title("Bus Here")
                                .icon(bitmapDescriptorFromVector(getApplicationContext(), bus_icon)));
                        mMap.setMinZoomPreference(15);
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                        //mMap.getUiSettings().setAllGesturesEnabled(true);
                        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(dhaka));
                        mapCondition = "2nd";
                        myMarker2.remove();

                    }else if (mapCondition.equals("2nd"))
                    {


                        // Add a marker in Sydney and move the camera
                        LatLng dhaka = new LatLng(latitude,longitude);
                        myMarker2 = mMap2.addMarker(new MarkerOptions().position(dhaka).title("Bus Here")
                                .icon(bitmapDescriptorFromVector(getApplicationContext(), bus_icon)));
                        mMap2.setMinZoomPreference(15);
                        mMap2.getUiSettings().setZoomControlsEnabled(true);
                        //mMap.getUiSettings().setAllGesturesEnabled(true);
                        mMap2.getUiSettings().setIndoorLevelPickerEnabled(true);
                        mMap2.moveCamera(CameraUpdateFactory.newLatLng(dhaka));
                        mapCondition = "1st";
                        myMarker.remove();
                    }
                    else if (mapCondition.equals("null"))
                    {
                        // Add a marker in Sydney and move the camera
                        LatLng dhaka = new LatLng(latitude,longitude);
                        myMarker2 = mMap2.addMarker(new MarkerOptions().position(dhaka).title("Bus Here")
                                .icon(bitmapDescriptorFromVector(getApplicationContext(), bus_icon)));
                        mMap2.setMinZoomPreference(15);
                        mMap2.getUiSettings().setZoomControlsEnabled(true);
                        //mMap.getUiSettings().setAllGesturesEnabled(true);
                        mMap2.getUiSettings().setIndoorLevelPickerEnabled(true);
                        mMap2.moveCamera(CameraUpdateFactory.newLatLng(dhaka));
                        mapCondition = "1st";
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        if (location !=null)
        {
            saveLocation(location);
        }else
        {
            Toast.makeText(this,"No Location",Toast.LENGTH_SHORT).show();
        }

    }

    private void saveLocation(Location location) {

        reference.setValue(location);
    }


     @Override
     public void onLocationChanged(@NonNull List<Location> locations) {

         Toast.makeText(this,"LocationChanged",Toast.LENGTH_SHORT).show();

     }



    @Override
     public void onFlushComplete(int requestCode) {

        Toast.makeText(this,"FlushComplete",Toast.LENGTH_SHORT).show();
     }

     @Override
     public void onStatusChanged(String provider, int status, Bundle extras) {

         Toast.makeText(this,"StatusChanged",Toast.LENGTH_SHORT).show();
     }

     @Override
     public void onProviderEnabled(@NonNull String provider) {

         Toast.makeText(this,"onProviderEnabled",Toast.LENGTH_SHORT).show();
     }

     @Override
     public void onProviderDisabled(@NonNull String provider) {
         Toast.makeText(this,"onProviderDisabled",Toast.LENGTH_SHORT).show();


     }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResID) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResID);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);


    }
}