package com.example.qrcheckin.Attendee;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.widget.ImageView;

import com.example.qrcheckin.Common.ImageStorageManager;
import com.example.qrcheckin.Attendee.ProfilePicture;

import com.google.firebase.firestore.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Profile {
    private Boolean trackGeolocation;
    private String name;
    private String homepage;
    private String contact;
    private ProfilePicture profilePicture;
    private GeoPoint location;
    /**
     * Constructs a Profile for an Attendee
     */
    public Profile(){
        // default geolocation sharing permission set to false
        this.trackGeolocation = false;

        // TODO: randomize initial names in some way so that more unique profile pictures are generated.
        this.name = "New User";
    }

    /**
     * Creates a ProfilePicture of the user's name's initials and sets it as this Profile's profilePicture
     * @param imageView ImageView for cases where a profile picture has been removed, we need to re-call displayImage() once
     *                  the new profile picture has been successfully uploaded. Null in other cases.
     */
    public void generateProfilePicture(ImageView imageView){
        String initials = getInitials();
        // Create a bitmap canvas, set the background color
        // https://stackoverflow.com/questions/33522701/how-to-create-bitmap-from-string-text-inside-a-circle-using-canvas , dungtv, 2015
        int size = 180;
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint background = new Paint();
        background.setColor(Color.LTGRAY);
        canvas.drawRect(0F, 0F, (float) size, (float) size, background);

        // Draw the user's initials if their name is not empty
        if(!Objects.equals(initials, "")){
            Paint textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(72);
            textPaint.setTextScaleX(1);

            // Center the text in the canvas
            // https://stackoverflow.com/questions/11120392/android-center-text-on-canvas , Arun George, 2012
            textPaint.setTextAlign(Paint.Align.CENTER);
            int x = (canvas.getWidth() / 2);
            int y = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;

            canvas.drawText(initials, x, y, textPaint);
        }
        try{
            // Convert bitmap to uri using a temporary file
            // https://stackoverflow.com/questions/8295773/how-can-i-transform-a-bitmap-into-a-uri , Uzzam Altaf, 2022
            // OpenAi, ChatGPT, convert this code from kotlin to java: *kotlin code from stackoverflow answer above*
            final File localFile = File.createTempFile("tempPic", ".jpg");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            byte[] bitmapData = bytes.toByteArray();

            FileOutputStream fileOutputStream = new FileOutputStream(localFile);
            fileOutputStream.write(bitmapData);
            fileOutputStream.flush();
            fileOutputStream.close();
            Uri uri = Uri.fromFile(localFile);

            // Create and store the new ProfilePicture using the uri
            this.profilePicture = new ProfilePicture(uri.toString(), null);
            ImageStorageManager storage = new ImageStorageManager(profilePicture, "/ProfilePictures");
            storage.uploadImage(imageView);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Gets the initials of the user's name
     * @return String of the user's initials
     */
    public String getInitials() {
        // if the name is empty, returns the name (no initials)
        if (Objects.equals(name, "")) {
            return name;
        }
        String[] splitName = name.split(" ");
        StringBuilder builder = new StringBuilder();
        // Appends the first initial
        if (splitName.length >= 1) {
            builder.append(splitName[0].charAt(0));
        }
        // If the user has 2 or more words in their name, appends the initial of the second word
        // (words past the first 2 are ignored)
        if (splitName.length >= 2) {
            builder.append(splitName[1].charAt(0));
        }
        return builder.toString();
    }



    /**
     * Returns the ProfilePicture of the Profile
     * @return the ProfilePicture of the Profile
     */
    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    /**
     * Sets the ProfilePicture of the Profile
     * @param profilePicture the ProfilePicture of the Profile
     */
    public void setProfilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * Returns the Location of the Profile
     * @return the Location of the Profile
     */
    public GeoPoint getLocation() {
        return location;
    }

    /**
     * Sets the Location of the Profile
     * @param location the Location of the Profile
     */
    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    /**
     * Gets the status whether the Profile has agreed to Geolocation tracking
     * @return the Boolean representing Geolocation tracking permission
     */
    public Boolean getTrackGeolocation() {
        return trackGeolocation;
    }

    /**
     * Sets the Profile's permission to share Geolocation
     * @param trackGeolocation the Boolean representing Geolocation tracking permission
     */
    public void setTrackGeolocation(Boolean trackGeolocation) {
        this.trackGeolocation = trackGeolocation;
    }

    /**
     * Returns the Profile's homepage
     * @return String of the Profile's homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * Sets the Profile's homepage
     * @param homepage String of the Profile's homepage
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * Returns the Profile's contact information
     * @return String of the Profile's contact information
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the Profile's contact information
     * @param contact String of the Profile's contact information
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Returns the Profile's name
     * @return name String of the Profile's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the Profile's name
     * @param name String the of the Profile's name
     */
    public void setName(String name){
        this.name = name;
    }
}
