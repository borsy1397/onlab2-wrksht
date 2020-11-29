package com.captainborsy.wrksht.model;

public enum Status {

    TODO("TODO", "Elkészítendő"),
    IN_PROGRESS("IN_PROGRESS", "Folyamatban"),
    DONE("DONE", "Elvégzett");

    private final String status;
    private final String docxStatus;

    Status(String status, String docxStatus) {
        this.status = status;
        this.docxStatus = docxStatus;
    }

    public String getStatus() {
        return status;
    }

    public String getDocxStatus() {
        return docxStatus;
    }
}
