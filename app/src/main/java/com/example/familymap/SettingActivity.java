package com.example.familymap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {
    private Switch lifeStorySwitch;
    private Switch familyTreeSwitch;
    private Switch spouseSwitch;
    private Spinner lifeStorySpinner;
    private Spinner familyTreeSpinner;
    private Spinner spouseSpinner;
    private Spinner mapTypeSpinner;
    private View reSyncView;
    private View logoutView;
    private List<String> colorOptions = new ArrayList<>();
    private List<String> mapOption = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        lifeStorySwitch = findViewById(R.id.lifeStorySwitch);
        familyTreeSwitch = findViewById(R.id.familyTreeSwitch);
        spouseSwitch = findViewById(R.id.spouseSwitch);
        lifeStorySpinner = findViewById(R.id.lifeStorySpinner);
        familyTreeSpinner = findViewById(R.id.familyTreeSpinner);
        spouseSpinner = findViewById(R.id.spouseSpinner);
        mapTypeSpinner = findViewById(R.id.mapSpinner);
        colorOptions.add("blue");
        colorOptions.add("red");
        colorOptions.add("green");
        colorOptions.add("magenta");
        colorOptions.add("yellow");
        colorOptions.add("black");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colorOptions);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lifeStorySpinner.setAdapter(dataAdapter);
        lifeStorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                Data.getInstance().setLifeLineColor( parent.getItemAtPosition(pos).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        familyTreeSpinner.setAdapter(dataAdapter);
        familyTreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                Data.getInstance().setLifeLineColor( parent.getItemAtPosition(pos).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        spouseSpinner.setAdapter(dataAdapter);
        spouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                Data.getInstance().setSpouseLineColor( parent.getItemAtPosition(pos).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        mapOption.add("normal");
        mapOption.add("hybrid");
        mapOption.add("satellite");
        mapOption.add("terrain");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mapOption);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapTypeSpinner.setAdapter(dataAdapter2);
        mapTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                Data.getInstance().setMapType( parent.getItemAtPosition(pos).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                Data.getInstance().setLifeLine(isChecked);
            }
        });
        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Data.getInstance().setFamilyTreeLine(isChecked);
            }
        });
        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Data.getInstance().setSpouseLine(isChecked);
            }
        });
        reSyncView = findViewById(R.id.reSync);
        reSyncView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        logoutView = findViewById(R.id.logout);
        logoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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