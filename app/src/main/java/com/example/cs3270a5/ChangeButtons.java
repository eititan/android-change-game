package com.example.cs3270a5;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * implements an OnClickListener which allows us to only have 1 listener for the 10 buttons
 * in this fragment
 */
public class ChangeButtons extends Fragment implements View.OnClickListener {

    private View root;
    private Button dollar50, dollar20, dollar10, dollar5, dollar1,
                    cent50, cent25, cent10, cent5, cent1;
    private double userTotal;

    //interface
    private UpdateValues mCallback;
    interface UpdateValues {
        void updateChange(double userTotal);
    }


    public ChangeButtons() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallback = (UpdateValues) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement the UpdateValues Interface");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        //queuing for a commit
        spEdit.putLong("userTotal", (long) userTotal);
        //commit to disk
        spEdit.apply();
        //spEdit.commit; will save to disk immediately
    }

    @Override
    public void onResume() {
        super.onResume();

        //reinitializing all buttons and listeners for those buttons
        dollar50 = (Button) root.findViewById(R.id.btn_50_dollar);
        dollar20 = (Button) root.findViewById(R.id.btn_20_dollar);
        dollar10 = (Button) root.findViewById(R.id.btn_10_dollar);
        dollar5 = (Button) root.findViewById(R.id.btn_5_dollar);
        dollar1 = (Button) root.findViewById(R.id.btn_1_dollar);
        cent50 = (Button) root.findViewById(R.id.btn_50);
        cent25 = (Button) root.findViewById(R.id.btn_25);
        cent10 = (Button) root.findViewById(R.id.btn_10);
        cent5 = (Button) root.findViewById(R.id.btn_05);
        cent1 = (Button) root.findViewById(R.id.btn_01);

        dollar50.setOnClickListener(this);
        dollar20.setOnClickListener(this);
        dollar10.setOnClickListener(this);
        dollar5.setOnClickListener(this);
        dollar1.setOnClickListener(this);
        cent50.setOnClickListener(this);
        cent25.setOnClickListener(this);
        cent10.setOnClickListener(this);
        cent5.setOnClickListener(this);
        cent1.setOnClickListener(this);

        //getting our stored value
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        userTotal = (double) sp.getLong("userTotal", 0L);
        mCallback.updateChange(userTotal);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return  root = inflater.inflate(R.layout.fragment_change_buttons, container, false);

    }

    /**
     * interface class that allows us to have 1 listener for 10 buttons
     * @param v the button that was just clicked
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_50_dollar:
                userTotal += 50;
                break;

            case R.id.btn_20_dollar:
                userTotal += 20;
                break;

            case R.id.btn_10_dollar:
                userTotal += 10;
                break;

            case R.id.btn_5_dollar:
                userTotal += 5;
                break;

            case R.id.btn_1_dollar:
                userTotal += 1;
                break;

            case R.id.btn_50:
                userTotal += 0.50;
                break;

            case R.id.btn_25:
                userTotal += 0.25;
                break;

            case R.id.btn_10:
                userTotal += 0.10;
                break;

            case R.id.btn_05:
                userTotal += 0.05;
                break;

            case R.id.btn_01:
                userTotal += 0.01;
                break;

            default:
                break;
        }

        //make a call to main activity to pass that value into ChangeResults
        mCallback.updateChange(userTotal);
    }

    public void resetUserTotal(){
        userTotal = 0;
        mCallback.updateChange(userTotal);
    }

    /**
     * disables all buttons in this fragment
     */
    public void disableAllMoneyButtons(){
        dollar50.setEnabled(false);
        dollar20.setEnabled(false);
        dollar10.setEnabled(false);
        dollar5.setEnabled(false);
        dollar1.setEnabled(false);
        cent50.setEnabled(false);
        cent25.setEnabled(false);
        cent10.setEnabled(false);
        cent5.setEnabled(false);
        cent1.setEnabled(false);
    }

    /**
     * enables all buttons in this fragment
     */
    public void enableAllMoneyButtons(){
        dollar50.setEnabled(true);
        dollar20.setEnabled(true);
        dollar10.setEnabled(true);
        dollar5.setEnabled(true);
        dollar1.setEnabled(true);
        cent50.setEnabled(true);
        cent25.setEnabled(true);
        cent10.setEnabled(true);
        cent5.setEnabled(true);
        cent1.setEnabled(true);
    }
}
