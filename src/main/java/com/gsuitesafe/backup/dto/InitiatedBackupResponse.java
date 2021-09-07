package com.gsuitesafe.backup.dto;

import com.gsuitesafe.backup.domain.BackupStatus;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class InitiatedBackupResponse {

    private UUID backupId;
    private LocalDate date;
    private BackupStatus status;

    public InitiatedBackupResponse() {
    }

    public InitiatedBackupResponse(UUID backupId, LocalDate date, BackupStatus status) {
        this.backupId = backupId;
        this.date = date;
        this.status = status;
    }

    public UUID getBackupId() {
        return backupId;
    }

    public LocalDate getDate() {
        return date;
    }

    public BackupStatus getStatus() {
        return status;
    }

    public void setStatus(BackupStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InitiatedBackupResponse that = (InitiatedBackupResponse) o;
        return backupId.equals(that.backupId) &&
                date.equals(that.date) &&
                status.equals(that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backupId, date, status);
    }

    @Override
    public String toString() {
        return "InitiatedBackupResponse{" +
                "backupId=" + backupId +
                ", date=" + date +
                ", status='" + status + '\'' +
                '}';
    }
}
