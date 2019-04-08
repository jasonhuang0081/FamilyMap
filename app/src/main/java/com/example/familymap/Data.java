package com.example.familymap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import model.Event;
import model.Person;

public class Data {
    private static Data single_instance;
    private String hostString;
    private String portString;
    private String userString;
    private String passwordString;
    private Person currentPerson;
    private Event currentEvent;
    private List<Person> personList = new ArrayList<>();
    private List<Event>  eventList = new ArrayList<>();
    private List<Event> shownEvent = new ArrayList<>();
    private Map<String, Integer> markerColor = new TreeMap<>();
    private Map<String, Boolean> eventFilter = new TreeMap<>();
    private Map<String,String> eventIDToGender = new TreeMap<>();
    private Map<String, String> eventIDToBelongerID = new TreeMap<>();
    private Map<String, Set<Person>> personIDtoParents = new TreeMap<>();
    private Map<String, Person> parentIDtoChildren = new TreeMap<>();
    private List<Event> motherSideEvent = new ArrayList<>();
    private List<Event> fatherSideEvent = new ArrayList<>();
    private boolean ismaleEvent = true;
    private boolean isfemaleEvent= true;
    private boolean ismotherSideEvent = true;
    private boolean isfatherSiceEvent = true;
    private boolean isSpouseLine = true;
    private boolean isLifeLine = true;
    private boolean isFamilyTreeLine = true;
    private String mapType = "normal";
    private String spouseLineColor = "blue";
    private String lifeLineColor = "red";
    private String FamilyLineColor = "green";


    public static Data getInstance()
    {
        if (single_instance == null)
        {
            single_instance = new Data();
        }
        return single_instance;
    }

    public List<Event> getPersonalEvents(String personID)
    {
        List<Event> personalEvents = new ArrayList<>();
        for (Event each: shownEvent)
        {
            if (each.getPersonID().equals(personID))
            {
                personalEvents.add(each);
            }
        }
        Collections.sort(personalEvents, new  Comparator<Event> ()
        {
            @Override
            public int compare(Event first, Event second)
            {
                int diff = first.getYear() - second.getYear();
                if (diff == 0)
                {
                    diff = first.getEventType().compareTo(second.getEventType());
                }
                return diff;
            }
        });
        return personalEvents;
    }

    public Map<String, Set<Person>> getPersonIDtoParents() {
        return personIDtoParents;
    }

    public Map<String, Person> getParentIDtoChildren() {
        return parentIDtoChildren;
    }

