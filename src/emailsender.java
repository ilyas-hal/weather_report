import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
public class emailsender {
    public static void sendEmail(String str) throws MessagingException {
        String EMAIL_USERNAME = System.getenv("EMAIL_USERNAME");
        String EMAIL_PASSWORD = System.getenv("EMAIL_PASSWORD");
        if (EMAIL_USERNAME == null || EMAIL_PASSWORD == null) {
            throw new IllegalStateException("EMAIL_USERNAME or EMAIL_PASSWORD environment variables are not defined.");
        }
        String recipient = "";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL_USERNAME));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(recipient));
        message.setSubject("Weather Report for today");
        message.setText(str);

        Transport.send(message);

    }
}