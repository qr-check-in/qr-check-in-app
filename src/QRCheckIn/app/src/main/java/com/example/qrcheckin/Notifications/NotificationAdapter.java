// https://www.geeksforgeeks.org/how-to-implement-recylerview-inside-a-dialogue-window/, 2024, how to implement a recyclerview inside a dialog
package com.example.qrcheckin.Notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcheckin.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<Notification> notificationList;

    public NotificationAdapter(Context context, ArrayList<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @Override
    public RecyclerView.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // Use the LayoutInflater to
        // create a new view from the
        // item_employee layout file
        View view = LayoutInflater.from(context).inflate(
                R.layout.notification_layout, parent, false);
        // Return a new ViewHolder that
        // holds the newly created view
        return new MyViewHolder(view);
    }

    /**
     * Method is called when the RecyclerView needs to display the data at a certain position
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void
    onBindViewHolder(RecyclerView.ViewHolder holder,
                     int position)
    {
        // Get the item at the specified position from the list
        Notification notification = notificationList.get(position);

        // Check if the ViewHolder is of type MyViewHolder
        if (holder instanceof MyViewHolder) {
            // If it is, set the name and email TextViews to the corresponding values in the item
            ((MyViewHolder)holder).notificationTitle.setText(notification.getTitle());
            ((MyViewHolder)holder).notificationDescription.setText(notification.getMessage());
            ((MyViewHolder)holder).notificationDateTime.setText(notification.getDateTime());
        }
    }

    @Override public int getItemCount()
    {
        return notificationList.size();
    }

    /**
     * Define a ViewHolder class that holds references to the TextViews in the notification_layout layout file
     */
    private static class MyViewHolder
            extends RecyclerView.ViewHolder {
        TextView notificationTitle;
        TextView notificationDescription;
        TextView notificationDateTime;
        ImageView eventPoster;

        MyViewHolder(View view)
        {
            super(view);
            notificationTitle = view.findViewById(R.id.notificationTitleText);
            notificationDescription = view.findViewById(R.id.notificationDescriptionText);
            notificationDateTime = view.findViewById(R.id.notificationTimeText);
            eventPoster = view.findViewById(R.id.eventPoster);
        }
    }
}
