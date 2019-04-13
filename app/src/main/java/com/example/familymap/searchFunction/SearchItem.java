package com.example.familymap.searchFunction;

import model.Event;
import model.Person;

public class SearchItem {
    private String firstLineInfo;
    private String secondLineInfo;
    private boolean isEvent;    // if not event, it will be person
    private boolean isMale;
    private Person person = null;
    private Event event = null;

    public SearchItem(String firstLineInfo, String secondLineInfo, boolean isEvent,
                      boolean isMale, Person person, Event event) {
        this.firstLineInfo = firstLineInfo;
        this.secondLineInfo = secondLineInfo;
        this.isEvent = isEvent;
        this.isMale = isMale;
        this.person = person;
        this.event = event;
    }

    public Person getPerson() {
        return person;
    }

    public Event getEvent() {
        return event;
    }

    public boolean isMale() {
        return isMale;
    }

    public String getFirstLineInfo() {
        return firstLineInfo;
    }

    public String getSecondLineInfo() {
        return secondLineInfo;
    }

    public boolean isEvent() {
        return isEvent;
    }

}
