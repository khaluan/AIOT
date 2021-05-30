package com.example.aiot.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aiot.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;

public class MapsFragment extends Fragment {
    private GoogleMap map;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private MapViewModel mapViewModel;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    LatLng locationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (locationLatLng == null){
                        locationLatLng = new LatLng(10.762913, 106.6821717);
                    }
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationLatLng, 16);
                    map.animateCamera(cameraUpdate);
                    mapViewModel.requestForEvents();
                    updateEventOnMap();
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        final Observer<List<Event>> eventsObserver = new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                updateEventOnMap();
            }
        };
        mapViewModel.getLiveEventList().observe(getViewLifecycleOwner(), eventsObserver);
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }

    private void updateEventOnMap() {
        Integer[] color = new Integer[]{-1, 0, 30, 60, 90, 180, 240, 300};
        String[] title = this.getResources().getStringArray(R.array.event_name);
        ArrayList<Event> events = mapViewModel.eventList;
        Log.d("DRAWING", events.toString());
        for (Event event : events){
            LatLng location = new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongtitude());
            Log.d("DRAWING",String.format("Lat: %s, Lng: %s, Type: %d", location.latitude, location.longitude, event.getType()));
            map.addMarker(new MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.defaultMarker(color[event.getType()]))
                    .snippet("Something here")
                    .title(title[event.getType()]));
        }
    }

}