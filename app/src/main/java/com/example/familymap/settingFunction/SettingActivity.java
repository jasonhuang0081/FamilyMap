package com.example.familymap.settingFunction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.familymap.model.Data;
import com.example.familymap.mainActivityFunction.MainActivity;
import com.example.familymap.R;
import com.example.familymap.loginFunction.Proxy;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.Event;
import requests.LoginRequest;
import results.LoginResult;

public class SettingActivity extends AppCompatActivity {
    private List<String> colorOptions = new ArrayList<>();
    private List<String> mapOption = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Switch lifeStorySwitch = findViewById(R.id.lifeStorySwitch);
        Switch familyTreeSwitch = findViewById(R.id.familyTreeSwitch);
        Switch spouseSwitch = findViewById(R.id.spouseSwitch);
        Spinner lifeStorySpinner = findViewById(R.id.lifeStorySpinner);
        Spinner familyTreeSpinner = findViewById(R.id.familyTreeSpinner);
        Spinner spouseSpinner = findViewById(R.id.spouseSpinner);
        Spinner mapTypeSpinner = findViewById(R.id.mapSpinner);
        colorOptions.add("blue");
        colorOptions.add("red");
        colorOptions.add("green");
        colorOptions.add("magenta");
        colorOptions.add("yellow");
        colorOptions.add("black");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colorOptions);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lifeStorySpinner.setAdapter(dataAdapter);
        lifeStorySpinner.setSelection(dataAdapter.getPosition(Data.getInstance().getLifeLineColor()));
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
        familyTreeSpinner.setSelection(dataAdapter.getPosition(Data.getInstance().getFamilyLineColor()));
        familyTreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                Data.getInstance().setFamilyLineColor( parent.getItemAtPosition(pos).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        spouseSpinner.setAdapter(dataAdapter);
        spouseSpinner.setSelection(dataAdapter.getPosition(Data.getInstance().getSpouseLineColor()));
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
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mapOption);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapTypeSpinner.setAdapter(dataAdapter2);
        mapTypeSpinner.setSelection(dataAdapter2.getPosition(Data.getInstance().getMapType()));
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
        lifeStorySwitch.setChecked(Data.getInstance().isLifeLine());
        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Data.getInstance().setFamilyTreeLine(isChecked);
            }
        });
        familyTreeSwitch.setChecked(Data.getInstance().isFamilyTreeLine());
        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Data.getInstance().setSpouseLine(isChecked);
            }
        });
        spouseSwitch.setChecked(Data.getInstance().isSpouseLine());
        View reSyncView = findViewById(R.id.reSync);
        reSyncView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new reSync().execute();
            }
        });
        View logoutView = findViewById(R.id.logout);
        logoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private class reSync extends AsyncTask<Void,Void,Void> {
        String output;
        String hostString = Data.getInstance().getHostString();
        String portString = Data.getInstance().getPortString();
        String userString = Data.getInstance().getUserString();
        String passwordString = Data.getInstance().getPasswordString();
        String personID;
        @Override
        protected Void doInBackground(Void... params) {
            Proxy proxy = new Proxy();
            try {
                URL url = new URL("http://" + hostString + ":" + portString + "/user/login");
                LoginResult result = proxy.login(new LoginRequest(userString, passwordString), url);
                URL eventURL = new URL("http://" + hostString + ":" + portString + "/event");
                URL personURL = new URL("http://" + hostString + ":" + portString + "/person");
                String synchRes = proxy.getAllData(result.getToken(), eventURL, personURL);
                output = "Re-Sync Succeed!";
                personID = result.getPersonID();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                output = "Re-Sync Fail!";
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            Toast.makeText(getApplicationContext(), output, Toast.LENGTH_SHORT).show();
            if (output.equals("Re-Sync Succeed!"))
            {
                Data.getInstance().setMotherSideEvent(new ArrayList<Event>());
                Data.getInstance().setFatherSideEvent(new ArrayList<Event>());
                Data.getInstance().processData(personID);
            }
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