# Backup

### What is Backup?
* It's a command line tool used to back up your messages

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

 

