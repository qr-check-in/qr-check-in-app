package com.example.qrcheckin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditProfilefragment extends DialogFragment {

    // Interface for communication between the dialog fragment and its owner
    public interface EditProfileDialogListener {
        void editDetails(String nameUpdated, String contactUpdated, String homepageUpdated);
    }

    EditProfileDialogListener listener;
    private String name;
    private String contact;
    private String homepage;
    private Boolean emptyProfile = false;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof EditProfileDialogListener) {
            listener = (EditProfileDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + "There is no implementation of EditProfileDialogListener in EditProfile");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_profile_fragment, null);
        TextView personName = view.findViewById(R.id.edit_name);
        TextView personContact = view.findViewById(R.id.edit_contact);
        TextView personHomepage = view.findViewById(R.id.edit_homepage);


        Bundle arguments = getArguments();

        if (arguments != null) {
            // Retrieve data with different data types from the bundle
            name = arguments.getString("name");
            contact = arguments.getString("contact");
            homepage = arguments.getString("homepage");

        }

        personName.setText(name);
        personContact.setText(contact);
        personHomepage.setText(homepage);

        return builder
                .setView(view)
                .setTitle("Edit Profile")
                .setNegativeButton("CANCEL", null)
                .setPositiveButton("EDIT", (dialog, which) -> {
                    String nameUpdated = personName.getText().toString();
                    String contactUpdated = personContact.getText().toString();
                    String homepageUpdated = personHomepage.getText().toString();

                    listener.editDetails(nameUpdated, contactUpdated, homepageUpdated);
                })
                .create();
    }
}