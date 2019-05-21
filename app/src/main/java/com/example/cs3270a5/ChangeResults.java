package com.example.cs3270a5;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Top fragment in our ui that also is responsible for our dialog
 */
public class ChangeResults extends Fragment {

    private View root;
    private Locale defaultLocale = Resources.getSystem().getConfiguration().locale;
    private NumberFormat numFormat = NumberFormat.getCurrencyInstance(defaultLocale);
    private Random randChange = new Random();
    private TextView txtCountdown, txtChangeToMake, txtUserChangeTotal;
    private DecimalFormat df = new DecimalFormat("##.#");   //format for the timer
    private DecimalFormat dfChangeToMake = new DecimalFormat("#.##");   //format for the money
    private BigDecimal bdChangeToMake, bdUserChangeTotal;
    private CountDownTimer countDownTimer;
    private double rangeMax;


    private UpdateCorrectCount mCallback;
    interface UpdateCorrectCount {
        void updateUserCorrectCount();
        int getRange();
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
        Log.d(TAG, "onPause: " + String.valueOf(bdChangeToMake.doubleValue()));
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
               createDialog("You took too long", "You should try again");
            }

        }.start();

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        String tempChange = sp.getString("changeToMake", "0.00");
        bdChangeToMake = new BigDecimal(dfChangeToMake.format(Double.parseDouble(tempChange)));
        Log.d(TAG, "onResume: " + tempChange);

        txtChangeToMake = (TextView) root.findViewById(R.id.txt_changeToMakeOutput);
        if(txtChangeToMake!=null){
            txtChangeToMake.setText(numFormat.format(bdChangeToMake.doubleValue()));
        }

    }

    public void generateRandomChange(){
        rangeMax = mCallback.getRange();
        double randomValue = 0 + (rangeMax - 0) * randChange.nextDouble();

        bdChangeToMake = new BigDecimal(dfChangeToMake.format(randomValue));

        Log.d(TAG, "generateRandomChange: " + rangeMax);
        txtChangeToMake = (TextView) root.findViewById(R.id.txt_changeToMakeOutput);
        if(txtChangeToMake != null){
            txtChangeToMake.setText(numFormat.format(bdChangeToMake.doubleValue()));
        }
    }

    public void updateUserChangeTotal(double userTotal){
        txtUserChangeTotal = (TextView) root.findViewById(R.id.txt_ChangeTotalSoFarOutput);
        bdUserChangeTotal = new BigDecimal(dfChangeToMake.format(userTotal));
        Log.d(TAG, "updateUserChangeTotal: " + dfChangeToMake.format(userTotal));

        if(txtUserChangeTotal != null){
            txtUserChangeTotal.setText(numFormat.format(bdUserChangeTotal.doubleValue()));
        }


        Log.d(TAG, "updateUserChangeTotal: bdUser" + bdUserChangeTotal.doubleValue());
        Log.d(TAG, "updateUserChangeTotal: bdChange" + bdChangeToMake.doubleValue());
        if((bdUserChangeTotal != null && bdChangeToMake != null) && bdUserChangeTotal.doubleValue() == bdChangeToMake.doubleValue()){

            createDialog("You did it!", "You were able to make the correct change");
            countDownTimer.cancel();
            mCallback.updateUserCorrectCount();

        }else if((bdUserChangeTotal != null && bdChangeToMake != null) && (bdUserChangeTotal.doubleValue() > bdChangeToMake.doubleValue())){
            createDialog("That's too much change.", "You should try again");
            countDownTimer.cancel();
        }
    }

    public void resetGame(){
        countDownTimer.cancel();
        countDownTimer.start();
    }

    public void resetGameAndAmount(){
        generateRandomChange();
        resetGame();
    }

    private void createDialog(String title, String message){
        Bundle messageBundle = new Bundle();
        messageBundle.putString("title", title);
        messageBundle.putString("message", message);

        ResultsDialogFragment dialogChangeActions = new ResultsDialogFragment();
        dialogChangeActions.setArguments(messageBundle);
        dialogChangeActions.setCancelable(false);
        dialogChangeActions.show(getActivity().getSupportFragmentManager(), "time_expired");

    }
}

