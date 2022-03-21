package com.example.giveandtake.ui.map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.giveandtake.R;
import com.example.giveandtake.model.Post;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapFragment extends Fragment {

    MapViewModel mapViewModel;
    private View view;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onMapReady(GoogleMap googleMap) {
            List<Post> posts = mapViewModel.getPosts();
            for (Post post: posts){
                String locationStr = post.getLocation();
                LatLng location = getLocationFromAddress(locationStr);
                if(location == null) continue;
                Marker marker = googleMap.addMarker(new MarkerOptions().position(location).title("Marker in Haifa"));
                marker.setTag(post.getId());
                googleMap.setOnMarkerClickListener(marker1 -> {
                    String postId = marker1.getTag().toString();
                    Navigation.findNavController(view).navigate(MapFragmentDirections.actionNavMapToPostDetailsFragment(postId));
                    return true;
                });
            }
            LatLng israel = getLocationFromAddress("Israel");
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(israel,8));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        LatLng p1;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null || address.size() == 0) {
                return null;
            }
            Address location = address.get(0);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            p1 = new LatLng(latitude, longitude);

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}