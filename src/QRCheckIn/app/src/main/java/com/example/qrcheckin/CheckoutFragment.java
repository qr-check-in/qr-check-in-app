package com.example.qrcheckin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.qrcheckin.R;

public class CheckoutFragment extends DialogFragment {

    // Interface for communication between the dialog fragment and its owner
    public interface CheckOutDialogListener {
        void onCheckOutConfirmed();
    }

    private CheckOutDialogListener listener;
    private String title;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CheckOutDialogListener) {
            listener = (CheckOutDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + "There is no implementation of CheckOutDialogListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_checkout, null);

        Bundle arguments = getArguments();

        if (arguments != null) {
            // Retrieve data with different data types from the bundle
            title = arguments.getString("title");
        }

        builder.setView(view)
                .setTitle("Confirm Checkout")
                .setMessage("Are you sure you want to CHECK OUT " + title + "?")
                .setNegativeButton("NO", null)
                .setPositiveButton("YES", (dialog, which) -> {
                    listener.onCheckOutConfirmed();
                });

        return builder.create();
    }
}
