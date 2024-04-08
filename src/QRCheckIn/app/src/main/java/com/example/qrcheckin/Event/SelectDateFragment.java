package com.example.qrcheckin.Event;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.qrcheckin.R;

import java.util.Calendar;

/**
 * Fragment shows a DatePicker, allowing user to input a date for an Event
 */
public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    // Create listener to call methods in createNewEventScreen1
    interface DatePickerDialogListener{
        //public void onDateSet(DatePicker view, int year, int month, int dayOfMonth);
        void buildDate(int year, int month, int dayOfMonth);
    }
    private DatePickerDialogListener listener;

    /**
     * assign the context to the listener
     * @param context the context of createNewEventScreen1
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof DatePickerDialogListener){
            listener = (DatePickerDialogListener) context;
        }
        else{
            throw new RuntimeException(context + " must implement DatePickerFragmentListener");
        }
    }
    // https://developer.android.com/develop/ui/views/components/pickers#java , 2024, Apache 2.0 license
    /**
     * Gets the integers representing a date
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return DatePickerDialog with the input date
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(requireContext(), R.style.CustomDatePickerStyle,this, year, month, day);
    }

    /**
     * Calls buildDate to create a formatted string once a date has been entered in the DatePicker
     * @param view the picker associated with the dialog
     * @param year the selected year
     * @param month the selected month (0-11 for compatibility with
     *              {@link Calendar#MONTH})
     * @param dayOfMonth the selected day of the month (1-31, depending on
     *                   month)
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        listener.buildDate(year, month, dayOfMonth);
    }
}
