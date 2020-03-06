package ru.levap.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource({ "classpath:mail-${environment.type}.properties" })
public class MailConfig {
	@Autowired
	private Environment env;
	
	@Bean
	public JavaMailSender mailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost(env.getProperty("mail.server.host"));
	    mailSender.setPort(Integer.parseInt(env.getProperty("mail.server.port")));
	    mailSender.setProtocol(env.getProperty("mail.server.protocol"));
	    mailSender.setUsername(env.getProperty("mail.server.username"));
	    mailSender.setPassword(env.getProperty("mail.server.password"));
	    return mailSender;
	}
}
