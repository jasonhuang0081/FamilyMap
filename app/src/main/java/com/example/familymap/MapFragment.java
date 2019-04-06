package com.example.familymap;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback  {
    private GoogleMap map;
    private Event currentEvent = null;
    private TextView nameDisplay;
    private TextView eventDisplay;
    private TextView yearDisplay;
    private ImageView image;
    private View clickView;
    private int colorFactor = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        nameDisplay = view.findViewById(R.id.eventPersonName);
        eventDisplay = view.findViewById(R.id.eventNCity);
        yearDisplay = view.findViewById(R.id.eventCountryNtime);
        image = view.findViewById(R.id.image);
        clickView = view.findViewById(R.id.clickView);
        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /// check if currentEvent is null, then enter person activity
                if (currentEvent != null)
                {
                    String personID = currentEvent.getPersonID();
                    Data.getInstance().setCurrentPerson(Data.getInstance().getPersonByID(personID));
                    Intent intent = new Intent(getActivity(), PersonActivity.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Add a marker in Sydney and move the camera
        addMarker();

        LatLng sydney = new LatLng(-34, 151);
        map.animateCamera(CameraUpdateFactory.newLatLng(sydney));
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                currentEvent = (Event) marker.getTag();
                List<Person> personList = Data.getInstance().getPersonList();
                String name = "";
                String gender = "m";
                for (Person each: personList)
                {
                    if (each.getPersonID().equals(currentEvent.getPersonID()))
                    {
                        name = each.getFirstName() + " " + each.getLastName();
                        gender = each.getGender();
                        break;
                    }
                }
                String eventDes = currentEvent.getEventType() + ": " + currentEvent.getCity();
                String eventYear = currentEvent.getCountry() + " (" + Integer.toString(currentEvent.getYear()) + ")";

                if (gender.equals("m"))
                {
                    Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                            colorRes(R.color.male_icon).sizeDp(40);
                    image.setImageDrawable(genderIcon);
                }
                else
                {
                    Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                            colorRes(R.color.female_icon).sizeDp(40);
                    image.setImageDrawable(genderIcon);
                }
                nameDisplay.setText(name);
                eventDisplay.setText(eventDes);
                yearDisplay.setText(eventYear);
                return true;
            }
        });
    }

    private void addSpouseLine()
    {
//        Polyline line = map.addPolyline(new PolylineOptions()
//                .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
//                .width(5)
//                .color(Color.RED));
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
                Data.getInstance().getMarkerColor().put(eventType, newColor);
            }
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lon))
                    .title(Data.getInstance().getEventList().get(i).getCity())
                    .icon(BitmapDescriptorFactory.defaultMarker(hue)));
            marker.setTag(Data.getInstance().getEventList().get(i));
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
