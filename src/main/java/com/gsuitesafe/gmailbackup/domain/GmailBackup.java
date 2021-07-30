package com.gsuitesafe.gmailbackup.domain;

import com.google.api.services.gmail.model.Message;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GmailBackup {

    private final UUID backupId;
    private final LocalDate date;
    private BackupStatus backupStatus;
    private CompletableFuture<Message> backupTask;

    public GmailBackup() {
        this.backupId = UUID.randomUUID();
        this.date = LocalDate.now();
        this.backupStatus = BackupStatus.IN_PROGRESS;
    }

    public UUID getBackupId() {
        return backupId;
    }

    public LocalDate getDate() {
        return date;
    }

    public BackupStatus getBackupStatus() {
        return backupStatus;
    }

    public void setBackupStatus(BackupStatus backupStatus) {
        this.backupStatus = backupStatus;
    }

    @Override
    public String toString() {
        return "GmailBackup{" +
                "backupId=" + backupId +
                ", date=" + date +
                ", backupStatus=" + backupStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GmailBackup that = (GmailBackup) o;
        return backupId.equals(that.backupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backupId);
    }
}
