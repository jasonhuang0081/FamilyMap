package com.example.familymap;

import com.example.familymap.model.Data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

import static org.junit.Assert.assertEquals;

public class ModelTestFilterEvents {
    private Event childEvent;
    private Event fatherEvent;
    private Event motherEvent;
    private Event gradpaEvent;
    private Event gradmaEvent;
    private Person child;
    private Person father;
    private Person mother;
    private Person gradpa;
    private Person gradma;
    private List<Event> eventList = new ArrayList<>();
    private List<Person> personList = new ArrayList<>();

    @Before
    public void setUp() {
        child = new Person("a", "child", "current", "A",
                "m", "fatherID", "motherID", "notExist");
        father = new Person("fatherID", "child", "Jackson", "A",
                "m", "grandpaID", "grandmaID", "motherID");
        mother = new Person("motherID", "child", "Eve", "whitening",
                "f", "grandfatherID", "grandmotherID", "fatherID");
        gradpa = new Person("grandpaID","child","random","last",
                "m","greatGrandpaID","greatGrandmaID","grandmaID");
        gradma = new Person("grandmaID","child","r2","ty",
                "f","greatGrandFatherID","greatGrandMotherID","grandpaID");

        childEvent = new Event("Biking_123A", "myName", "a",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 1994);
        fatherEvent = new Event("abc123", "myName", "fatherID",
                16.4f, 15.3f, "Korea", "Okinawa",
                "surfing", 1994);
        motherEvent = new Event("ddd345", "myName", "motherID",
                12f, 17f, "Taiwan", "Taipei",
                "shopping", 2000);
        gradpaEvent = new Event("aaaaa", "myName", "grandpaID",
                17f, 10f, "sd", "China",
                "driving", 1999);
        gradmaEvent = new Event("abacde", "myName", "grandmaID",
                7f, 11f, "sd", "China",
                "surfing", 1999);


    }
    @After
    public void tearDown()
    {
        Data.getInstance().reset();
    }
    @Test
    public void filterFatherEventsPass()
    {
        eventList.add(motherEvent);
        eventList.add(fatherEvent);
        eventList.add(childEvent);
        eventList.add(gradpaEvent);
        eventList.add(gradmaEvent);
        personList.add(child);
        personList.add(father);
        personList.add(mother);
        personList.add(gradpa);
        personList.add(gradma);
        Data.getInstance().setEventList(eventList);
        Data.getInstance().setPersonList(personList);
        Data.getInstance().setCurrentPerson(child);
        Data.getInstance().processData(child.getPersonID());

        Data.getInstance().setIsfatherSiceEvent(false);
        Data.getInstance().filter();
        List<Event> result = Data.getInstance().getShownEvent();
        List<Event> actual = new ArrayList<>();
        actual.add(childEvent);
        actual.add(motherEvent);
        assertEquals(result,actual);
    }
    @Test
    public void filterFatherEventsFail()
    {
        // no father and father father side events
        eventList.add(motherEvent);
        eventList.add(childEvent);
        personList.add(child);
        personList.add(mother);
        Data.getInstance().setEventList(eventList);
        Data.getInstance().setPersonList(personList);
        Data.getInstance().setCurrentPerson(child);
        Data.getInstance().processData(child.getPersonID());

        Data.getInstance().setIsfatherSiceEvent(false);
        Data.getInstance().filter();
        List<Event> result = Data.getInstance().getShownEvent();
        List<Event> actual = new ArrayList<>();
        actual.add(childEvent);
        actual.add(motherEvent);
        assertEquals(result,actual);

    }
    @Test
    public void filterMaleEventsPass()
    {
        eventList.add(motherEvent);
        eventList.add(fatherEvent);
        eventList.add(childEvent);
        eventList.add(gradpaEvent);
        eventList.add(gradmaEvent);
        personList.add(child);
        personList.add(father);
        personList.add(mother);
        personList.add(gradpa);
        personList.add(gradma);
        Data.getInstance().setEventList(eventList);
        Data.getInstance().setPersonList(personList);
        Data.getInstance().setCurrentPerson(child);
        Data.getInstance().processData(child.getPersonID());

        Data.getInstance().setIsmaleEvent(false);
        Data.getInstance().filter();
        List<Event> result = Data.getInstance().getShownEvent();
        List<Event> actual = new ArrayList<>();
        actual.add(motherEvent);
        actual.add(gradmaEvent);
        assertEquals(result,actual);
    }
    @Test
    public void filterMaleEventsFail()
    {
        // no male event at all
        eventList.add(motherEvent);
        eventList.add(gradmaEvent);
        personList.add(child);
        personList.add(father);
        personList.add(mother);
        personList.add(gradpa);
        personList.add(gradma);
        Data.getInstance().setEventList(eventList);
        Data.getInstance().setPersonList(personList);
        Data.getInstance().setCurrentPerson(child);
        Data.getInstance().processData(child.getPersonID());

        Data.getInstance().setIsmaleEvent(false);
        Data.getInstance().filter();
        List<Event> result = Data.getInstance().getShownEvent();
        List<Event> actual = new ArrayList<>();
        actual.add(motherEvent);
        actual.add(gradmaEvent);
        assertEquals(result,actual);
    }
    @Test
    public void filterSpecialEventPass()
    {
        eventList.add(motherEvent);
        eventList.add(fatherEvent);
        eventList.add(childEvent);
        eventList.add(gradpaEvent);
        eventList.add(gradmaEvent);
        personList.add(child);
        personList.add(father);
        personList.add(mother);
        personList.add(gradpa);
        personList.add(gradma);
        Data.getInstance().setEventList(eventList);
        Data.getInstance().setPersonList(personList);
        Data.getInstance().setCurrentPerson(child);
        Data.getInstance().processData(child.getPersonID());

        // do not show the event type that is the same as fatherEvent, which is surfing type
        Map<String, Boolean> temp = Data.getInstance().getEventFilter();
        temp.put(fatherEvent.getEventType(),false);
        Data.getInstance().setEventFilter(temp);
        Data.getInstance().filter();
        List<Event> result = Data.getInstance().getShownEvent();
        List<Event> actual = new ArrayList<>();
        actual.add(gradpaEvent);
        actual.add(childEvent);
        actual.add(motherEvent);
        assertEquals(result,actual);

    }
    @Test
    public void filterSpecialEventFail()
    {
        eventList.add(motherEvent);
        eventList.add(childEvent);
        eventList.add(gradpaEvent);
        personList.add(child);
        personList.add(father);
        personList.add(mother);
        personList.add(gradpa);
        personList.add(gradma);
        Data.getInstance().setEventList(eventList);
        Data.getInstance().setPersonList(personList);
        Data.getInstance().setCurrentPerson(child);
        Data.getInstance().processData(child.getPersonID());

        //desired event to be turn off doesn't exist
        Map<String, Boolean> temp = Data.getInstance().getEventFilter();
        temp.put(fatherEvent.getEventType(),false);
        Data.getInstance().setEventFilter(temp);
        Data.getInstance().filter();
        List<Event> result = Data.getInstance().getShownEvent();
        List<Event> actual = new ArrayList<>();
        actual.add(gradpaEvent);
        actual.add(childEvent);
        actual.add(motherEvent);
        assertEquals(result,actual);

    }

}
