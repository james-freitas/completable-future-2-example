package com.gsuitesafe.gmailbackup.controller;

import com.gsuitesafe.gmailbackup.dto.BackupResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GmailBackupController {

    @PostMapping("/backups")
    public ResponseEntity<BackupResponse> createBackup() {
        final BackupResponse backupResponse = new BackupResponse();
        return new ResponseEntity<>(backupResponse, HttpStatus.OK);
    }
}
