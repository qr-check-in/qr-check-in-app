package com.example.qrcheckin.Common;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Superclass for Atttendee/EventDatatbaseManager. Accesses firebase related objects like CollectionReference and DocumentReference.
 */

public abstract class DatabaseManager {
    protected final FirebaseFirestore db;
    protected final CollectionReference collectionRef;
    protected String documentID;
    protected DocumentReference docRef;


    /**
     * Constructs a DatabaseManager for cases where we want to store/retrieve data from Firebase for a specific Attendee
     * @param documentID String of the document we are accessing
     */
    public DatabaseManager(String collectionName, String documentID){
        this.db  = FirebaseFirestore.getInstance();
        this.collectionRef = db.collection(collectionName);
        this.documentID = documentID;
        this.docRef = collectionRef.document(documentID);
    }

    /**
     * Constructs a DatabaseManager for cases where we want to access a collection (to execute a query for example)
     */
    public DatabaseManager(String collectionName){
        this.db  = FirebaseFirestore.getInstance();
        this.collectionRef = db.collection(collectionName);
    }

    /**
     * Adds a value to an String array type field
     * @param fieldName String name of the array field
     * @param value String to be added to the array
     */
    public void addToArrayField(String fieldName, String value){
        docRef.update(fieldName, FieldValue.arrayUnion(value));
    }

    /**
     * Removes a value to an String array type field
     * @param fieldName String name of the array field
     * @param value String to be removed to the array
     */
    public void removeFromArrayField(String fieldName, String value){
        docRef.update(fieldName, FieldValue.arrayRemove(value));
    }

    /**
     * Returns the DocumentReference
     * @return docRef DocumentReference for a document in firebase
     */
    public DocumentReference getDocRef(){
        return docRef;
    }

    /**
     * Returns the CollectionReference
     * @return collectionRef CollectionReference for a collection in firebase
     */
    public CollectionReference getCollectionRef() {
        return collectionRef;
    }

    /**
     * Returns the documentID of the document this instance was created for
     * @return documentID String of the document ID in firebase
     */
    public String getDocumentID(){
        return documentID;
    }
}
