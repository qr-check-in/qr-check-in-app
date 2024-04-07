package com.example.qrcheckin;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreWrapperImpl implements FirestoreWrapper {
    @Override
    public void fetchDocument(String collectionPath, String documentId, DocumentCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionPath).document(documentId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        callback.onSuccess(documentSnapshot.getData());
                    } else {
                        callback.onFailure(new Exception("No such document exists!"));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }
    // working on defining other methods.
}

