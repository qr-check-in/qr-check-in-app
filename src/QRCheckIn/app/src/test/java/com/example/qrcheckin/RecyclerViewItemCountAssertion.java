//package com.example.qrcheckin;
//
//import android.view.View;
//
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.test.espresso.NoMatchingViewException;
//import androidx.test.espresso.ViewAssertion;
//
//public class RecyclerViewItemCountAssertion implements ViewAssertion {
//    private final int expectedCount;
//    final int[] itemCount = new int[1];
//
//    public RecyclerViewItemCountAssertion(int expectedCount) {
//        this.expectedCount = expectedCount;
//    }
//
//    @Override
//    public void check(View view, NoMatchingViewException noViewFoundException) {
//        if (noViewFoundException != null) {
//            throw noViewFoundException;
//        }
//
//        RecyclerView recyclerView = (RecyclerView) view;
//        RecyclerView.Adapter adapter = recyclerView.getAdapter();
//        itemCount[0] = adapter.getItemCount();
//        androidx.test.espresso.matcher.ViewMatchers.assertThat(adapter.getItemCount(), org.hamcrest.Matchers.is(expectedCount));
//    }
//}
