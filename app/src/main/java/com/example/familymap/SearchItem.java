package com.example.familymap;

import model.Event;
import model.Person;

public class SearchItem {
    private String firstLineInfo;
    private String secondLineInfo;
    private boolean isEvent;    // if not event, it will be person
    private boolean isMale;
    private Person person = null;
    private Event event = null;

    public SearchItem() {
    }

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

    public void setPerson(Person person) {
        this.person = person;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public String getFirstLineInfo() {
        return firstLineInfo;
    }

    public void setFirstLineInfo(String firstLineInfo) {
        this.firstLineInfo = firstLineInfo;
    }

    public String getSecondLineInfo() {
        return secondLineInfo;
    }

    public void setSecondLineInfo(String secondLineInfo) {
        this.secondLineInfo = secondLineInfo;
    }

    public boolean isEvent() {
        return isEvent;
    }

    public void setEvent(boolean event) {
        isEvent = event;
    }
}
