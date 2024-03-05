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

public class EventAdapter extends FirestoreRecyclerAdapter<Event, EventAdapter.EventHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options Options for the query which will lookup an event
     */
    public EventAdapter(@NonNull FirestoreRecyclerOptions<Event> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EventHolder holder, int position, @NonNull Event model) {
        holder.tvTitle.setText(model.getEventName());
        holder.tvLocation.setText(model.getEventLocation());
        holder.tvDate.setText(model.getEventDate());
        Log.d("Firestore", String.format("event %s on %s happening on %s fetched", model.getEventName(), model.getEventDate(), model.getEventLocation()));
        //holder.ivPoster.setImageURI(model.getPoster().getImage());
        // TODO: Store images as URI files and store them in Image class. Image.getImage() should return a URI file
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item,
                parent, false);
        return new EventHolder(view);
    }

    class EventHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvLocation;
        TextView tvDate;
        ImageView ivPoster;
        public EventHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.text_event_title);
            tvLocation = itemView.findViewById(R.id.text_event_location);
            tvDate = itemView.findViewById(R.id.text_event_date);
            ivPoster = itemView.findViewById(R.id.image_event_poster);
        }
    }

}
