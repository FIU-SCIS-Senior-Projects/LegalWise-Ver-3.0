package res;

import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 
 * @author Michel
 *
 */
public class Email {
	
	private Properties props;
	private String host;
	private String username;
	private String passwd;
	private String port;
	private boolean auth;
	private boolean starttls;
	
	private Session session;
	private MimeMessage messg;
	private List<String> emailTo;
	private String subjectLine;
	private String body;
	
	public Email(List<String> emailTo, String subjectLine, String body) {
		super();
		
		this.emailTo = emailTo;
		this.subjectLine = subjectLine;
		this.body = body;
		session = init();
        messg = new MimeMessage(session);
	}
	//comment
	public boolean sentEmail (){
		boolean isSend = false;
		
		try {
            messg.setFrom(new InternetAddress(username));
            InternetAddress[] toAddress = new InternetAddress[emailTo.size()];

            // To get the array of addresses
            for( int i = 0; i < emailTo.size(); i++ ) {
                toAddress[i] = new InternetAddress(emailTo.get(i));
            }

            for( int i = 0; i < toAddress.length; i++) {
                messg.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            messg.setSubject(subjectLine);
            messg.setText(body,  "utf-8", "html");
           /* 
            // Create a multi-part to combine the parts
            Multipart multipart = new MimeMultipart("alternative");
            
            // Create your text message part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Hello user,");

            // Add the text part to the multipart
            multipart.addBodyPart(messageBodyPart);
            
            // Create the html part
            messageBodyPart = new MimeBodyPart();   
            messageBodyPart.setContent(body, "text/html; charset=utf-8");

            // Add html part to multi part
            multipart.addBodyPart(messageBodyPart);

            // Associate multi-part with message
            messg.setContent(multipart);
            */
            Transport transport = session.getTransport("smtp");
            transport.connect(host, username, passwd);
            transport.sendMessage(messg, messg.getAllRecipients());
            transport.close();
            isSend = true;
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
		
		return isSend;
	}
	
	private Session init(){
		// this should come from a porperty file but :)
		props = System.getProperties();
		host = "smtp.gmail.com";
		username = "legalwise3.0@gmail.com";
		passwd = "legalwise12345";
		port = "587";
		auth = true;
		starttls = true;
		
		//set properties
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.password", passwd);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        
        return Session.getDefaultInstance(props);
	}
	
	
	
	
	
}
