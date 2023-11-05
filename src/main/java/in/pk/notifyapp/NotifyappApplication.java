package in.pk.notifyapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import in.pk.notifyapp.service.EmailSenderService;

@SpringBootApplication
@EnableScheduling
@EnableAsync 
public class NotifyappApplication {
	
	@Autowired
	private EmailSenderService emailSenderService;
	
	public static void main(String[] args) {
		SpringApplication.run(NotifyappApplication.class, args);
	}
	
	@Scheduled(cron = "${email.schedule.time}")
	@Async("emailSenderExecutor")
    public void sendScheduledEmail() {
        emailSenderService.sendScheduledEmailToUsers();
    }
}
