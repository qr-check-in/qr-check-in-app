package db;

public interface DocumentIdCallback {
    void onDocumentIdRetrieved(String documentId);

    void onDocumentIdNotFound();

    void onFailure(Exception e);
}
