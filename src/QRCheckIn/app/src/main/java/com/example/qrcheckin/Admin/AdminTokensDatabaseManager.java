package com.example.qrcheckin.Admin;

import com.example.qrcheckin.Common.DatabaseManager;

import java.util.HashMap;

public class AdminTokensDatabaseManager extends DatabaseManager {
    /**
     * Constructs an AdminDatabaseManager for cases where we want to store a token as an Admin Token
     * @param token String fcmToken of the Attendee whose we are adding as an admin
     */
    public AdminTokensDatabaseManager(String token){
        super("adminTokens", token);
    }

    /**
     * Constructs an AttendeeDatabaseManager for cases where we want the Attendee collection (to execute a query for example)
     */
    public AdminTokensDatabaseManager(){
        super("adminTokens");
    }

    /**
     * Stores an empty document with a user's fcmToken to indicate they have admin permissions
     */
    public void storeAdminToken(){
        getCollectionRef().document(getDocumentID()).set(new HashMap<String, Object>());
    }
}
