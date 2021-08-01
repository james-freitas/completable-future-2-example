package com.gsuitesafe.gmailbackup.dto;

import java.util.UUID;

public class CreatedBackupResponse {

    private final UUID backupId;

    public CreatedBackupResponse(UUID backupId) {
        this.backupId = backupId;
    }

    public UUID getBackupId() {
        return backupId;
    }
}
