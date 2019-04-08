package com.example.familymap;

public class FilterItem {
    private String eventName;
    private String note;
    private boolean isChecked;

    public FilterItem() {
    }

    public FilterItem(String eventName, String note, boolean isChecked) {
        this.eventName = eventName;
        this.note = note;
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public String getEventName() {
        return eventName;
    }


    public String getNote() {
        return note;
    }

}
