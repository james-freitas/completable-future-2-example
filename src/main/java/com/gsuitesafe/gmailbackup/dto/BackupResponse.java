package com.gsuitesafe.gmailbackup.dto;

import java.util.Objects;
import java.util.UUID;

public class BackupResponse {

    private UUID backupId;

    public BackupResponse() {
        this.backupId = UUID.randomUUID();
    }

    public UUID getBackupId() {
        return backupId;
    }

    public void setBackupId(UUID backupId) {
        this.backupId = backupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackupResponse that = (BackupResponse) o;
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
