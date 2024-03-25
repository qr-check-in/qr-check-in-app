package com.example.qrcheckin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.bumptech.glide.Glide;

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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(com.example.qrcheckin.R.layout.profile_list_item, parent, false);
        return new ProfileViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile profile = profiles.get(position);
        holder.nameTextView.setText(profile.getName());
        Glide.with(holder.itemView.getContext())
                .load(profile.getProfilePicture())
                .placeholder(R.drawable.baseline_account_circle_24) // A default placeholder in case the load fails.
                .into(holder.profileImageView);
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public CircleImageView profileImageView;
        public ProfileViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textview_name);
            profileImageView = itemView.findViewById(R.id.profile_picture);
        }
    }
}
