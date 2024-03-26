package com.example.qrcheckin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private List<Profile> profiles;
    public interface OnItemClickListener {
        void onItemClick(Profile profile);
    }

    private OnItemClickListener listener;

    public ProfileAdapter(List<Profile> profiles, OnItemClickListener onItemClickListener) {
        this.profiles = profiles;
        this.listener = onItemClickListener; // This line was corrected
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list_item, parent, false);
        return new ProfileViewHolder(itemView, listener); // Pass the listener here
    }


    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile profile = profiles.get(position);
        holder.nameTextView.setText(profile.getName());
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ProfileActivityAdmin.class);
            intent.putExtra("profileName", profile.getName()); // Example of passing the profile name
            context.startActivity(intent);
        });
        // Assuming getProfilePicture() returns a valid, accessible URI as a String
        if(profile.getProfilePicture() != null && profile.getProfilePicture()!=null) {
            Glide.with(holder.itemView.getContext())
                    .load(profile.getProfilePicture())
                    .placeholder(R.drawable.baseline_account_circle_24)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("ProfileAdapter", "Image load failed", e);
                            return false; // Important to return false so the error placeholder can be placed
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.profileImageView);
        } else {
            // Load a default image if no profile picture URI is available
            holder.profileImageView.setImageResource(R.drawable.baseline_account_circle_24);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(profile);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        private List<Profile> profiles;
        private OnItemClickListener listener;
        public CircleImageView profileImageView;
        public ProfileViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textview_name);
            profileImageView = itemView.findViewById(R.id.profile_picture);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the position of the ViewHolder
                    int position = getAdapterPosition();
                    // Check if position is valid and listener is not null
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(profiles.get(position));
                    }
                }
            });
        }

    }
}
