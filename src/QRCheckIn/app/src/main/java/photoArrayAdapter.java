package com.example.qrcheckin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Class for ArrayAdapter that connects the photos_grid to the local list of ImageViews
 * extends ArrayAdapter
 */
public class photoArrayAdapter extends ArrayAdapter<ImageView> {
    private ArrayList<ImageView> photos;
    private Context context;

    /**Constructor for PhotoArrayAdapter
     *
     * @param context context object
     * @param photos list of ImageViews
     */
    public photoArrayAdapter(Context context, ArrayList<ImageView> photos)
    {
        super(context,0,photos);
        this.photos = photos;
        this.context=context;
    }

    /**
     * Returns the current view
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        photos.get(position).setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, 1000));

        return photos.get(position);
    }
}
