package com.example.familymap.searchFunction;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.familymap.model.Data;
import com.example.familymap.eventFunction.EventActivity;
import com.example.familymap.mainActivityFunction.MainActivity;
import com.example.familymap.personFunction.PersonActivity;
import com.example.familymap.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private List<SearchItem> SearchResultList = new ArrayList<>();
    private SearchAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.searchResultView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SearchView searchView = findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                SearchResultList = Data.getInstance().doSearching(query.toLowerCase());
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


    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView firstLineInfo;
        private TextView secondLineInfo;
        private ImageView image;
        private SearchItem currentItem;
        Drawable eventIcon;
        Drawable femaleIcon;
        Drawable maleIcon;

        ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_search_result, parent, false));
            firstLineInfo =  itemView.findViewById(R.id.firstLineInfo);
            secondLineInfo = itemView.findViewById(R.id.secondLineInfo);
            image =  itemView.findViewById(R.id.searchResultImage);
            itemView.setOnClickListener(this);
        }
        void bind(SearchItem item) {
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
