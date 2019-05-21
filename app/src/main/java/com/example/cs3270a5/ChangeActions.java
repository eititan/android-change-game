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
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeActions extends Fragment {

    private View root;
    private Button btnStartOver, btnNewAmount;
    private TextView txtChangeCorrect;
    private int userCorrectCount = 0;

    private RestartGame mCallback;
    interface RestartGame {
        void resetGame();
        void resetGameAndAmount();
    }

    public ChangeActions() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallback = (RestartGame) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement the RestartGame Interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_change_actions, container, false);
    }


    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        //queuing for a commit
        spEdit.putInt("userWins", userCorrectCount);
        //apply to disk
        spEdit.apply();
    }

    @Override
    public void onResume() {
        super.onResume();

        btnStartOver = (Button) root.findViewById(R.id.btnStartOver);
        btnStartOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.resetGame();
            }
        });

        btnNewAmount = (Button) root.findViewById(R.id.btnNewAmount);
        btnNewAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.resetGameAndAmount();
            }
        });

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        userCorrectCount = sp.getInt("userWins", 0);
        setUserCorrectCount();

    }

    public void updateUserCorrectNum(){
        userCorrectCount++;
        setUserCorrectCount();
    }

    public void resetUserCorrectNum(){
        userCorrectCount = 0;
        setUserCorrectCount();
    }

    private void setUserCorrectCount(){
        txtChangeCorrect = (TextView) root.findViewById(R.id.txt_correctChangeCount);
        if(txtChangeCorrect != null){
            txtChangeCorrect.setText(userCorrectCount + "");
        }
    }
}
