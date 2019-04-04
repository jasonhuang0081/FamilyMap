package com.example.familymap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import model.Event;
import model.Person;

public class Data {
    private static Data single_instance;
    private List<Person> personList;
    private List<Event>  eventList = new ArrayList<>();
    private List<Event> shownEvent;
    private Map<String, Integer> markerColor = new TreeMap<>();
    private Map<String, Boolean> eventFilter = new TreeMap<>();
    private boolean maleEvent = true;
    private boolean femaleEvent = true;
    private boolean motherSideEvent = true;
    private boolean fatherSiceEvent = true;
    public static Data getInstance()
    {
        if (single_instance == null)
        {
            single_instance = new Data();
        }
        return single_instance;
    }

    private void filter()
    {
        try
        {
            for (Event each: eventList)
            {
                if ((boolean) eventFilter.get(each.getEventType()))
                {
                    shownEvent.add(each);
                }
            }
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public List<Event> getShownEvent() {
        return shownEvent;
    }

    public void setPersonList(List<Person> personList) {
        List<Person> people = this.personList = personList;
    }

    public List<Event> getEventList() {

        return eventList;
    }

    public void setEventList(List<Event> eventList) {

        this.eventList = eventList;
        for (Event each: eventList)
        {
            if (!eventFilter.containsKey(each.getEventType()))
            {
                eventFilter.put(each.getEventType(),true);
            }
        }
    }

    public Map<String, Integer> getMarkerColor() {
        return markerColor;
    }

    public void setMarkerColor(Map<String, Integer> markerColor) {
        this.markerColor = markerColor;
    }
}
