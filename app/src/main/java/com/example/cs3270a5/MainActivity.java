package com.example.cs3270a5;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar; //use this not android widget to support the backwards compatible toolbar
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements
        ChangeButtons.UpdateValues,
        ChangeActions.RestartGame,
        ChangeResults.UpdateCorrectCount,
        ChangeMaxAmount.UpdateMaxRange{

    private FragmentManager fragMan;
    private ChangeResults changeResultsFrag;
    private ChangeButtons changeButtonsFrag;
    private ChangeActions changeActionsFrag;
    private ChangeMaxAmount changeMaxAmountFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragMan = getSupportFragmentManager();
        fragMan.beginTransaction()
                .replace(R.id.fragChangeResults, new ChangeResults(), "fragChangeResults")
                .replace(R.id.fragChangeButtons, new ChangeButtons(), "fragChangeButtons")
                .replace(R.id.fragChangeActions, new ChangeActions(), "fragChangeActions")
                .commit();


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
                changeActionsFrag = (ChangeActions) fragMan.findFragmentById(R.id.fragChangeActions);

                if(changeActionsFrag != null){
                    changeActionsFrag.resetUserCorrectNum();
                }
                return true;
            case R.id.action_menuSetMaxChange:
                settingChangeMaxAmount();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void updateChange(double userTotal) {
        changeResultsFrag = (ChangeResults) fragMan.findFragmentById(R.id.fragChangeResults);

        if(changeResultsFrag != null){
            changeResultsFrag.updateUserChangeTotal(userTotal);
        }
    }

    @Override
    public void resetGame() {
        changeResultsFrag = (ChangeResults) fragMan.findFragmentById(R.id.fragChangeResults);
        if(changeResultsFrag != null){
            changeResultsFrag.resetGame();
        }

        changeButtonsFrag = (ChangeButtons) fragMan.findFragmentById(R.id.fragChangeButtons);
        if(changeButtonsFrag != null){
            changeButtonsFrag.resetUserTotal();
        }

    }

    @Override
    public void resetGameAndAmount() {
        changeResultsFrag = (ChangeResults) fragMan.findFragmentById(R.id.fragChangeResults);
        if (changeResultsFrag != null) {
            changeResultsFrag.resetGameAndAmount();
        }

        changeButtonsFrag = (ChangeButtons) fragMan.findFragmentById(R.id.fragChangeButtons);
        if (changeButtonsFrag != null) {
            changeButtonsFrag.resetUserTotal();
        }
    }

    @Override
    public void updateUserCorrectCount() {
        changeActionsFrag = (ChangeActions) fragMan.findFragmentById(R.id.fragChangeActions);

        if(changeActionsFrag != null){
            changeActionsFrag.updateUserCorrectNum();
        }
    }

    @Override
    public int getRange() {
        changeMaxAmountFrag  = (ChangeMaxAmount) fragMan.findFragmentById(R.id.fragChangeMaxChange);

        if(changeMaxAmountFrag != null){
            return changeMaxAmountFrag.getRange();
        }

        return 30;
    }

    @Override
    public void updateRange(int newRange) {
        changeResultsFrag = (ChangeResults) fragMan.findFragmentById(R.id.fragChangeResults);
        if (changeResultsFrag != null) {

            changeActionsFrag = (ChangeActions) fragMan.findFragmentById(R.id.fragChangeActions);
            changeButtonsFrag = (ChangeButtons) fragMan.findFragmentById(R.id.fragChangeButtons);
            changeMaxAmountFrag = (ChangeMaxAmount) fragMan.findFragmentById(R.id.fragChangeMaxChange);

            fragMan.beginTransaction()
                    .replace(R.id.fragChangeResults, new ChangeResults(), "fragChangeResults")
                    .replace(R.id.fragChangeButtons, new ChangeButtons(), "fragChangeButtons")
                    .replace(R.id.fragChangeActions, new ChangeActions(), "fragChangeActions")
                    .hide(changeMaxAmountFrag)
                    .commit();
        }
    }

    /** Called when the user taps on the set max amount */
    public void settingChangeMaxAmount() {
        changeMaxAmountFrag = (ChangeMaxAmount) fragMan.findFragmentById(R.id.fragChangeMaxChange);
        changeResultsFrag = (ChangeResults) fragMan.findFragmentById(R.id.fragChangeResults);
        changeActionsFrag = (ChangeActions) fragMan.findFragmentById(R.id.fragChangeActions);
        changeButtonsFrag = (ChangeButtons) fragMan.findFragmentById(R.id.fragChangeButtons);

        fragMan.beginTransaction()
                .replace(R.id.fragChangeMaxChange, new ChangeMaxAmount(), "fragChangeMaxAmount")
                .hide(changeResultsFrag)
                .hide(changeButtonsFrag)
                .hide(changeActionsFrag)
                .commit();
    }


}
