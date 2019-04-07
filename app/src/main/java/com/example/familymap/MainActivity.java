package com.example.familymap;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity {
    private MenuItem searchIcon;
    private MenuItem filterIcon;
    private MenuItem settingIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Iconify.with(new FontAwesomeModule());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.mainActivity);
        if (fragment == null) {
            fragment = new LoginFragment();
            fm.beginTransaction().add(R.id.mainActivity, fragment).commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        searchIcon = menu.findItem(R.id.search);
        searchIcon.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_search)
                .colorRes(R.color.menu_icon)
                .actionBarSize());
        filterIcon = menu.findItem(R.id.filter);
        filterIcon.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_filter)
                .colorRes(R.color.menu_icon)
                .actionBarSize());
        settingIcon = menu.findItem(R.id.setting);
        settingIcon.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_gear)
                .colorRes(R.color.menu_icon)
                .actionBarSize());
//        if (Data.getInstance().isSignIn())
//        {
//            SetMap();
//        }
        searchIcon.setVisible(false);
        filterIcon.setVisible(false);
        settingIcon.setVisible(false);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        Intent intent = null;
        switch(menu.getItemId()) {
            case R.id.search:
                        // open activity
                return true;
            case R.id.filter:
                // open activity
                return true;
            case R.id.setting:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.home:
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }
    public void SetMap()
    {
        searchIcon.setVisible(true);
        filterIcon.setVisible(true);
        settingIcon.setVisible(true);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.mainActivity, new MapFragment())
                .commit();
    }
}
