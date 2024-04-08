package com.example.qrcheckin.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcheckin.Common.Image;
import com.example.qrcheckin.Common.ImageStorageManager;
import com.example.qrcheckin.R;

import java.util.ArrayList;
import java.util.Map;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Map<Image, String> imageUriToFolderMap;
    private OnImageClickListener imageClickListener;

    public ImageAdapter(Context context, Map<Image, String> imageUriToFolderMap) {
        this.imageUriToFolderMap = imageUriToFolderMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list, parent, false);
        return new ViewHolder(view, imageClickListener); // Pass the click listener to the ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image image = new ArrayList<>(imageUriToFolderMap.keySet()).get(position);
        String folderName = imageUriToFolderMap.get(image);

        // Assuming ImageStorageManager is correctly implemented to display the image
        ImageStorageManager manager = new ImageStorageManager(image, folderName);
        manager.displayImage(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUriToFolderMap.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(View itemView, final OnImageClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            int position = getBindingAdapterPosition();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        Image image = new ArrayList<>(imageUriToFolderMap.keySet()).get(position);
                        listener.onImageClick(image, position); // Pass the image and position
                    }
                }
            });
        }
    }

    public interface OnImageClickListener {
        void onImageClick(Image image, int position);
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.imageClickListener = listener;
    }

    // Method to update the adapter with new data
    public void updateData(Map<Image, String> newImageUriToFolderMap) {
        this.imageUriToFolderMap.clear();
        this.imageUriToFolderMap.putAll(newImageUriToFolderMap);
        notifyDataSetChanged();
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
        return position;
    }

}