package com.example.cs3270a5;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultsDialogFragment extends DialogFragment {

    private String title;
    private String message;

    public ResultsDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        savedInstanceState = getArguments();
        title = savedInstanceState.getString("title", "Something Went wrong :(");
        message = savedInstanceState.getString("message", "Something Went Wrong :(");
        //builder allows us to get the options and get activity allows us to put it on top of all content
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO reset our labels and stuff
                    }
                });

        return builder.create();
    }
}
