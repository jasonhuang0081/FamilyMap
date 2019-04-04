package com.example.familymap;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

import model.Event;
import model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap map;
    int colorFactor = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Add a marker in Sydney and move the camera
        addMarker();

        LatLng sydney = new LatLng(-34, 151);
        map.animateCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void addEventLine()
    {

    }
    private void addMarker()
    {
        int numEvent = Data.getInstance().getShownEvent().size();
        for (int i = 0; i < numEvent; i++)
        {
            Event curEvent = Data.getInstance().getShownEvent().get(i);
            boolean setNewColor = true;
            double lat = curEvent.getLatitude();
            double lon = curEvent.getLongitude();
            float hue = 0;
            String eventType = curEvent.getEventType();
            eventType = eventType.toLowerCase();
            for (Map.Entry<String, Integer> entry : Data.getInstance().getMarkerColor().entrySet())
            {
                String key = entry.getKey();
                Integer value = entry.getValue();
                if (key.equals(eventType))
                {
                    hue = (float) value;
                    setNewColor = false;
                    break;
                }
                else
                {
                    setNewColor = true;
                }
            }
            if (setNewColor)
            {
                int newColor = getNewColor();
                hue = (float) newColor;
                Data.getInstance().getMarkerColor().put(eventType,(Integer)newColor);
            }
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lon))
                    .title(Data.getInstance().getEventList().get(i).getCity())
                    .icon(BitmapDescriptorFactory.defaultMarker(hue)));

        }
    }
    private int getNewColor()
    {
        int color;
        if (colorFactor > 12)
        {
            color = 30*(colorFactor-13) + 15;
        }
        else
        {
            color = colorFactor*30;

        }
        colorFactor++;
        if (color > 360)
        {
            color = 350;
        }
        return color;
    }
}
