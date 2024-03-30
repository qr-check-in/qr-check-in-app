package com.example.qrcheckin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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
                R.layout.item_employee, parent, false);
        // Return a new ViewHolder that
        // holds the newly created view
        return new MyViewHolder(view);
    }

    @Override
    public void
    onBindViewHolder(RecyclerView.ViewHolder holder,
                     int position)
    {
        // Get the item at the specified position from the
        // list
        Employee item = list.get(position);

        // Check if the ViewHolder is of type MyViewHolder
        if (holder instanceof MyViewHolder) {
            // If it is, set the name and email TextViews
            // to the corresponding values in the item
            ((MyViewHolder)holder)
                    .name.setText(item.getName());
            ((MyViewHolder)holder)
                    .email.setText(item.getEmail());
        }
    }

    @Override public int getItemCount()
    {
        return list.size();
    }

    // Define a ViewHolder class that holds references
    // to the TextViews in the item_employee layout file
    private static class MyViewHolder
            extends RecyclerView.ViewHolder {
        TextView name;
        TextView email;

        MyViewHolder(View view)
        {
            super(view);
            name = view.findViewById(R.id.tvName);
            email = view.findViewById(R.id.tvEmail);
        }
    }
}
