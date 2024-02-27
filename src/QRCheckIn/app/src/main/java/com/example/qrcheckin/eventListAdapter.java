package com.example.qrcheckin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Links the data stored in the array of Event objects to the RecyclerView in EventListView
 * https://developer.android.com/develop/ui/views/layout/recyclerview#next-steps , 2024
 */

public class eventListAdapter extends RecyclerView.Adapter<eventListAdapter.ViewHolder> {
    private ArrayList<Event> localDataSet;

    /**
     * Reference the views for an Event item
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView eventName;
        public ViewHolder(View view){
            super(view);
            // TODO: get other views for an Event item that are displayed by an eventlistitem
            eventName = (TextView) view.findViewById(R.id.event_name_text);
        }
        public TextView getEventNameView(){
            return eventName;
        }
    }

    /**
     * Initialize the dataset of the adapter
     * @param dataSet ArrayList of Events to display
     */
    public eventListAdapter(ArrayList<Event> dataSet){
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.eventlistitem, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getEventNameView().setText(localDataSet.get(position).getEventName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
