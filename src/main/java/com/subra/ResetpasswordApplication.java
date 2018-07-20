package com.subra;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

// tut: https://spring.io/guides/gs/serving-web-content/

@SpringBootApplication
@Configuration
public class ResetpasswordApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResetpasswordApplication.class, args);
	}
	
	@Bean
	public JavaMailSender javaMailSender(){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername("my.gmail@gmail.com");
	    mailSender.setPassword("password");

	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    
		return mailSender;			
		
	}
	
	@Profile("!test")
	@Bean
	public DataSource dataSource() throws NamingException{
		//return new JndiDataSourceLookup().getDataSource("jdbc/betsregistration");
		InitialContext ctx = new InitialContext();
		//return (DataSource) ctx.lookup("java:comp/env/jdbc/betsregistration"); //  good 
		// return new JndiDataSourceLookup().getDataSource("jdbc/betsregistration"); //good
		//return (DataSource) ctx.lookup("java:comp/env/jdbc/Mysql");//good
		return new JndiDataSourceLookup().getDataSource("jdbc/Mysql");
		
	}
	
	@Profile("!test")
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource){
		return new JdbcTemplate(dataSource);
		
	}
	
}
