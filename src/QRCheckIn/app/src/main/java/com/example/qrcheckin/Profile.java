package com.example.qrcheckin;

import android.location.Location;

import java.io.FileNotFoundException;
public class Profile {
    private Boolean trackGeolocation;
    private String name;
    private String homepage;
    private String contact;
    private ProfilePicture profilePicture;
    private Location location;
    /**
     * Constructs a Profile for an Attendee
     */
    public Profile(){
        // default geolocation sharing permission set to false
        this.trackGeolocation = false;

        // TODO: randomize initial names in some way so that more unique profile pictures are generated.
        this.name = "new user";

        // TODO: generate a profile picture based on the profile name
        //generatePicture(this.firstName, this.lastName);
    }

    /**
     * In progress
     * @param firstName
     * @param lastName
     * @throws FileNotFoundException
     */
    /*
    public void generatePicture(String firstName, String lastName) throws FileNotFoundException {
        int firstHash = firstName.hashCode();
        int lastHash = lastName.hashCode();

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();

        // access collection of icons from db
        // https://firebase.google.com/docs/firestore/query-data/aggregation-queries#java_1
        // Get number of icons in db
        CollectionReference collection = db.collection("Profile Icons");

        AggregateQuerySnapshot snapshot = collection.count().get().get(); // Need aggregate source
        // index of the icon to pick from the db
        long iconIndex = firstHash % snapshot.getCount();

        // https://stackoverflow.com/questions/31232713/how-do-i-convert-to-color-to-bitmap
        // create bitmap for background color
        Bitmap background = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);
        canvas.drawColor(lastHash);

        // need to access document by index
        DocumentReference iconRef = db.collection("Profile Icons").document();

        https://stackoverflow.com/questions/2738834/combining-two-png-files-in-android
        // combine background and icon pngs
        Bitmap iconBitmap = BitmapFactory.decodeFile("");
        canvas.drawBitmap(iconBitmap, 0f, 0f, null);

        //Environment.getExternalStorageDirectory().getPath()
        OutputStream os = null;
        try {
            os = new FileOutputStream(Environment.getExternalStorageDirectory().getPath());
            //canvas.compress(Bitmap.CompressFormat.PNG, 50, os);
        } catch(IOException e) {
            e.printStackTrace();
        }

        //this.picture = new ProfilePicture();

    }
    */

    /**
     * Returns the ProfilePicture of the Profile
     * @return the ProfilePicture of the Profile
     */
    public ProfilePicture getPicture() {
        return profilePicture;
    }

    /**
     * Sets the ProfilePicture of the Profile
     * @param picture the ProfilePicture of the Profile
     */
    public void setPicture(ProfilePicture picture) {
        this.profilePicture = picture;
    }

    /**
     * Returns the Location of the Profile
     * @return the Location of the Profile
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the Location of the Profile
     * @param location the Location of the Profile
     */
    public void setLocation(Location location) {
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