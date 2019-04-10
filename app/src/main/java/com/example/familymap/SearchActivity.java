package com.example.familymap;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {
    List<SearchItem> SearchResultList = new ArrayList<>();
    private SearchAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.searchResultView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                SearchResultList = doSearching(query.toLowerCase());
                if (adapter == null) {
                    adapter = new SearchActivity.SearchAdapter();
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

    }
    private List<SearchItem> doSearching(String input)
    {
        List<Person> personList = Data.getInstance().getPersonList();
        List<Event> eventList = Data.getInstance().getShownEvent();
        List<SearchItem> result = new ArrayList<>();
        for (Person each: personList)
        {
            if (each.getFirstName().toLowerCase().contains(input) || each.getLastName().toLowerCase().contains(input))
            {
                String firstLine = each.getFirstName() + " " + each.getLastName();
                boolean isMale = false;
                if (each.getGender().equals("m"))
                {
                    isMale = true;
                }
                result.add(new SearchItem(firstLine,"",false,isMale,each,null));
            }
        }
        for (Event each: eventList)
        {
            if (each.getCountry().toLowerCase().contains(input) || each.getCity().toLowerCase().contains(input)
                    || each.getEventType().toLowerCase().contains(input))
            {
                String firstLine = each.getEventType() + ": " + each.getCity() + ", " + each.getCountry()
                        + " (" + each.getYear() + ")";
                String personID = Data.getInstance().getEventIDToBelongerID().get(each.getEventID());
                Person person = Data.getInstance().getPersonByID(personID);
                String secondLine = person.getFirstName() + " " + person.getLastName();
                result.add(new SearchItem(firstLine,secondLine,true,false,null,each));

            }
        }
        return result;
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView firstLineInfo;
        private TextView secondLineInfo;
        private ImageView image;
        private SearchItem currentItem;
        Drawable eventIcon;
        Drawable femaleIcon;
        Drawable maleIcon;

        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_search_result, parent, false));
            firstLineInfo =  itemView.findViewById(R.id.firstLineInfo);
            secondLineInfo = itemView.findViewById(R.id.secondLineInfo);
            image =  itemView.findViewById(R.id.searchResultImage);
            itemView.setOnClickListener(this);
        }
        public void bind(SearchItem item) {
            currentItem = item;

            firstLineInfo.setText(item.getFirstLineInfo());
            secondLineInfo.setText(item.getSecondLineInfo());
            eventIcon = new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.event_icon).sizeDp(40);
            femaleIcon = new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_female).
                    colorRes(R.color.female_icon).sizeDp(40);
            maleIcon = new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_male).
                    colorRes(R.color.male_icon).sizeDp(40);
            if (item.isEvent())
            {
                image.setImageDrawable(eventIcon);
            }
            else
            {
                if (item.isMale())
                {
                    image.setImageDrawable(maleIcon);
                }
                else
                {
                    image.setImageDrawable(femaleIcon);
                }
            }
        }
        @Override
        public void onClick(View view) {
            // check which activity and start that
            if (currentItem.isEvent())
            {
                Data.getInstance().setCurrentEvent(currentItem.getEvent());
                Intent intent = new Intent(getApplicationContext(), EventActivity.class);
                startActivity(intent);
            }
            else
            {
                Data.getInstance().setCurrentPerson(currentItem.getPerson());
                Intent intent = new Intent(getApplicationContext(), PersonActivity.class);
                startActivity(intent);
            }
        }
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchActivity.ItemHolder> {
        @Override
        public SearchActivity.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new SearchActivity.ItemHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(SearchActivity.ItemHolder holder, int position) {
            SearchItem item = SearchResultList.get(position);
            holder.bind(item);
        }
        @Override
        public int getItemCount() {
            return SearchResultList.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}
