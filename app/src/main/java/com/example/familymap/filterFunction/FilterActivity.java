package com.example.familymap.filterFunction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.familymap.model.Data;
import com.example.familymap.mainActivityFunction.MainActivity;
import com.example.familymap.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilterActivity extends AppCompatActivity {
    private List<FilterItem> filterList = new ArrayList<>();
    private FilterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        RecyclerView recyclerView = findViewById(R.id.filter_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        convertDataToList();
        if (adapter == null) {
            adapter = new FilterAdapter();
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void convertDataToList()
    {
        filterList = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : Data.getInstance().getEventFilter().entrySet())
        {
            String note = "Select to show " + entry.getKey() + " events";
            filterList.add(new FilterItem(entry.getKey(),note,entry.getValue()));
        }
        String note = "Select to show father's side events";
        filterList.add(new FilterItem("father's side",note,Data.getInstance().isIsfatherSiceEvent()));
        note = "Select to show mother's side events";
        filterList.add(new FilterItem("mother's side",note,Data.getInstance().isIsmotherSideEvent()));
        note = "Select to show male events";
        filterList.add(new FilterItem("males events",note,Data.getInstance().isIsmaleEvent()));
        note = "Select to show female events";
        filterList.add(new FilterItem("females events",note,Data.getInstance().isIsfemaleEvent()));
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private TextView eventTitleView;
        private TextView eventNoteView;
        private Switch eventSwitch;
        private FilterItem currentItem;

        ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_events, parent, false));
            eventTitleView =  itemView.findViewById(R.id.filterEvents);
            eventNoteView = itemView.findViewById(R.id.filterNote);
            eventSwitch =  itemView.findViewById(R.id.filterItem);
            eventSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (currentItem.getEventName().equals("father's side"))
                    {
                        Data.getInstance().setIsfatherSiceEvent(isChecked);
                    }
                    else if (currentItem.getEventName().equals("mother's side"))
                    {
                        Data.getInstance().setIsmotherSideEvent(isChecked);
                    }
                    else if (currentItem.getEventName().equals("males events"))
                    {
                        Data.getInstance().setIsmaleEvent(isChecked);
                    }
                    else if (currentItem.getEventName().equals("females events"))
                    {
                        Data.getInstance().setIsfemaleEvent(isChecked);
                    }
                    else
                    {
                        Map<String, Boolean> temp = Data.getInstance().getEventFilter();
                        temp.put(currentItem.getEventName(),isChecked);
                        Data.getInstance().setEventFilter(temp);
                    }
                    convertDataToList();
                }
            });
        }
        void bind(FilterItem item) {
            currentItem = item;
            eventTitleView.setText(item.getEventName());
            eventNoteView.setText(item.getNote());
            eventSwitch.setChecked(currentItem.isChecked());
        }
    }
    private class FilterAdapter extends RecyclerView.Adapter<ItemHolder> {
        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new ItemHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            FilterItem item = filterList.get(position);
            holder.bind(item);
        }
        @Override
        public int getItemCount() {
            return filterList.size();
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
