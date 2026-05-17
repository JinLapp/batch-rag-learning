package com.example.textvectorizer.ionos.dto;

import java.util.List;

public class IonosUpsertDocumentsRequest {

    private List<IonosDocumentRequest> documents;

    public IonosUpsertDocumentsRequest() {
    }

    public IonosUpsertDocumentsRequest(List<IonosDocumentRequest> documents) {
        this.documents = documents;
    }

    public List<IonosDocumentRequest> getDocuments() {
        return documents;
    }

    public void setDocuments(List<IonosDocumentRequest> documents) {
        this.documents = documents;
    }
}