package com.example.cs3270a5;

import android.content.res.Configuration;
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
        ChangeMaxAmount.DisplayMainView{

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
        reinitializeAllFragments();

        //if we are in landscape are hiding action frag -> means we are in the 'Set Max Change' menu and want to keep that view until the user presses save
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && (changeActionsFrag != null && changeActionsFrag.isHidden())){
            resetSetMaxTransaction();

        //if we are in landscape are hiding action frag -> means we are in the 'Set Max Change' menu and want to keep that view until the user presses save
        }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && (changeActionsFrag != null && changeActionsFrag.isHidden())) {
            resetSetMaxTransaction();

        }else {
            fragMan.beginTransaction()
                    .replace(R.id.fragChangeResults, new ChangeResults(), "fragChangeResults")
                    .replace(R.id.fragChangeButtons, new ChangeButtons(), "fragChangeButtons")
                    .replace(R.id.fragChangeActions, new ChangeActions(), "fragChangeActions")
                    .commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //use setSupportActionBar for backwards compatibility

    }

    /**
     * does our options toolbar,
     * make sure to inflate everytime
     * @param menu main_menu class we created
     * @return true on success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //inflates our menu xml as an actual java object
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    /**
     * Overriden method that is called when a menu item is selected in the toolbar
     *
     * @param item from the toolbar menu
     * @return true on success
     */
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

    /**
     * updates the user change so far when the user clicks one of the 10 buttons in
     * the ChangeButtons class
     * @param userTotal from the ChangeButton class
     */
    @Override
    public void updateChange(double userTotal) {
        changeResultsFrag = (ChangeResults) fragMan.findFragmentById(R.id.fragChangeResults);


        if(changeResultsFrag != null){
            changeResultsFrag.updateUserChangeTotal(userTotal);
        }
    }

    /**
     * resets the Change total so far and the time remaining
     * when the user clicks the 'Start Over' button
     */
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

    /**
     * Resets the Change to make so far, timer, and Change to Make
     * when the 'New Amount' button is clicked
     */
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

    /**
     * Updates the user's total of correct could. Called from ChangeResults when the user wins
     */
    @Override
    public void updateUserCorrectCount() {
        changeActionsFrag = (ChangeActions) fragMan.findFragmentById(R.id.fragChangeActions);

        if(changeActionsFrag != null){
            changeActionsFrag.updateUserCorrectNum();
        }
    }

    /**
     * Gets the range from ChangeMaxAmount class on onResume in the ChangeResults class
     * @return returns the range from the ChangeMaxClass or 30 if something went wrong
     */
    @Override
    public int getRange() {
        changeMaxAmountFrag  = (ChangeMaxAmount) fragMan.findFragmentById(R.id.fragChangeMaxAmount);

        if(changeMaxAmountFrag != null){
            return changeMaxAmountFrag.getRange();
        }

        return 30;
    }

    /**
     * sets our user total to zero when called when the menu button is clicked
     * communicates to the ChangeButtons class to do so
     */
    @Override
    public void setUserTotalZero() {
        changeButtonsFrag = (ChangeButtons) fragMan.findFragmentById(R.id.fragChangeButtons);
        if (changeButtonsFrag != null) {
            changeButtonsFrag.resetUserTotal();
        }
    }

    /**
     * disables all of the buttons found in ChangeButtons
     * called when the timer runes out,
     * change total so far is the same as change to make,
     * change total so far is greater than change to make.
     */
    @Override
    public void disableMoneyButtons() {
        changeButtonsFrag = (ChangeButtons) fragMan.findFragmentById(R.id.fragChangeButtons);
        if (changeButtonsFrag != null) {
            changeButtonsFrag.disableAllMoneyButtons();
        }
    }

    /**
     * enables all of the buttons found in ChangeButtons
     * called when the resetGame and resetGameAndAmount
     * is called in ChangeResults
     */
    @Override
    public void enableMoneyButtons() {
        changeButtonsFrag = (ChangeButtons) fragMan.findFragmentById(R.id.fragChangeButtons);
        if (changeButtonsFrag != null) {
            changeButtonsFrag.enableAllMoneyButtons();
        }
    }

    /**
     * called when we click the save button in ChangeMaxAmount
     * re-displays the proper fragments - ChangeResults, ChangeButtons, ChangeActions
     */
    @Override
    public void displayMainView() {
        changeMaxAmountFrag = (ChangeMaxAmount) fragMan.findFragmentById(R.id.fragChangeMaxAmount);

        fragMan.beginTransaction()
                .replace(R.id.fragChangeResults, new ChangeResults(), "fragChangeResults")
                .replace(R.id.fragChangeButtons, new ChangeButtons(), "fragChangeButtons")
                .replace(R.id.fragChangeActions, new ChangeActions(), "fragChangeActions")
                .hide(changeMaxAmountFrag)
                .commit();

    }

    /**
     * Reinitialises all fragments and removes the changeResultsFrag
     * due to the timer not being destroyed when the view would change.
     * This would cause the timer to keep going and display a message when it is complete
     */
    public void settingChangeMaxAmount() {
        reinitializeAllFragments();

        fragMan.beginTransaction()
                .replace(R.id.fragChangeMaxAmount, new ChangeMaxAmount(), "fragChangeMaxAmount")
                .remove(changeResultsFrag) //I decided to remove this fragment because of the timer activity
                .hide(changeButtonsFrag)
                .hide(changeActionsFrag)
                .commit();
    }

    /*
     * Private helper that resets the set max screen when it ir rotated
     * instead of destroying the view and reinitializing it with the main one
     * we keep the view until the user selects save
     */
    private void resetSetMaxTransaction(){
        fragMan.beginTransaction()
                .replace(R.id.fragChangeMaxAmount, new ChangeMaxAmount(), "fragChangeMaxAmount")
                .hide(changeButtonsFrag)
                .hide(changeActionsFrag)
                .commit();
    }

    /*
        Reinitialzes all fragments -- mainly used to reconfigure the view when we change orientations
     */
    private void reinitializeAllFragments(){
        changeMaxAmountFrag = (ChangeMaxAmount) fragMan.findFragmentById(R.id.fragChangeMaxAmount);
        changeResultsFrag = (ChangeResults) fragMan.findFragmentById(R.id.fragChangeResults);
        changeActionsFrag = (ChangeActions) fragMan.findFragmentById(R.id.fragChangeActions);
        changeButtonsFrag = (ChangeButtons) fragMan.findFragmentById(R.id.fragChangeButtons);
    }

}
