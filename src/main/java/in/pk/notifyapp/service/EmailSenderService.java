package in.pk.notifyapp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import in.pk.notifyapp.entity.UserAccountTO;
import in.pk.notifyapp.repository.UserAccountRepo;

@Service
public class EmailSenderService {

	Logger log = LoggerFactory.getLogger(EmailSenderService.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Value("${email.from}")
	private String EMAIL_FROM;

	@Value("${email.batch.size}")
	private String batchSize;

	public void sendEmail(String email, String subject, String body) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(EMAIL_FROM);
			message.setTo(email);
			message.setSubject(subject);
			message.setText(body);
			javaMailSender.send(message);
		} catch (MailException e) {
			log.error("Exception occurred in sendEmail... for " + email, e);
		}
	}

	public void sendScheduledEmailToUsers() {
		log.info("Entering into sendScheduledEmailToUsers...");
		try {
			int page = 0;
			int pageSize = batchSize != null && !batchSize.isEmpty() ? Integer.parseInt(batchSize) : 1000;
			while (true) {
				List<UserAccountTO> users = userAccountRepo.findAll(PageRequest.of(page, pageSize)).getContent();
				if (users.isEmpty()) {
					break;
				}
				for (UserAccountTO user : users) {
					sendEmail(user.getUserEmail(), "Subject", "Hi " + user.getUserName());
				}
				page++;
			}
		} catch (Exception e) {
			log.error("Exception occurred in sendScheduledEmailToUsers...", e);
		}
	}
}
