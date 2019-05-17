package com.example.cs3270a5;

import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar; //use this not android widget to support the backwards compatible toolbar
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //As we have the ChangeResults as a fragment and not as a
        //fragment and not a FrameLayout, we do not need to add it here
//        fragMan = getSupportFragmentManager();
//        fragMan.beginTransaction()
//                .replace(R.id.fragChangeResults, new ChangeResults(), "fragChangeResults")
//                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //use setSupportActionBar for backwards compatibility

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //inflates our menu xml as an actual java object
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_menuZeroCorrectCount:
                //some Method here when zeroCorrectCount is pressed
                return true;
            case R.id.action_menuSetMaxChange:
                //some Method here when menuSetMaxChange is pressed
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
