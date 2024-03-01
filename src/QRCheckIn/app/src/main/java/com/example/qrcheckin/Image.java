package com.example.qrcheckin;

import java.io.File;

public class Image {
    private File imageFile;
    private Attendee uploader;

    public Image(File imageFile, Attendee uploader) {
        this.imageFile = imageFile;
        this.uploader = uploader;
    }

    public File getImageFile() {
        return imageFile;
    }

    public Attendee getUploader() {
        return uploader;
    }
    public void deleteImage() {
    }
}
