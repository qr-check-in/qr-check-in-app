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
/**
 * Confirming the checkout process.
 * Manages a dialog that prompts the user for confirmation before proceeding with checkout.
 * Must implement the CheckOutDialogListener interface to handle user actions, otherwise runtime exception.
 */
public class CheckoutFragment extends DialogFragment {

    /**
     * Interface for communication between the dialog fragment and its hosting activity.
     * Allows the fragment to communicate checkout confirmation back to the activity.
     */
    public interface CheckOutDialogListener {
        /**
         * Callback method invoked when checkout is confirmed by the user.
         */
        void onCheckOutConfirmed();
    }

    private CheckOutDialogListener listener;
    private String title;

    /**
     * Attaches the fragment to its context, ensuring the hosting activity implements the required interface.
     *
     * @param context The context to which this fragment is being attached.
     * @throws RuntimeException If the hosting activity does not implement CheckOutDialogListener.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CheckOutDialogListener) {
            listener = (CheckOutDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement CheckOutDialogListener");
        }
    }

    /**
     * Creates the checkout confirmation dialog.
     * The dialog presents a message to the user and offers a choice to confirm or cancel the checkout process.
     *
     * @param savedInstanceState If non-null, this dialog fragment is being re-constructed from a previous saved state.
     * @return A new Dialog instance for confirming checkout.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_checkout, null);

        Bundle arguments = getArguments();
        if (arguments != null) {
            title = arguments.getString("title");
        }

        builder.setView(view)
                .setTitle("Confirm Checkout")
                .setMessage("Are you sure you want to CHECKOUT " + title + "?")
                .setNegativeButton("NO", null)
                .setPositiveButton("YES", (dialog, which) -> listener.onCheckOutConfirmed());

        return builder.create();
    }
}
