package badblues.smtp;

import java.util.Properties;

import javax.mail.*;

public class SMTP {

	public static void main(String[] args) {
		final String fromEmail = "***"; //requires valid gmail id
		final String password = "***"; // correct password for gmail id
		final String toEmail = "***"; // can be any email id

		System.out.println("SSLEmail Start");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.yandex.ru"); //SMTP Host
		props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		//SSL Factory Class
		props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
		props.put("mail.smtp.port", "465"); //SMTP Port

		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};


		MyMessage message = new MyMessage("My First message", "Hello from Africa");
		try {
			EmailUtil.send(props,fromEmail,toEmail,message, auth);
			EmailUtil.sendImageEmail(props,fromEmail,toEmail,message, auth);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}