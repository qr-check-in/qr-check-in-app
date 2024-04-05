package com.example.qrcheckin.Notifications;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcheckin.R;

import java.util.ArrayList;

public class DialogRecyclerView extends Dialog {
    private ArrayList<Notification> notifications = new ArrayList<>();
    private NotificationAdapter notificationsAdapter;
    private Context context;

    public DialogRecyclerView(@NonNull Context context, ArrayList<Notification> notifications){
        super(context);
        this.context = context;
        this.notifications = notifications;
    }

    /**
     * Method is called when Dialog is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState != null ? savedInstanceState : new Bundle());

        // Use the LayoutInflater to inflate the
        // dialog_list layout file into a View object
        View view = LayoutInflater.from(getContext()).inflate(R.layout.notification_dialog_layout, null);

        // Set the dialog's content view to the newly created View object
        setContentView(view);

        // Allow the dialog to be dismissed by touching outside of it
        setCanceledOnTouchOutside(true);

        // Allow the dialog to be canceled by pressing the back button
        setCancelable(true);

        // Set up the RecyclerView in the dialog
        setUpRecyclerView(view);

        // Adjust dialog dimensions to take up 50% of the screen
        adjustDialogDimensions();
    }

    /**
     * This method sets up the RecyclerView in the dialog
     *
     * @param view
     */
    private void setUpRecyclerView(View view) {
        // Find the RecyclerView in the layout file and set its layout manager to a LinearLayoutManager
        RecyclerView recyclerView = view.findViewById(R.id.notificationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create a new instance of the EmployeeAdapter and set it as the RecyclerView's adapter
        notificationsAdapter = new NotificationAdapter(getContext(), notifications);
        recyclerView.setAdapter(notificationsAdapter);
    }

    /**
     * This method adjusts the dialog dimensions to take up 50% of the screen
     */
    private void adjustDialogDimensions() {
        // Get the display metrics of the device
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        // Calculate the width and height of the dialog
        int width = (int) (displayMetrics.widthPixels * 0.95); // 95% of screen width
        int height = (int) (displayMetrics.heightPixels * 0.7); // 70% of screen height

        // Set the dialog's width and height
        getWindow().setLayout(width, height);
    }
}
