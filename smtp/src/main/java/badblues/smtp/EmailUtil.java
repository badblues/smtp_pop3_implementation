package badblues.smtp;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailUtil {


	public static void send(Properties props, String from, String to, MyMessage msg, Authenticator auth) throws MessagingException {

		Session session = Session.getInstance(props, auth);
		System.out.println("Session created");
		InternetAddress fromAddress = null;
		InternetAddress toAddress = null;
		Transport transport = session.getTransport("smtp");
		transport.connect();
		try {
			Message simpleMessage = new MimeMessage(session);
			fromAddress = new InternetAddress(from);
			toAddress = new InternetAddress(to);
			simpleMessage.setFrom(fromAddress);
			simpleMessage.setRecipient(MimeMessage.RecipientType.TO, toAddress);
			simpleMessage.setSubject(msg.subject);
			simpleMessage.setText(msg.content);
			transport.sendMessage(simpleMessage,
					simpleMessage.getAllRecipients());
		} catch (MessagingException e) {
			e.printStackTrace();
		} finally {
			transport.close();
			System.out.println("Message was successfully sent");
		}
	}

	public static void sendImageEmail(Properties props, String from, String to, MyMessage mesaga, Authenticator auth){
		try{

			Session session = Session.getInstance(props, auth);
			MimeMessage msg = new MimeMessage(session);
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress(from));

			msg.setReplyTo(InternetAddress.parse(to ,false));

			msg.setSubject(mesaga.subject, "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

			// Create the message body part
			BodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setText(mesaga.content);

			// Create a multipart message for attachment
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Second part is image attachment
			messageBodyPart = new MimeBodyPart();

			DataSource source = new FileDataSource(mesaga.fileName);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(mesaga.fileName);
			//Trick is to add the content-id header here
			messageBodyPart.setHeader("Content-ID", "image_id");
			multipart.addBodyPart(messageBodyPart);

			//third part for displaying image in the email body
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent("<h1>Attached Image</h1>" +
					"<img src='cid:image_id'>", "text/html");
			multipart.addBodyPart(messageBodyPart);

			//Set the multipart message to the email message
			msg.setContent(multipart);

			// Send message
			Transport.send(msg);
			System.out.println("EMail Sent Successfully with image!!");
		}catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void sendAttachmentEmail(Properties props, String from, String to, MyMessage mesaga, Authenticator auth){
		try{
			Session session = Session.getInstance(props, auth);
			MimeMessage msg = new MimeMessage(session);
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress(from));

			msg.setReplyTo(InternetAddress.parse(to, false));

			msg.setSubject(mesaga.subject, "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

			// Create the message body part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setText(mesaga.content);

			// Create a multipart message for attachment
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Second part is attachment
			messageBodyPart = new MimeBodyPart();
			String filename = mesaga.fileName;
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			msg.setContent(multipart);

			// Send message
			Transport.send(msg);
			System.out.println("EMail Sent Successfully with attachment!!");
		}catch (MessagingException e) {
			e.printStackTrace();
		}
	}


}