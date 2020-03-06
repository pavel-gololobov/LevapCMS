package ru.levap.cms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
public class SchedulingConfig {
	// every hour
	@Scheduled(cron="0 0 * * * ?")
	public void sendScheduledEmails() {
		log.info("Every hour job");
	}
	
	@Scheduled(initialDelay=3000, fixedRate=3600000)
	public void processTest() {
		log.info("============== Start Test");
	}
}
