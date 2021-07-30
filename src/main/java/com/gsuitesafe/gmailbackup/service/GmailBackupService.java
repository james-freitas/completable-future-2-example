package com.gsuitesafe.gmailbackup.service;

import com.gsuitesafe.gmailbackup.domain.GmailBackup;
import com.gsuitesafe.gmailbackup.dto.CreatedBackupResponse;
import org.springframework.stereotype.Service;

@Service
public class GmailBackupService {

    public CreatedBackupResponse createGmailBackup() {

        final GmailBackup gmailBackup = new GmailBackup();

        return new CreatedBackupResponse(gmailBackup.getBackupId());
    }
}
