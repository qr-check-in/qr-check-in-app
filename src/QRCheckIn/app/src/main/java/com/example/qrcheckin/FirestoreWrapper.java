package com.example.qrcheckin;

import java.util.Map;

public interface FirestoreWrapper {
    void fetchDocument(String collectionPath, String documentId, DocumentCallback callback);
    // working on defining other methods
}



