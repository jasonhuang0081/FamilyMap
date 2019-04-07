package com.example.familymap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import model.Event;
import model.Person;

public class ModelTestSortingEvents {
    Event firstEvent;
    Event secondEvent;
    Event thirdEvent;
    Event notUserEvent;
    Person currentuser;
    Person otherUser2;
    Person otherUser;
    private List<Event> eventList = new ArrayList<>();
    private List<Person> personList = new ArrayList<>();

    @Before
    public void setUp() {
        currentuser =  new Person("userID", "child", "current", "A",
                "m", "fatherID", "motherID", "notExist");
        otherUser =  new Person("someone", "others", "first", "last",
                "m", "other", "others", "David");
        otherUser2 =  new Person("someone", "others", "first", "last",
                "m", "other", "others", "David");
        firstEvent = new Event("Biking_123A", "myName", "userID",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 1994);
        secondEvent = new Event("abc123", "myName", "userID",
                16.4f, 15.3f, "Korea", "Okinawa",
                "surfing", 1994);
        thirdEvent = new Event("ddd345", "myName", "userID",
                12f, 17f, "Taiwan", "Taipei",
                "shopping", 2000);
        notUserEvent = new Event("aaaaa", "myName", "someone",
                17f, 10f, "sd", "China",
                "shopping", 1999);

        // the order of insertion is random and not in order
        eventList.add(thirdEvent);
        eventList.add(secondEvent);
        eventList.add(firstEvent);
        eventList.add(notUserEvent);
        personList.add(currentuser);
        personList.add(otherUser);
        personList.add(otherUser2);
        Data.getInstance().setEventList(eventList);
        Data.getInstance().setPersonList(personList);
        Data.getInstance().setCurrentPerson(currentuser);
        Data.getInstance().processData(currentuser.getPersonID());

    }
    @After
    public void tearDown()
    {

    }
    @Test
    public void SortingPass()
    {
        // checking years and if years are the same event type's alphabatical order
        List<Event> sortedEvents = Data.getInstance().getPersonalEvents(currentuser.getPersonID());
        assertEquals(sortedEvents.get(0),firstEvent);
        assertEquals(sortedEvents.get(1),secondEvent);
        assertEquals(sortedEvents.get(2),thirdEvent);
        assertEquals(sortedEvents.size(),3);
    }
    @Test
    public void SortingFail()
    {
        List<Event> sortedEvents = Data.getInstance().getPersonalEvents(otherUser2.getPersonID());
        List<Event> emptyEvents = new ArrayList<>();
        assertEquals(sortedEvents,emptyEvents);
    }
}
