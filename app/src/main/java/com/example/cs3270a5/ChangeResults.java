package com.example.cs3270a5;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 * Top fragment in our ui that also is responsible for our dialog
 */
public class ChangeResults extends Fragment {

    private View root;
    private TextView txtCountdown;
    private DecimalFormat df = new DecimalFormat("##.#"); //import java.text.DecimalFormat;
    private String timeLeft;

    public ChangeResults() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_change_results, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        txtCountdown = (TextView) root.findViewById(R.id.txt_countdown);

        new CountDownTimer(5000, 10){

            @Override
            public void onTick(long millisUntilFinished) {

                timeLeft = df.format(millisUntilFinished/1000.00);
                txtCountdown.setText(timeLeft);

            }

            @Override
            public void onFinish() {
                ResultsDialogFragment dialogChangeActions = new ResultsDialogFragment();
                dialogChangeActions.setCancelable(false);
                dialogChangeActions.show(getActivity().getSupportFragmentManager(), "time_expired");
            }

        }.start();
    }
}
