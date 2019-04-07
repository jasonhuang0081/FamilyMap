package com.example.familymap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {
    private Person currentPerson;
    Drawable eventIcon;
    Drawable femaleIcon;
    Drawable maleIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPerson = Data.getInstance().getCurrentPerson();
        setContentView(R.layout.activity_person);
        ExpandableListView expandableListView = findViewById(R.id.expandableView);

        eventIcon = new IconDrawable(this, FontAwesomeIcons.fa_map_marker).
                colorRes(R.color.event_icon).sizeDp(40);
        femaleIcon = new IconDrawable(this, FontAwesomeIcons.fa_female).
                colorRes(R.color.female_icon).sizeDp(40);
        maleIcon = new IconDrawable(this, FontAwesomeIcons.fa_male).
                colorRes(R.color.male_icon).sizeDp(40);

        TextView firstName =  findViewById(R.id.personFirstName);
        TextView lastName =  findViewById(R.id.personLastName);
        TextView gender =  findViewById(R.id.personGender);
        firstName.setText(Data.getInstance().getCurrentPerson().getFirstName());
        lastName.setText(Data.getInstance().getCurrentPerson().getLastName());
        String genderPrint;
        if (Data.getInstance().getCurrentPerson().getGender().equals("m"))
        {
            genderPrint = "Male";
        }
        else
        {
            genderPrint = "Female";
        }
        gender.setText(genderPrint);
        List<Person> familyMemberList = Data.getInstance().getImmediateFaimly(currentPerson.getPersonID());
        List<Event> eventList = Data.getInstance().getPersonalEvents(currentPerson.getPersonID());
        expandableListView.setAdapter(new ExpandableListAdapter(familyMemberList, eventList));

    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter{
        private static final int family_member = 0;
        private static final int life_event = 1;
        private final List<Person> familyMemberList;
        private final List<Event> eventList;

        ExpandableListAdapter(List<Person> familyMemberList, List<Event> eventList) {
            this.familyMemberList = familyMemberList;
            this.eventList = eventList;

        }
        @Override
        public int getGroupCount() {
            return 2;
        }
        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case family_member:
                    return familyMemberList.size();
                case life_event:
                    return eventList.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }
        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case family_member:
                    return getString(R.string.expandableFaimlyTitle);
                case life_event:
                    return getString(R.string.expandableEventTitle);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case family_member:
                    return familyMemberList.get(childPosition);
                case life_event:
                    return eventList.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }


        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case family_member:
                    titleView.setText(R.string.expandableFaimlyTitle);
                    break;
                case life_event:
                    titleView.setText(R.string.expandableEventTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView;

            switch(groupPosition) {
                case family_member:
                    itemView = layoutInflater.inflate(R.layout.expandable_item_view, parent, false);
                    initializeFamilyMember(itemView, childPosition);
                    break;
                case life_event:
                    itemView = layoutInflater.inflate(R.layout.expandable_item_view, parent, false);
                    initializeEvents(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }
        private void initializeEvents(View eventItemView, final int childPosition) {

            ImageView image = eventItemView.findViewById(R.id.expandableImage);
            image.setImageDrawable(eventIcon);
            TextView eventInfo = eventItemView.findViewById(R.id.firstLine);
            TextView belongerName = eventItemView.findViewById(R.id.secondLine);
            final Event currentEvent = eventList.get(childPosition);
            String firstInfo = currentEvent.getEventType() + ": "
                    + currentEvent.getCity() + ", " + currentEvent.getCountry()
                    + " (" + currentEvent.getYear() + ")";
            String personID = Data.getInstance().getEventIDToBelongerID().get(currentEvent.getEventID());
            final Person person = Data.getInstance().getPersonByID(personID);
            String secondInfo = person.getFirstName() + " " + person.getLastName();
            eventInfo.setText(firstInfo);
            belongerName.setText(secondInfo);
            eventItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /// bring it to event view of this event
                    ///////update the current person or event
                    Data.getInstance().setCurrentPerson(person);
                    Data.getInstance().setCurrentEvent(currentEvent);
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    startActivity(intent);
                }
            });
        }
        private void initializeFamilyMember(View familyMemberView, final int childPosition) {
            ImageView image = familyMemberView.findViewById(R.id.expandableImage);
            TextView name = familyMemberView.findViewById(R.id.firstLine);
            TextView relationship = familyMemberView.findViewById(R.id.secondLine);
            final Person person = familyMemberList.get(childPosition);
            String firstInfo = person.getFirstName() + " " + person.getLastName();
            String secondInfo = "";
            if(currentPerson.getFather().equals(person.getPersonID()))
            {
                secondInfo = "Father";
            }
            else if (currentPerson.getMother().equals(person.getPersonID()))
            {
                secondInfo = "Mother";
            }
            else if(currentPerson.getSpouse().equals(person.getPersonID()))
            {
                secondInfo = "Spouse";
            }
            else
            {
                secondInfo = "Child";
            }

            if (person.getGender().equals("m"))
            {
                image.setImageDrawable(maleIcon);
            }
            else
            {
                image.setImageDrawable(femaleIcon);
            }

            name.setText(firstInfo);
            relationship.setText(secondInfo);
            familyMemberView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /// bring it to person's activity
                    Data.getInstance().setCurrentPerson(person);
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    startActivity(intent);
                }
            });
        }
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


    }
}