    public boolean isSpouseLine() {
        return isSpouseLine;
    }


    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }
    public void setSpouseLine(boolean spouseLine) {
        isSpouseLine = spouseLine;
    }

    public boolean isLifeLine() {
        return isLifeLine;
    }

    public void setLifeLine(boolean lifeLine) {
        isLifeLine = lifeLine;
    }

    public boolean isFamilyTreeLine() {
        return isFamilyTreeLine;
    }

    public void setFamilyTreeLine(boolean familyTreeLine) {
        isFamilyTreeLine = familyTreeLine;
    }

    public String getSpouseLineColor() {
        return spouseLineColor;
    }

    public void setSpouseLineColor(String spouseLineColor) {
        this.spouseLineColor = spouseLineColor;
    }

    public String getLifeLineColor() {
        return lifeLineColor;
    }

    public void setLifeLineColor(String lifeLineColor) {
        this.lifeLineColor = lifeLineColor;
    }

    public String getFamilyLineColor() {
        return FamilyLineColor;
    }

    public void setFamilyLineColor(String familyLineColor) {
        FamilyLineColor = familyLineColor;
    }

    public List<Person> getImmediateFaimly(String personID)
    {
        List<Person> family = new ArrayList<>();
        for (Map.Entry<String, Set<Person>> entry : personIDtoParents.entrySet())
        {
            if (entry.getKey().equals(personID))
            {
                for(Person parent: entry.getValue())
                {
                    family.add(parent);
                }
                break;
            }
        }
        for (Map.Entry<String, Person> entry : parentIDtoChildren.entrySet())
        {
            if (entry.getKey().equals(personID))
            {
                family.add(entry.getValue());
                break;
            }
        }
        Person person = getPersonByID(personID);
        Person spouse = getPersonByID(person.getSpouse());
        if (spouse != null)
        {
            family.add(spouse);
        }
        return family;
    }


    public void filter()
    {
        shownEvent = new ArrayList<>();
        List<Event> tempList1 = new ArrayList<>();
        List<Event> tempList2 = new ArrayList<>();
        if (ismotherSideEvent)
        {
            tempList1.addAll(motherSideEvent);
        }
        if (isfatherSiceEvent)
        {
            tempList1.addAll(fatherSideEvent);
        }
        // add user's event
        for (Event each: eventList)
        {
            if (each.getPersonID().equals(currentPerson.getPersonID()))
            {
                tempList1.add(each);
            }
        }
        if (ismaleEvent)
        {
            for(Event each: tempList1)
            {
                if (eventIDToGender.get(each.getEventID()).equals("m"))
                {
                    tempList2.add(each);
                }
            }
        }
        if (isfemaleEvent)
        {
            for(Event each: tempList1)
            {
                if (eventIDToGender.get(each.getEventID()).equals("f"))
                {
                    tempList2.add(each);
                }
            }
        }
        for (Event each: tempList2)
        {
            if (eventFilter.get(each.getEventType()))
            {
                shownEvent.add(each);
            }
        }
    }
    private void separateEventByGender()
    {
        for(Event eachEvent: eventList)
        {
            for (Person eachPerson: personList)
            {
                if (eachEvent.getPersonID().equals(eachPerson.getPersonID()))
                {
                    String gender = eachPerson.getGender().toLowerCase();
                    eventIDToGender.put(eachEvent.getEventID(),gender);
                    eventIDToBelongerID.put(eachEvent.getEventID(),eachPerson.getPersonID());
                    break;
                }
            }
        }
    }
    public void processData(String personID)
    {
        separateEventByGender();
//        Person currentPerson = null;
        Set<Person> parents = new HashSet<>();
        for (Person each: personList)
        {
            if (each.getPersonID().equals(personID))
            {
                currentPerson = each;
                break;
            }
        }
        Person father = null;
        Person mother = null;
        for (Person each: personList)
        {
            if (currentPerson.getFather().equals(each.getPersonID()))
            {
                parents.add(each);
                father = each;
                parentIDtoChildren.put(father.getPersonID(),currentPerson);
            }
            if (currentPerson.getMother().equals(each.getPersonID()))
            {
                parents.add(each);
                mother = each;
                parentIDtoChildren.put(mother.getPersonID(),currentPerson);
            }
        }
        personIDtoParents.put(personID,parents);
        if (father != null)
        {
            fillSidesEvent(father.getPersonID(),"father");
            recurTree(father,"father");
        }
        if (mother != null)
        {
            fillSidesEvent(mother.getPersonID(),"mother");
            recurTree(mother,"mother");
        }
        filter();
    }

    public Map<String, Boolean> getEventFilter() {
        return eventFilter;
    }

    public void setEventFilter(Map<String, Boolean> eventFilter) {
        this.eventFilter = eventFilter;
    }

    private void recurTree(Person person, String side)
    {
        Set<Person> parents = new HashSet<>();
        Person father = null;
        Person mother = null;
        for (Person each: personList)
        {
            if (person.getMother().equals(each.getPersonID()))
            {
                parents.add(each);
                father = each;
                parentIDtoChildren.put(father.getPersonID(),person);
            }
            if (person.getFather().equals(each.getPersonID()))
            {
                parents.add(each);
                mother = each;
                parentIDtoChildren.put(mother.getPersonID(),person);
            }
        }
        personIDtoParents.put(person.getPersonID(),parents);
        if (father != null)
        {
            fillSidesEvent(father.getPersonID(),side);
            recurTree(father,side);
        }
        if (mother != null)
        {
            fillSidesEvent(mother.getPersonID(),side);
            recurTree(mother,side);
        }
    }

    private void fillSidesEvent(String personID, String side)
    {
        for(Event each: eventList)
        {
            if (each.getPersonID().equals(personID))
            {
                if (side.equals("father"))
                {
                    fatherSideEvent.add(each);
                }
                else if(side.equals("mother"))
                {
                    motherSideEvent.add(each);
                }
            }
        }
    }

    public void setEventList(List<Event> eventList) {

        this.eventList = eventList;
        for (Event each: eventList)
        {
            if (!eventFilter.containsKey(each.getEventType()))
            {
                String eventType = each.getEventType().toLowerCase();
                eventFilter.put(eventType,true);
            }
        }
    }

    public Map<String, String> getEventIDToBelongerID() {
        return eventIDToBelongerID;
    }

    public Person getPersonByID (String ID)
    {
        for (Person each: personList)
        {
            if (each.getPersonID().equals(ID))
            {
                return each;
            }
        }
        return null;
    }

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public Event getCurrentEvent() {
        return currentEvent;
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

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public Map<String, Integer> getMarkerColor() {
        return markerColor;
    }

    public void setMarkerColor(Map<String, Integer> markerColor) {
        this.markerColor = markerColor;
    }

    public String getHostString() {
        return hostString;
    }

    public void setHostString(String hostString) {
        this.hostString = hostString;
    }

    public String getPortString() {
        return portString;
    }

    public void setPortString(String portString) {
        this.portString = portString;
    }

    public String getUserString() {
        return userString;
    }

    public void setUserString(String userString) {
        this.userString = userString;
    }

    public String getPasswordString() {
        return passwordString;
    }

    public void setPasswordString(String passwordString) {
        this.passwordString = passwordString;
    }

    public boolean isIsmaleEvent() {
        return ismaleEvent;
    }

    public void setIsmaleEvent(boolean ismaleEvent) {
        this.ismaleEvent = ismaleEvent;
    }

    public boolean isIsfemaleEvent() {
        return isfemaleEvent;
    }

    public void setIsfemaleEvent(boolean isfemaleEvent) {
        this.isfemaleEvent = isfemaleEvent;
    }

    public boolean isIsmotherSideEvent() {
        return ismotherSideEvent;
    }

    public void setIsmotherSideEvent(boolean ismotherSideEvent) {
        this.ismotherSideEvent = ismotherSideEvent;
    }

    public boolean isIsfatherSiceEvent() {
        return isfatherSiceEvent;
    }

    public void setIsfatherSiceEvent(boolean isfatherSiceEvent) {
        this.isfatherSiceEvent = isfatherSiceEvent;
    }

}
