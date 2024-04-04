package com.example.qrcheckin.Common;

import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
/**
 * ViewModel class, manage UI-related data in a lifecycle.
 */
public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Uri> selectedImageUri = new MutableLiveData<>();
    /**
     * Updates URI of the selected image.
     * Allows UI to call setSelectedImageUri when an image is selected.
     *
     * @param uri The new URI of the selected image.
     */
    public void setSelectedImageUri(Uri uri) {
        selectedImageUri.setValue(uri);
    }
    /**
     * Returns the LiveData object of the selected image URI.
     * UI observes changes to the image URI.
     *
     * @return LiveData object containing the URI of the selected image.
     */
    public LiveData<Uri> getSelectedImageUri() {
        return selectedImageUri;
    }
}
