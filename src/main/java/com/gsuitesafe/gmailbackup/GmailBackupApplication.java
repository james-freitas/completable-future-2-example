package com.gsuitesafe.gmailbackup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class GmailBackupApplication {

	private static final Logger logger = LoggerFactory.getLogger(GmailBackupApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GmailBackupApplication.class, args);
	}

	@Bean(name="processExecutor")
	public TaskExecutor workExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("GmailBackup-");
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(5);
		executor.setQueueCapacity(500);
		executor.afterPropertiesSet();
		executor.initialize();
		logger.info("ThreadPoolTaskExecutor set");
		return executor;
	}
}
