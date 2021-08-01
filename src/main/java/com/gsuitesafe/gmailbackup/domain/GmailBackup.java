package com.gsuitesafe.gmailbackup.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gsuitesafe.gmailbackup.service.GmailBackupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class GmailBackup {

    private UUID backupId;
    private LocalDate date;
    private BackupStatus backupStatus;
    private CompletableFuture<String> backupTask;

    private GmailBackup() {}

    public GmailBackup(CompletableFuture<String> backupTask) {
        this.backupId = UUID.randomUUID();
        this.date = LocalDate.now();
        this.backupStatus = BackupStatus.IN_PROGRESS;
        this.backupTask = backupTask;
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
