package com.example.qrcheckin.Attendee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcheckin.Common.ImageStorageManager;
import com.example.qrcheckin.Event.EventAdapter;
import com.example.qrcheckin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Locale;
import java.util.function.Function;

/**
 * Syncs a recycler view displaying a list of attendee profiles with firestore database
 */
public class AttendeeAdapter extends FirestoreRecyclerAdapter<Attendee, AttendeeAdapter.AttendeeViewHolder> {
    private EventAdapter.OnItemClickListener listener;
    private final Function<Integer, Boolean> viewTypeDeterminer;
    private final String eventDocId;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options               The FirestoreRecyclerOptions<Attendee> options for the query
     *                                  that returns the desired attendees from Firestore
     * @param viewTypeDeterminer    A lambda function that returns 0 if you want attendee signups
     *                                  and returns 1 if you want attendee check-ins
     */
    public AttendeeAdapter(@NonNull FirestoreRecyclerOptions<Attendee> options,
                           String eventDocId,
                           Function<Integer, Boolean> viewTypeDeterminer) {
        super(options);
        this.viewTypeDeterminer = viewTypeDeterminer;
        this.eventDocId = eventDocId;

    }

    /**
     * Creates a view holder for a new Attendee item
     * @param parent    The ViewGroup into which the new View will be added after it is bound to
     *                      an adapter position.
     * @param viewType  The view type of the new View.
     * @return          ViewHolder with the created view
     */
    @NonNull
    @Override
    public AttendeeAdapter.AttendeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_attendee, parent, false);
            return new AttendeeViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_attendee_check_ins, parent, false);
            return new AttendeeCheckInViewHolder(view);
        }
    }

    /**
     * Displays the Attendee in the appropriate view in the view holder (invoked by the layout manager)
     * @param holder    The ViewHolder which should be updated to represent the contents of the
     *                      item at the given position in the data set.
     * @param position  The position of the item within the adapter's data set.
     * @param model     The model object containing the data that should be used to populate the view.
     */
    @Override
    protected void onBindViewHolder(@NonNull AttendeeAdapter.AttendeeViewHolder holder, int position, @NonNull Attendee model) {
        int viewType = getItemViewType(position);
        if (viewType == 0) {
            ((AttendeeViewHolder) holder).bind(model, eventDocId);
        } else {
            ((AttendeeCheckInViewHolder) holder).bind(model, eventDocId);
        }

    }

    // https://stackoverflow.com/questions/36712704/why-is-my-item-image-in-custom-recyclerview-changing-while-scrolling, Fathima km, 2017
    // Override getItemId and getItemViewType methods to prevent flickering of images while scrolling through the recycler view
    /**
     * Returns the position as the stable item ID
     * @param position Adapter position to query
     * @return position Int of the item's positon
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Returns the position as the view type of the item
     * @param position position to query
     * @return position Int of the item's position
     */
    @Override
    public int getItemViewType(int position) {
        int viewType = viewTypeDeterminer.apply(position) ? 1 : 0;
        return viewType;
    }

    /**
     * Holds the view where attendee data is sent, does not include the check-in field
     */
    class AttendeeViewHolder extends RecyclerView.ViewHolder {
        //TextView tvCheckIns;
        TextView tvName;
        ImageView ivProfilePic;
        public AttendeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textViewAttendeeName);
            ivProfilePic = itemView.findViewById(R.id.imageViewAttendeeIcon);

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

        /**
         * Displays the Attendee in the appropriate view in the view holder. Called by
         *      onBindViewHolder()
         * @param model The Attendee class instance
         */
        public void bind(Attendee model, String eventDocId) {
            tvName.setText(model.getProfile().getName());
            if (model.getProfile().getProfilePicture() != null) {
                ImageStorageManager storage = new ImageStorageManager(model.getProfile().getProfilePicture(), "/ProfilePictures");
                storage.displayImage(ivProfilePic);
            }
        }
    }

    /**
     * Holds the view where attendee data is sent, includes the check-in field
     */
    class AttendeeCheckInViewHolder extends AttendeeViewHolder {
        TextView tvCheckIns;
        public AttendeeCheckInViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCheckIns = itemView.findViewById(R.id.textViewCheckInCount);
        }

        /**
         * Overrides the bind method to also display the attendee profile picture
         * @param model The Attendee class instance
         */
        @Override
        public void bind(Attendee model, String eventDocId) {
            // Count the number of times the attendee checked into this event
            HashMap<String, Integer> checkedInEvents = model.getAttendedEvents();
            Integer count = checkedInEvents.getOrDefault(eventDocId, 1);
            int checkInCount = (count != null) ? count : 1;

            tvName.setText(model.getProfile().getName());
            String checkInString = String.format(Locale.CANADA, "%d", checkInCount);

            tvCheckIns.setText(checkInString);
            if (model.getProfile().getProfilePicture() != null) {
                ImageStorageManager storage = new ImageStorageManager(model.getProfile().getProfilePicture(), "/ProfilePictures");
                storage.displayImage(ivProfilePic);
            }
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
    public void setOnItemClickListener(EventAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
