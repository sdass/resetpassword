package com.subra.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.subra.model.User;
import com.subra.model.UserRepository;

@Controller
public class HomeController {

	private static Logger log = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private JdbcTemplate jdbctemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@GetMapping(value={"", "/", "greet"})
	public String oneMethod(@RequestParam(name="email", required=false, defaultValue="sdass@drf.com") String email, Model mc) throws MessagingException{
		log.info("email=" + email);// good request param does not need regex .+ as reqd in reqparam. so use it and make it post, required =true
		mc.addAttribute("email", email);
		//sendEmail(email);
		getsomeData();
		addArecordJpaSpring();
		return "one";
	}
	
	private void sendEmail(String email) throws MessagingException{
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg);
		helper.setTo(email);
		helper.setText("Test 123");
		helper.setSubject("testing...");
		log.info("About to send mail");
		mailSender.send(msg);
	}
	private void getsomeData(){
		String sqlStr = "select * from users";

		List<User> luser =  jdbctemplate.query(sqlStr, new UserRowMapper());
		luser.forEach(u->log.info(u.toString()));
		
	}
	private User addArecordJpaSpring(){
		log.info("before cnt=)" + userRepository.count());
		User u = new User();
		u.setUsername("aaab");
		u.setPassword("ababab");
		u.setEnabled(0);
		User pers = userRepository.saveAndFlush(u);
		log.info("just persisted: " + pers);
		log.info("before cnt=)" + userRepository.count());
		return u;
	}
	
}

//----------------------
class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		return user;
	}
	
}