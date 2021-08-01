package com.gsuitesafe.gmailbackup.service;

import com.gsuitesafe.gmailbackup.domain.GmailBackup;
import com.gsuitesafe.gmailbackup.dto.CreatedBackupResponse;
import com.gsuitesafe.gmailbackup.dto.InitiatedBackupResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class GmailBackupService {

    private static final Logger logger = LoggerFactory.getLogger(GmailBackupService.class);

    private final Map<UUID, GmailBackup> backupMap = new ConcurrentHashMap<>();

    public CreatedBackupResponse createGmailBackup() {
        final GmailBackup gmailBackup = new GmailBackup(createBackupTask());
        backupMap.put(gmailBackup.getBackupId(), gmailBackup);
        return new CreatedBackupResponse(gmailBackup.getBackupId());
    }

    public List<InitiatedBackupResponse> getInitiatedGmailBackupList() {
        return this.backupMap
                .values()
                .stream()
                .map(g -> new InitiatedBackupResponse(
                        g.getBackupId(),
                        g.getDate(),
                        g.getBackupStatus()
                )).collect(Collectors.toList());
    }

    @Async("processExecutor")
    public CompletableFuture<String> createBackupTask() {

        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                logger.info("Getting google messages");
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Gmail messages";
        });
    }

}
