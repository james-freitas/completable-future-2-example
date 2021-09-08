package com.gsuitesafe.backup.service;

import com.google.api.client.json.GenericJson;
import com.google.api.services.gmail.model.Message;
import com.gsuitesafe.backup.domain.Backup;
import com.gsuitesafe.backup.domain.BackupStatus;
import com.gsuitesafe.backup.dto.CreatedBackupResponse;
import com.gsuitesafe.backup.dto.InitiatedBackupResponse;
import com.gsuitesafe.backup.exception.BackupNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BackupService {

    private static final Logger logger = LoggerFactory.getLogger(BackupService.class);

    private final Map<String, Backup> backupMap = new ConcurrentHashMap<>();

    public CreatedBackupResponse createBackup() {
        final Backup backup = new Backup(createBackupTask());
        final String backupId = backup.getBackupId().toString();
        backupMap.put(backupId, backup);
        return new CreatedBackupResponse(backup.getBackupId());
    }

    public List<InitiatedBackupResponse> getInitiatedBackupList() {
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
    public CompletableFuture<List<Message>> createBackupTask() {

        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                logger.info("Getting messages");
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return generateMockedMessagesList();
        });
    }

    private List<Message> generateMockedMessagesList() {
        Message message1 = new Message();
        message1.setId(UUID.randomUUID().toString());
        message1.setLabelIds(Collections.singletonList("labelA"));

        Message message2 = new Message();
        message2.setId(UUID.randomUUID().toString());
        message2.setLabelIds(Collections.singletonList("labelB"));

        return Arrays.asList(message1, message2);
    }

    public List<String> getMessagesBy(String backupId) {
        Backup backup = backupMap.get(backupId);
        if (backup == null) {
            throw new BackupNotFoundException("Backup was not found");
        }

        setOkStatusFor(backupId, backup);

        return backup.getBackupTask().join()
                .stream()
                .map(GenericJson::toString)
                .collect(Collectors.toList());
    }

    private void setOkStatusFor(String backupId, Backup backup) {
        final CompletableFuture<List<Message>> backupTask = backup.getBackupTask();
        backupTask.thenRun(() -> {
            if (backup.getBackupStatus() != BackupStatus.OK) {
                backup.setBackupStatus(BackupStatus.OK);
                backupMap.put(backupId, backup);
            }
        }).exceptionally(ex -> {
            logger.error("Backup " + backupId + " has failed.");
            backup.setBackupStatus(BackupStatus.FAILED);
            return null;
        });
    }

    public List<String> getMessagesBy(String backupId, String label) {
        Backup backup = backupMap.get(backupId);
        if (backup == null) {
            throw new BackupNotFoundException("Backup was not found");
        }

        setOkStatusFor(backupId, backup);

        return backup.getBackupTask().join()
                .stream()
                .filter(m -> m.getLabelIds().contains(label))
                .map(GenericJson::toString)
                .collect(Collectors.toList());
    }
}
