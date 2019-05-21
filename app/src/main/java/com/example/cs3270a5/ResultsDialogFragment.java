package com.example.cs3270a5;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * extends from dialogfragment
 */
public class ResultsDialogFragment extends DialogFragment {

    private String title;
    private String message;

    private ResetChangeAfterDialog mCallback;
    interface ResetChangeAfterDialog{
        void resetChangeAfterDialog();
    }
    public ResultsDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallback = (ResetChangeAfterDialog) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement the ResetChangeAfterDialog Interface");
        }
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    /**
     * Dialog from DialogFragment inheritance.
     * Allows for multiple dialog that are similar to be created through one object
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        savedInstanceState = getArguments();
        title = savedInstanceState.getString("title", getResources().getString(R.string.something_went_wrong));
        message = savedInstanceState.getString("message", getResources().getString(R.string.something_went_wrong));
        //builder allows us to get the options and get activity allows us to put it on top of all content
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(getResources().getString(R.string.dialog_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCallback.resetChangeAfterDialog();
                    }
                });

        return builder.create();
    }
}
