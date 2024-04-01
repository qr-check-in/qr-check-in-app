package com.example.qrcheckin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class AdminEventAdapter extends FirestoreRecyclerAdapter<Event, AdminEventAdapter.EventViewHolder> {

    private OnItemClickListener listener;

    public AdminEventAdapter(@NonNull FirestoreRecyclerOptions<Event> options) {
        super(options);

    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_events_list, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull Event model) {
        holder.eventNameTextView.setText(model.getEventName());
        holder.eventLocationTextView.setText(model.getEventLocation());
        holder.eventTimeTextView.setText(model.getEventDate());
        // Assuming you have a method to set the image on the ImageView
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView, eventLocationTextView, eventTimeTextView;
        ImageView qrCodeImageView;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.eventNameTextView);
            eventLocationTextView = itemView.findViewById(R.id.eventLocationTextView);
            eventTimeTextView = itemView.findViewById(R.id.eventTimeTextView);
            qrCodeImageView = itemView.findViewById(R.id.qrCodeImageView);

            itemView.setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
