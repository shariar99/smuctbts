package com.approxsoft.buslocation;

import static com.approxsoft.buslocation.R.drawable.bus_icon;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import com.approxsoft.buslocation.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.approxsoft.buslocation.databinding.ActivityMapShowBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapShowActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap,mMap2;
    private ActivityMapShowBinding binding;

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

        reference = FirebaseDatabase.getInstance().getReference().child("Bus Location");

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists())
                        {
                            double latitude = Double.parseDouble(Objects.requireNonNull(snapshot.child("latitude").getValue()).toString());
                            double longitude = Double.parseDouble(Objects.requireNonNull(snapshot.child("longitude").getValue()).toString());

                            lan = latitude;
                            lon = longitude;

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
                                        .icon(bitmapDescriptorFromVector(getApplicationContext(),bus_icon)));
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
        },5000);

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResID) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResID);
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);


    }




}