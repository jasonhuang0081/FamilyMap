package com.example.familymap;

import com.example.familymap.model.Data;
import com.example.familymap.searchFunction.SearchItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import model.Event;
import model.Person;

import static org.junit.Assert.assertEquals;

public class ModelTestSearching {
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
    public void setUp()
    {
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
    public void searchEventsPass()
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

        // search father's event with its country name
        List<SearchItem> result = Data.getInstance().doSearching("Korea".toLowerCase());
        // there should be one item only
        assertEquals(result.size(),1);
        assertEquals(result.get(0).getEvent(),fatherEvent);
    }
    @Test
    public void searchEventsFail()
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

        // search something not in there
        List<SearchItem> result = Data.getInstance().doSearching("thereIsNoSuchAThingInThere".toLowerCase());
        // there should be one item only
        assertEquals(result.size(),0);
    }
    @Test
    public void searchPersonPass()
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

        // search father's event with its country name
        List<SearchItem> result = Data.getInstance().doSearching("Eve".toLowerCase());
        // there should be one item only
        assertEquals(result.size(),1);
        assertEquals(result.get(0).getPerson(),mother);
    }
    @Test
    public void searchPersonFail()
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

        // search father's event with its country name
        List<SearchItem> result = Data.getInstance().doSearching("NoBodyIsWithThisName".toLowerCase());
        // there should be one item only
        assertEquals(result.size(),0);
    }
}
