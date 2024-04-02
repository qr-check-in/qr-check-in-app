package com.example.qrcheckin.Common;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Overrides the default LinearLayoutManager for recycler views to disable some features that
 *      cause bugs when used with FirestoreRecyclerAdapter (instead of the regular RecyclerAdapter)
 */
public class LinearLayoutManagerWrapper extends LinearLayoutManager {
    // Set default constructors
    public LinearLayoutManagerWrapper(Context context) {
        super(context);
    }

    public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Disables predictive animations as they are bugged when used with FirestoreRecyclerAdapter
     * @return  Boolean to disable/enable predictive item animations
     */
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    /**
     * Catch index out of bound errors resulting from firestore queries that return nothing
     * @param recycler Recycler to use for fetching potentially cached views for a
     *                 position
     * @param state    Transient state of RecyclerView
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("LinearLayoutManagerWrapper", "IndexOutOfBound in RecyclerView");
        }
    }
}
