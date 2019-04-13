package com.example.familymap;

import com.example.familymap.model.Data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

import model.Event;
import model.Person;

public class ModelTestSortingEvents {
    private Event firstEvent;
    private Event secondEvent;
    private Event thirdEvent;
    private Event notUserEvent;
    private Person currentuser;
    private Person otherUser2;
    private Person otherUser;
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


    }
    @After
    public void tearDown()
    {
        Data.getInstance().reset();
    }
    @Test
    public void SortingPass()
    {
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

        // checking years and if years are the same event type's alphabatical order
        List<Event> sortedEvents = Data.getInstance().getPersonalEvents(currentuser.getPersonID());
        assertEquals(sortedEvents.get(0),firstEvent);
        assertEquals(sortedEvents.get(1),secondEvent);
        assertEquals(sortedEvents.get(2),thirdEvent);
        // to check there is nothing else in the list
        assertEquals(sortedEvents.size(),3);
    }
    @Test
    public void SortingFail()
    {
        eventList.add(thirdEvent);
        personList.add(currentuser);
        personList.add(otherUser);
        personList.add(otherUser2);
        Data.getInstance().setEventList(eventList);
        Data.getInstance().setPersonList(personList);
        Data.getInstance().setCurrentPerson(currentuser);
        Data.getInstance().processData(currentuser.getPersonID());

        // if there is only one event exist, to test if sorting will give an error
        List<Event> sortedEvents = Data.getInstance().getPersonalEvents(currentuser.getPersonID());
        assertEquals(sortedEvents.get(0),thirdEvent);
        // to check there is nothing else in the list
        assertEquals(sortedEvents.size(),1);
    }
}
