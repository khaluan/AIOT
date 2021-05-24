package com.example.aiot.ui.browse;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aiot.R;
import com.example.aiot.ui.notifications.NotificationsViewModel;

import java.io.File;
import java.util.List;

public class BrowseFragment extends Fragment {

    private BrowseViewModel browseViewModel;
    private RecyclerView recyclerView;
    private GalleryAdapter galleryAdapter;
    static int READ_EXTERNAL_REQUEST_CODE = 101;
    List<String> images;
    TextView galleryNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        browseViewModel =
                new ViewModelProvider(this).get(BrowseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_browse, container, false);
        // galleryNumber = root.findViewById(R.id.gallery_number);
        recyclerView = root.findViewById(R.id.recyclerview_images);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_REQUEST_CODE) ;
        }
        else {
            loadImages();
        }
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                loadImages();
            }
        }
    }

    private void loadImages() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        images = browseViewModel.listOfImages(getContext());
        galleryAdapter = new GalleryAdapter(getContext(), images, new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                //TODO: Do something here
                Log.d("URI", path);
                browseViewModel.uploadPhoto(path);
            }
        });
        recyclerView.setAdapter(galleryAdapter);
        // galleryNumber.setText("Photo (" + images.size()+")");

    }
}