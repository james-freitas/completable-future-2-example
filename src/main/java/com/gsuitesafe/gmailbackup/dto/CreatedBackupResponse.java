package com.gsuitesafe.gmailbackup.dto;

import java.util.Objects;
import java.util.UUID;

public class CreatedBackupResponse {

    private final UUID backupId;

    public CreatedBackupResponse(UUID backupId) {
        this.backupId = backupId;
    }

    public UUID getBackupId() {
        return backupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreatedBackupResponse that = (CreatedBackupResponse) o;
        return backupId.equals(that.backupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backupId);
    }

    @Override
    public String toString() {
        return "BackupResponse{" +
                "backupId=" + backupId +
                '}';
    }
}
