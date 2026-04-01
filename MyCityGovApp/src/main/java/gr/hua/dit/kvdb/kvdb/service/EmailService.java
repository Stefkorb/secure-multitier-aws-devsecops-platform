package gr.hua.dit.kvdb.kvdb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        logger.info("📧 Attempting to send email to: {}", to); // Log έναρξης

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@mycitygov.gr");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            logger.info("✅ Email sent successfully to {}", to); // Log επιτυχίας

        } catch (MailException e) {
            logger.error("❌ Failed to send email to {}. Error: {}", to, e.getMessage());
            e.printStackTrace();
        }
    }
}