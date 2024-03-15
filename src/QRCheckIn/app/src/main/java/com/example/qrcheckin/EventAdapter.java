package com.example.qrcheckin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Syncs the recycler view displaying events with the firestore database
 */
public class EventAdapter extends FirestoreRecyclerAdapter<Event, EventAdapter.ViewHolder> {
    private OnItemClickListener listener;

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
     * Creates a view holder for a new Event item
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
     * Displays the Event in the appropriate view in the view holder (invoked by the layout manager)
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

        // TODO: Store images as URI files and store them in Image class. Image.getImage() should return a URI file
    }

    /**
     * Holds the view where event data is sent
     */
    class ViewHolder extends RecyclerView.ViewHolder {
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

            // Set onclick listener to open event page when this event's view is clicked
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();

                    // In the event that you click an item that is in it's removal animation (no
                    // valid position)
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    /**
     * Sends a document snapshot to the Event Page activity
     */
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    /**
     * Used in the Event Page activity to set a listener for an adapter
     * @param listener The listener to set for an adapter
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
