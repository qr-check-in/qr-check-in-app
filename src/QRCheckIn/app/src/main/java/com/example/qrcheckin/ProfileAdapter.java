package com.example.qrcheckin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.qrcheckin.Profile;
import com.example.qrcheckin.R;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private List<Profile> profiles;

    public ProfileAdapter(List<Profile> profiles) {
        this.profiles = profiles;
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
        // You can set more attributes here
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textview_name);
            // Initialize other views here
        }
    }
}
