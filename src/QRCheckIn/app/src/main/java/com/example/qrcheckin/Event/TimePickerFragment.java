package com.example.qrcheckin.Event;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.qrcheckin.R;

import java.util.Calendar;

/**
 * Fragment shows a time picker, allows user to select a time
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    /**
     * Listner to call methods in createNewEventScreen1
     */
    interface TimePickerDialogListner{
        void buildTime(int hr, int min);
    }


    private TimePickerDialogListner listener;

    /**
     * assign the context to the listener
     * @param context the context of createNewEventScreen1
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof TimePickerDialogListner){
            listener = (TimePickerDialogListner) context;
        }
        else{
            throw new RuntimeException(context + " must implement TimePickerDialogListner");
        }
    }

    /**
     * Gets the integers representing a time
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return
     *      TimePickerDialog with the input time
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker.
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it.
        return new TimePickerDialog(getActivity(), R.style.CustomTimePickerStyle, this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    /**
     * Calls buildTime to create a fomrated string of the time
     * @param view the view associated with this listener
     * @param hourOfDay the hour that was set
     * @param minute the minute that was set
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time the user picks.
        listener.buildTime(hourOfDay, minute);
    }
}
