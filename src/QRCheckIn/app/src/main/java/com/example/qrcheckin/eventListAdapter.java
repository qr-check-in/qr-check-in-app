package com.example.qrcheckin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Links the data stored in the array of Event objects to the RecyclerView in EventListView
 */

public class eventListAdapter extends RecyclerView.Adapter<eventListAdapter.ViewHolder> {
    private ArrayList<Event> localDataSet;
    // https://developer.android.com/develop/ui/views/layout/recyclerview#next-steps , 2024, Apache 2.0 license
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


    /**
     * Create new views (invoked by the layout manager)
     * @param viewGroup The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return ViewHolder with the created view
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.eventlistitem, viewGroup, false);

        return new ViewHolder(view);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getEventNameView().setText(localDataSet.get(position).getEventName());
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     * @return int the size of dataset
     */
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
