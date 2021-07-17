package com.gsuitesafe.gmailbackup.controller;

import com.gsuitesafe.gmailbackup.dto.CreatedBackupResponse;
import com.gsuitesafe.gmailbackup.dto.InitiatedBackupResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class GmailBackupController {

    @PostMapping("/backups")
    public ResponseEntity<CreatedBackupResponse> createBackup() {
        final CreatedBackupResponse createdBackupResponse = new CreatedBackupResponse();
        return new ResponseEntity<>(createdBackupResponse, HttpStatus.OK);
    }

    @GetMapping("/backups")
    public ResponseEntity<List<InitiatedBackupResponse>> listInitiatedBackups() {

        final InitiatedBackupResponse ibr = new InitiatedBackupResponse(
                UUID.randomUUID(),
                LocalDate.now(),
                "In progress"
        );

        List<InitiatedBackupResponse> list = new ArrayList<>();
        list.add(ibr);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
