package com.example.qrcheckin;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

/**
 * Syncs the recycler view displaying events with the firestore database
 */
public class EventAdapter extends FirestoreRecyclerAdapter<Event, EventAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options Options for the query which will lookup an event
     */
    public EventAdapter(@NonNull FirestoreRecyclerOptions<Event> options) {
        super(options);
    }

    /**
     * Holds the views where event data is sent
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvLocation;
        TextView tvDate;
        ImageView ivPoster;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.text_event_title);
            tvLocation = itemView.findViewById(R.id.text_event_location);
            tvDate = itemView.findViewById(R.id.text_event_date);
            ivPoster = itemView.findViewById(R.id.image_event_poster);
        }
    }

    /**
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return          ViewHolder with the created view
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     * @param holder    The ViewHolder which should be updated to represent the contents of the
     *                      item at the given position in the data set.
     * @param position  The position of the item within the adapter's data set.
     * @param model     The model object containing the data that should be used to populate the view.
     */
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Event model) {
        holder.tvTitle.setText(model.getEventName());
        holder.tvLocation.setText(model.getEventLocation());
        holder.tvDate.setText(model.getEventDate());
        Log.d("Firestore", String.format("event %s on %s happening on %s fetched", model.getEventName(), model.getEventDate(), model.getEventLocation()));
        //holder.ivPoster.setImageURI(model.getPoster().getImage());
        // TODO: Store images as URI files and store them in Image class. Image.getImage() should return a URI file
    }

}