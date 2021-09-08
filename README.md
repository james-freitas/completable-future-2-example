# Completable Future Example

### What is it?
* This command line tool backs up your messages

### Prerequisites
 * Java 1.8 Jdk or Jre installed

### How to build the application using Windows
 1. Go to the Windows command prompt. One option is to choose Run from the Windows Start menu, type cmd, and click OK.
 2. Go to the project root folder 
 3. Run `gradlew.bat build`

### How to build the application using MacOs
 1. Open a terminal
 2. Go to the project root folder
 3. Run `./gradlew build`

### API documentation
 - To access the API documentation start the application and go to http://localhost:8080/swagger-ui.html 
 
### How to run the application

 In order to run this application you must provide the full path of your credentials file 
 as informed in the **Prerequisites** section

 1. Open a terminal
 2. Go to the project root folder
 3. Build the application to generate the jar file
 4. Execute `java -jar build/libs/gmailbackup-0.0.1-SNAPSHOT.jar` 
 5. You can test the application calling this curl in another terminal window
 ```bash
curl --location --request GET 'http://localhost:8080/backups' 
```

### Functional requirements
There are three different client methods. All methods will return HTTP status code 200, unless something goes wrong.

 - **Backup**: This API will initiate a complete backup. The backup is asynchronous, and the API will return the id 
 for the initiated backup immediately.
   - **Request**: POST `/backups`
   - **Request body**: n/a
   - ** Response body** :
        ```json 
        {
            "backupId": <backupId>
        }
        ```   

 - **List backups**: This API will list all backups that have initiated.
   - **Request**: GET `/backups`
   - **Request body**: n/a
   - **Response body**:
    ```json 
    [
        {
        “backupId”: “<backup id>”,
            “date”: “<backup date>”,
            “status”: “<backup status>”
      },
      {
        …
      }
    ]
    ``` 

 - Backup status is one of the following:
   - In progress
   - OK
   - Failed
 
 - **Export backup**: This API will return the content of a specified backup in a compressed archive. 
 For now, that will be a ZIP file.  At this moment processing starts, and the client will wait until
 the backup is ready. 
   - **Request**: GET /exports/{backup id}
   - **Request body**: n/a
   - **Response body**: A streamed compressed archive.
   
### Non Functional requirements
 - The configuration of the executor is in the class `BackupApplication`
    ```java
    @SpringBootApplication
    @EnableAsync
    public class BackupApplication {
    
        // omitted code    
   
        @Bean(name = "processExecutor")
        public TaskExecutor workExecutor() {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setThreadNamePrefix("GmailBackup-");
            executor.setCorePoolSize(5);
            executor.setMaxPoolSize(10);
            executor.setQueueCapacity(500);
            executor.afterPropertiesSet();
            executor.initialize();
            logger.info("ThreadPoolTaskExecutor set");
            return executor;
        }
    }
    ```

 - Example of the `CompletableFuture` using the executor is found on the `BackupService`
    ```java
    @Service
    public class BackupService {
    
        // omitted code
        
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
    
        public List<String> getMessagesBy(String backupId) {
    
            // omitted code
            
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
    ``` 