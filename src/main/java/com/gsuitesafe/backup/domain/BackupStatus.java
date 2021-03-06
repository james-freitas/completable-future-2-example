package com.gsuitesafe.backup.domain;

public enum BackupStatus {

    IN_PROGRESS("In progress"),
    OK("OK"),
    FAILED("Failed");

    private String description;

    BackupStatus(String description) {
        this.description = description;
    }
}
