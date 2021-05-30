package com.example.aiot.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aiot.R;
import com.example.aiot.ViewPagerAdapter;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
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
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    onMarkerClickCallback(marker);
                    return true;
                }
            });
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
                    LatLng locationLatLng;
                    if (location == null)
                        locationLatLng = new LatLng(10.762913, 106.6821717);
                    else
                        locationLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationLatLng, 16);
                    map.animateCamera(cameraUpdate);
                    mapViewModel.requestForEvents();
                    updateEventOnMap();
                }
            });
        }
    };

    private void onMarkerClickCallback(Marker marker) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(
                R.layout.bottom_info_sheet,
                (LinearLayout)getActivity().findViewById(R.id.bottom_sheet_container)
        );
        setupViewPager(bottomSheetView, marker);

        TextView eventName = bottomSheetView.findViewById(R.id.event_name);
        TextView updateTime = bottomSheetDialog.findViewById(R.id.event_update_time);
        eventName.setText(marker.getTitle());
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void setupViewPager(View view, Marker marker) {
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(), (ArrayList<String>) marker.getTag());
        viewPager.setAdapter(viewPagerAdapter);
    }

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

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void updateEventOnMap() {
        Integer[] color = new Integer[]{-1, 0, 30, 60, 90, 180, 240, 300};
        Integer[] icons = new Integer[]{0, R.drawable.ic_treefall, R.drawable.ic_fire, R.drawable.ic_flood, R.drawable.ic_duong_xau, R.drawable.ic_traffic_jam, R.drawable.ic_garbage, R.drawable.ic_accident};
        String[] title = this.getResources().getStringArray(R.array.event_name);
        ArrayList<Event> events = mapViewModel.eventList;
        if (events == null)
            return;
        Log.d("DRAWING", events.toString());
        ArrayList<String> tags = new ArrayList<>();
        tags.add("https://i.pinimg.com/originals/05/96/7b/05967ba7a49130269bf8c23a3b5e253c.jpg");
        tags.add("https://pbs.twimg.com/media/CEO6QnFVAAE5ZCh.jpg");
        tags.add("https://metro.co.uk/wp-content/uploads/2013/01/ay_102510013.jpg?quality=90&strip=all&zoom=1&resize=480%2C320");
        for (Event event : events){
            LatLng location = new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongtitude());
            Log.d("DRAWING",String.format("Lat: %s, Lng: %s, Type: %d", location.latitude, location.longitude, event.getType()));
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(location)
                    .icon(bitmapDescriptorFromVector(getContext(), icons[event.getType()]))
                    .title(title[event.getType()]));
            marker.setTag(tags);
        }
    }

}