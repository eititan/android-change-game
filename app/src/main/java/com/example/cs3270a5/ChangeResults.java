package com.example.cs3270a5;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Top fragment in our ui that also is responsible for our dialog
 */
public class ChangeResults extends Fragment {

    private View root;
    private Locale defaultLocale = Resources.getSystem().getConfiguration().locale;         //format our money to the current locations denomination
    private NumberFormat numFormat = NumberFormat.getCurrencyInstance(defaultLocale);
    private Random randChange = new Random();
    private TextView txtCountdown, txtChangeToMake, txtUserChangeTotal;
    private DecimalFormat df = new DecimalFormat("##.#");   //format for the timer
    private DecimalFormat dfChangeToMake = new DecimalFormat("#.##");   //format for the money
    private BigDecimal bdChangeToMake, bdUserChangeTotal;
    private CountDownTimer countDownTimer;

    private UpdateCorrectCount mCallback;
    interface UpdateCorrectCount {
        void updateUserCorrectCount();
        int getRange();
        void setUserTotalZero();
        void disableMoneyButtons();
        void enableMoneyButtons();
    }

    public ChangeResults() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallback = (UpdateCorrectCount) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement the UpdateCorrectCount Interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       //generateRandomChange();
        return root = inflater.inflate(R.layout.fragment_change_results, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        //queuing for a commit
        spEdit.putString("changeToMake", String.valueOf(bdChangeToMake.doubleValue()));
        //apply to disk
        spEdit.apply();
        countDownTimer.cancel(); //Added bc null referenced we being called after old frag was destroyed so I destroy everytime when it's frag dies

    }

    @Override
    public void onResume() {
        super.onResume();

        txtCountdown = (TextView) root.findViewById(R.id.txt_countdown);

        countDownTimer = new CountDownTimer(30000, 10){

            @Override
            public void onTick(long millisUntilFinished) {
                txtCountdown.setText(df.format(millisUntilFinished/1000.00));
            }

            @Override
            public void onFinish() {
               createDialog(getResources().getString(R.string.too_long_title), getResources().getString(R.string.try_again_message));
               mCallback.disableMoneyButtons();
            }

        }.start();

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        String tempChange = sp.getString("changeToMake", "0.00");
        bdChangeToMake = new BigDecimal(dfChangeToMake.format(Double.parseDouble(tempChange)));

        txtChangeToMake = (TextView) root.findViewById(R.id.txt_changeToMakeOutput);
        if(txtChangeToMake!=null){
            txtChangeToMake.setText(numFormat.format(bdChangeToMake.doubleValue()));
        }

    }

    public void generateRandomChange(){
        double rangeMax = mCallback.getRange();
        double randomValue = 0 + (rangeMax - 0) * randChange.nextDouble();
        bdChangeToMake = new BigDecimal(dfChangeToMake.format(randomValue));

        txtChangeToMake = (TextView) root.findViewById(R.id.txt_changeToMakeOutput);
        if(txtChangeToMake != null){
            txtChangeToMake.setText(numFormat.format(bdChangeToMake.doubleValue()));
        }
    }

    public void updateUserChangeTotal(double userTotal){
        bdUserChangeTotal = new BigDecimal(dfChangeToMake.format(userTotal));
        txtUserChangeTotal = (TextView) root.findViewById(R.id.txt_ChangeTotalSoFarOutput);
        //set our Change Total So Far when our user clicks one of the change buttons
        if(txtUserChangeTotal != null){
            txtUserChangeTotal.setText(numFormat.format(bdUserChangeTotal.doubleValue()));
        }

        //winning statement and our change too high case
        //last && statement controls for the first time the app is used on both statements
        if((bdUserChangeTotal != null && bdChangeToMake != null) && bdUserChangeTotal.doubleValue() == bdChangeToMake.doubleValue() && (bdChangeToMake.doubleValue() > 0)){
            createDialog(getResources().getString(R.string.success_title), getResources().getString(R.string.success_message));
            countDownTimer.cancel();
            mCallback.updateUserCorrectCount();
            mCallback.disableMoneyButtons();

        }else if((bdUserChangeTotal != null && bdChangeToMake != null) && (bdUserChangeTotal.doubleValue() > bdChangeToMake.doubleValue()) && (bdChangeToMake.doubleValue() > 0)){
            createDialog(getResources().getString(R.string.too_much_title), getResources().getString(R.string.try_again_message));
            countDownTimer.cancel();
            mCallback.disableMoneyButtons();
        }
    }

    public void resetGame(){
        countDownTimer.cancel();
        countDownTimer.start();
        mCallback.enableMoneyButtons();
    }

    public void resetGameAndAmount(){
        generateRandomChange();
        resetGame();
        mCallback.enableMoneyButtons();

    }

    /**
     * helper method that creates a dialog box with the given title and message
     * @param title title to be in the dialog box
     * @param message message to be put in the dialog box
     */
    private void createDialog(String title, String message){
        Bundle messageBundle = new Bundle();
        messageBundle.putString("title", title);
        messageBundle.putString("message", message);

        ResultsDialogFragment dialogChangeActions = new ResultsDialogFragment();
        dialogChangeActions.setArguments(messageBundle);
        dialogChangeActions.setCancelable(false);
        dialogChangeActions.show(getActivity().getSupportFragmentManager(), "time_expired");

        //reset our user change to make total and out timer to 0 when dialog is called
        mCallback.setUserTotalZero();
        countDownTimer.onTick(0);
    }
}

