package com.example.qrcheckin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class eventListAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;

    public eventListAdapter(@NonNull Context context, int resource, ArrayList<Event> events, Context context1) {
        super(context, resource);
        this.events = events;
        this.context = context1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.eventlistitem, parent, false);
        }
        return view;
    }
}
