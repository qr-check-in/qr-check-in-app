package com.example.qrcheckin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcheckin.Event;
import com.example.qrcheckin.R;
import java.util.List;

public class AdminEventAdapter extends RecyclerView.Adapter<AdminEventAdapter.EventViewHolder> {
    private List<com.example.qrcheckin.Event> events;

    // Constructor
    public AdminEventAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_events_list, parent, false);
        return new EventViewHolder(view);
    }
    public void updateEvents(List<Event> events) {
        this.events.clear();
        this.events.addAll(events);
        notifyDataSetChanged(); // Refresh the RecyclerView
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventNameTextView.setText(event.getEventName());
        holder.eventLocationTextView.setText(event.getEventLocation());
        holder.eventTimeTextView.setText(event.getEventTime());
        // Set other fields as necessary
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView, eventLocationTextView, eventTimeTextView;
        ImageView qrCodeImageView, deleteImageView;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.eventNameTextView);
            eventLocationTextView = itemView.findViewById(R.id.eventLocationTextView);
            eventTimeTextView = itemView.findViewById(R.id.eventTimeTextView);
            qrCodeImageView = itemView.findViewById(R.id.qrCodeImageView);
            //deleteImageView = itemView.findViewById(R.id.deleteImageView); // Assuming you have a delete icon and want to use it for something.
        }
    }
}
