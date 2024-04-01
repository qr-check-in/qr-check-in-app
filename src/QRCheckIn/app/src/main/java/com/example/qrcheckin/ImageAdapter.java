package com.example.qrcheckin;

import android.content.Context;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

// ImageAdapter.java
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<String> imageUrls;
    private LayoutInflater inflater;

    public ImageAdapter(Context context, List<String> imageUrls) {
        this.inflater = LayoutInflater.from(context);
        this.imageUrls = imageUrls;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.image_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imagePath = imageUrls.get(position);
        Log.d("imageUrls", "image is shown:"+ imagePath);
        Log.d("imageUrls", "List size: " + imageUrls.size());

        Picasso.get()
                .load(imagePath)
                .placeholder(R.drawable.imageresize) // Optional: Placeholder image
                .error(R.drawable.calenderresize) // Optional: Error image, use a different image for error
                .into(holder.imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        // Image successfully loaded
                        Log.d("Picasso", "Success loading image: " + imagePath);
                    }

                    @Override
                    public void onError(Exception e) {
                        // Image loading failed
                        Log.e("Picasso", "Error loading image: " + imagePath, e);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
    public void updateData(List<String> newImageUris) {
        imageUrls.clear();
        imageUrls.addAll(newImageUris);
        notifyDataSetChanged();
    }
}
