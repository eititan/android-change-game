package com.example.cs3270a5;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeMaxAmount extends Fragment {

    private View root;
    private SeekBar seekBar; //seekBar used to control user input and change max change
    private Locale defaultLocale = Resources.getSystem().getConfiguration().locale;
    private NumberFormat numFormat = NumberFormat.getCurrencyInstance(defaultLocale);
    private TextView txtMaxAmount;
    private final int minRange = 1; //set the lowest we can go on our seekbar
    private BigDecimal bdMaxAmount;

    private DisplayMainView mCallback;

    interface DisplayMainView {
        void displayMainView();
    }

    public ChangeMaxAmount() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallback = (DisplayMainView) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement the DisplayMainView Interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_change_max_amount, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        //queuing for a commit
        spEdit.putInt("seekBar", seekBar.getProgress());
        //commit to disk
        spEdit.apply();
        //spEdit.commit; will save to disk immediately
    }

    @Override
    public void onResume() {
        super.onResume();

        Button btnSave = (Button) root.findViewById(R.id.btn_SaveMaxAmount);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.displayMainView();

            }
        });


        seekBar = (SeekBar)root.findViewById(R.id.sb_ChangeMaxAmount);
        seekBar.setMax(99);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
                bdMaxAmount = new BigDecimal(seekBar.getProgress()+1);
                if(txtMaxAmount != null){
                    txtMaxAmount.setText(numFormat.format(bdMaxAmount));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        seekBar.setProgress(sp.getInt("seekBar", 30));
        txtMaxAmount = (TextView) root.findViewById(R.id.txt_maxValue);

        bdMaxAmount = new BigDecimal(seekBar.getProgress()+ minRange);
        if(txtMaxAmount != null){
            txtMaxAmount.setText(numFormat.format(bdMaxAmount));
        }

    }

    public int getRange(){
        return seekBar.getProgress()+ minRange;
    }
}
