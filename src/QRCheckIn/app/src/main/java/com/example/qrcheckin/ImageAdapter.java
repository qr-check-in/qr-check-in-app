package com.example.qrcheckin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// ImageAdapter.java
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Map<Image, String> imageUriToFolderMap;
    private OnImageClickListener imageClickListener; // Define the interface

    private LayoutInflater inflater; // No longer storing this as a field if not needed elsewhere

    // Ensure the context passed here is not null
    public ImageAdapter(Context context, Map<Image, String> imageUriToFolderMap) {
        this.inflater = LayoutInflater.from(context);
        this.imageUriToFolderMap = imageUriToFolderMap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Using the inflater initialized with a non-null context
        View view = inflater.inflate(R.layout.image_list, parent, false); // Make sure the layout resource ID is correct
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image image = new ArrayList<>(imageUriToFolderMap.keySet()).get(position);
        String folderName = imageUriToFolderMap.get(image);

        ImageStorageManager manager = new ImageStorageManager(image, folderName);
        manager.displayImage(holder.imageView);
    }
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

    @Override
    public int getItemCount() {
        return imageUriToFolderMap.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view); // Make sure the ID matches
        }
    }

    // Ensure you update data and notify the adapter correctly
    public void updateData(Map<Image, String> newImageUriToFolderMap) {
        this.imageUriToFolderMap.clear();
        this.imageUriToFolderMap.putAll(newImageUriToFolderMap);
        notifyDataSetChanged();
    }
    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    /**
     * Used in the Event Page activity to set a listener for an adapter
     * @param listener The listener to set for an adapter
     */
    public void setOnImageClickListener(OnImageClickListener listener) {
        this.imageClickListener = listener;
    }


}