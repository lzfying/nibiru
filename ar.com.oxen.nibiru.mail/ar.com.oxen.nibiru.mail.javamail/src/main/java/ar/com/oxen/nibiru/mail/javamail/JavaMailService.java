package ar.com.oxen.nibiru.mail.javamail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import ar.com.oxen.nibiru.mail.api.MailException;
import ar.com.oxen.nibiru.mail.api.MailMessage;
import ar.com.oxen.nibiru.mail.api.MailService;

public class JavaMailService implements MailService {
	private Session session;

	@Override
	public void sendMail(MailMessage message) {
		try {
			MimeMessage mimeMessage = new MimeMessage(this.session);
			mimeMessage.setFrom(new InternetAddress(message.getFrom()));
			for (String recipient : message.getTo()) {
				mimeMessage.addRecipient(Message.RecipientType.TO,
						new InternetAddress(recipient));
			}
			mimeMessage.setSubject(message.getSubject());
			mimeMessage.setText(message.getBody());
			
			Transport.send(mimeMessage);
			
		} catch (AddressException e) {
			throw new MailException(e);
		} catch (MessagingException e) {
			throw new MailException(e);
		}
	}

	public void setMailProperties(Properties mailProperties) {
		this.session = Session.getDefaultInstance(mailProperties, null);
	}
}
