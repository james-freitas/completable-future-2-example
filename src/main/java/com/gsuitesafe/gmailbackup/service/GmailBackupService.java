package com.gsuitesafe.gmailbackup.service;

import com.google.api.services.gmail.model.Message;
import com.gsuitesafe.gmailbackup.domain.GmailBackup;
import com.gsuitesafe.gmailbackup.dto.CreatedBackupResponse;
import com.gsuitesafe.gmailbackup.dto.InitiatedBackupResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    public CompletableFuture<List<Message>> createBackupTask() {

        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                logger.info("Getting google messages");
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            return generateMockedGmailMessagesList();
        });
    }

    private List<Message> generateMockedGmailMessagesList() {
        Message message = new Message();
        message.setId(UUID.randomUUID().toString());
        return Arrays.asList(new Message());
    }

    public void generateBackupZipFile(HttpServletResponse response) throws IOException {
        // create a list to add files to be zipped
        ArrayList<File> files = new ArrayList<>(2);
        files.add(new File("README.md"));

        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

        // package files
        for (File file : files) {
            //new zip entry and copying inputstream with file to zipOutputStream, after all closing streams
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fileInputStream = new FileInputStream(file);

            IOUtils.copy(fileInputStream, zipOutputStream);

            fileInputStream.close();
            zipOutputStream.closeEntry();
        }

        zipOutputStream.close();
    }
}
