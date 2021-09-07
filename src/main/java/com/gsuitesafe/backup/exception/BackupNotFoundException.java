package com.gsuitesafe.backup.exception;

public class BackupNotFoundException extends RuntimeException {

    public BackupNotFoundException(String message) {
        super(message);
    }
}
