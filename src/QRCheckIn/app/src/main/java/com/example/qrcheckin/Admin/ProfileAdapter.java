package com.example.qrcheckin.Admin;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qrcheckin.Attendee.Profile;
import com.example.qrcheckin.Attendee.ProfilePicture;
import com.example.qrcheckin.Common.ImageStorageManager;
import com.example.qrcheckin.R;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private List<Profile> profileList;
    private static OnItemClickListener listener;

    // Constructor
    public ProfileAdapter(List<Profile> profileList) {
        this.profileList = profileList;
        //this.listener = listener;
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public ImageView photoImageView;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textview_name);
            photoImageView = itemView.findViewById(R.id.profile_picture);
            // Initialize other views
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            });
        }
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list_item, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile profile = profileList.get(position);
        Log.d("ProfileAdapter", "Binding profile: " + profile.getName());
        holder.nameTextView.setText(profile.getName());
        ProfilePicture pic = profile.getProfilePicture();
        if (pic != null) {
            ImageStorageManager storage = new ImageStorageManager(pic, "/ProfilePictures");
            storage.displayImage(holder.photoImageView);}

    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    // Update method to refresh the list
    public void updateProfiles(List<Profile> profiles, List<String> documentIds) {
        profileList.clear();
        profileList.addAll(profiles);
        documentIds.clear();
        documentIds.addAll(documentIds);
        notifyDataSetChanged();
    }
//    public interface OnItemClickListener {
//        void onItemClick(DocumentSnapshot documentSnapshot, int position);
//    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}