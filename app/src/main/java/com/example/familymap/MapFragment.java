package com.example.familymap;

import android.content.Intent;
import android.graphics.Color;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
//        List<Event> personalEvents = Data.getInstance().getPersonalEvents(Data.getInstance().getCurrentPerson().getPersonID());
//        if (personalEvents.size() != 0)
//        {
//            currentEvent = personalEvents.get(0);
//            Data.getInstance().setCurrentEvent(currentEvent);
//        }
        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /// check if currentEvent is null, then enter person activity when click on infoBox
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

    public void setMapType()
    {
        int mapTypeInt = 1;
        if (Data.getInstance().getMapType().equals("normal"))
        {
            mapTypeInt = 1;
        }
        else if (Data.getInstance().getMapType().equals("hybrid"))
        {
            mapTypeInt = 2;
        }
        else if (Data.getInstance().getMapType().equals("satellite"))
        {
            mapTypeInt = 3;
        }
        else if (Data.getInstance().getMapType().equals("terrain"))
        {
            mapTypeInt = 4;
        }
        map.setMapType(mapTypeInt);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setMapType();
        addMarker();

        if (Data.getInstance().getCurrentEvent() != null)
        {
            currentEvent = Data.getInstance().getCurrentEvent();
            map.animateCamera(CameraUpdateFactory.newLatLng
                    (new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude())));
            fillInfoBox();
            drawLines();
        }
        else
        {
            // default position
            map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(-34, 151)));
        }


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                currentEvent = (Event) marker.getTag();
                fillInfoBox();
                drawLines();
                return true;
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if (currentEvent != null)
        {
            Data.getInstance().filter();
            drawLines();
            setMapType();
        }

    }
    private void fillInfoBox()
    {
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
    }
    public void drawLines()
    {
        map.clear();
        addMarker();
        if (currentEvent != null)
        {
            // draw all three lines
            if (Data.getInstance().isSpouseLine())
            {
                addSpouseLine();
            }
            if (Data.getInstance().isLifeLine())
            {
                addLifeStoryLine();
            }
            if (Data.getInstance().isFamilyTreeLine())
            {
                addFamilyTreeLine(currentEvent.getPersonID(),currentEvent,8);
            }
        }
    }
    private void addSpouseLine()
    {
        String personID = currentEvent.getPersonID();
        Person curPerson = Data.getInstance().getPersonByID(personID);
        String spouseID = curPerson.getSpouse();
        List<Event> spouseEvents = Data.getInstance().getPersonalEvents(spouseID);
        if (spouseEvents.size() != 0)
        {
            Event spouseEvent = spouseEvents.get(0);
            String chosenColor = Data.getInstance().getSpouseLineColor();
            Polyline line = map.addPolyline(new PolylineOptions()
                    .add(new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude())
                            , new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude()))
                    .width(5)
                    .color(convertColor(chosenColor)));
        }

    }
    private void addLifeStoryLine()
    {
        String personID = currentEvent.getPersonID();
        List<Event> personalEvents = Data.getInstance().getPersonalEvents(personID);
        String chosenColor = Data.getInstance().getLifeLineColor();
        for (Event eachEvent: personalEvents)
        {
            Polyline line = map.addPolyline(new PolylineOptions()
                    .add(new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude())
                            , new LatLng(eachEvent.getLatitude(), eachEvent.getLongitude()))
                    .width(5)
                    .color(convertColor(chosenColor)));
        }
    }
    private void addFamilyTreeLine(String personID, Event PersonEvent, double lineWidth)
    {
        Set<Person> parents = Data.getInstance().getPersonIDtoParents().get(personID);
        String chosenColor = Data.getInstance().getFamilyLineColor();
        for (Person parent: parents)
        {
            List<Event> parentOneEvents = Data.getInstance().getPersonalEvents(parent.getPersonID());
            if (parentOneEvents.size() != 0)
            {
                Polyline line = map.addPolyline(new PolylineOptions()
                        .add(new LatLng(parentOneEvents.get(0).getLatitude(), parentOneEvents.get(0).getLongitude())
                                , new LatLng(PersonEvent.getLatitude(), PersonEvent.getLongitude()))
                        .width((float)lineWidth)
                        .color(convertColor(chosenColor)));
                addFamilyTreeLine(parent.getPersonID(),parentOneEvents.get(0),lineWidth*0.5);
            }

        }

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
                    .title(Data.getInstance().getShownEvent().get(i).getCity())
                    .icon(BitmapDescriptorFactory.defaultMarker(hue)));
            marker.setTag(Data.getInstance().getShownEvent().get(i));
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
    private int convertColor(String color)
    {
        if (color.equals("blue"))
        {
            return -16776961;
        }
        else if (color.equals("red"))
        {
            return-65536;
        }
        else if (color.equals("magenta"))
        {
            return-65281;
        }
        else if (color.equals("green"))
        {
            return -16711936;
        }
        else if (color.equals("yellow"))
        {
            return-256;
        }
        else if (color.equals("black"))
        {
            return-16777216;
        }
        else
        {
            return 0;
        }
    }
}
