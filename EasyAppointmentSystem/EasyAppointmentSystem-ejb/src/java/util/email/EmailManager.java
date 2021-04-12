package util.email;

import entity.AppointmentEntity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;




public class EmailManager 
{
    private final String emailServerName = "smtp.gmail.com";     
    private final String mailer = "JavaMailer";
    private String smtpAuthUser;
    private String smtpAuthPassword;
    String datePattern = "yyyy-MM-dd";
    String timePattern = "HH:mm";
    DateFormat dateFormat = new SimpleDateFormat(datePattern);
    DateFormat timeFormat = new SimpleDateFormat(timePattern);
    
    
    
    public EmailManager()
    {
    }

    
    
    public EmailManager(String smtpAuthUser, String smtpAuthPassword)
    {
        this.smtpAuthUser = smtpAuthUser;
        this.smtpAuthPassword = smtpAuthPassword;
    }
    
    
    
    public Boolean emailCheckoutNotification(AppointmentEntity appointmentEntity, String fromEmailAddress, String toEmailAddress)
    {
        String emailBody = "";
      
        emailBody += "Dear " + appointmentEntity.getCustomerEntity().getFirstName() + ", you have to following appointment soon!\n" +   
                "Appointment Num: " + appointmentEntity.getAppointmentNum() + "\n" +
                "Time: " + dateFormat.format(appointmentEntity.getStartTimestamp()) + ", " + timeFormat.format(appointmentEntity.getStartTimestamp()) + "\n";
        emailBody += "Please be there at least 15 minutes before your time slot!\n";
        emailBody += "We hope to see you there and have a great day ahead!\n";
        emailBody += "Sincerely, \n The EasyAppointment Team";
           
        
        try 
        {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");            
            javax.mail.Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPassword);
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);            
            Message msg = new MimeMessage(session);
                                    
            if (msg != null)
            {
                msg.setFrom(InternetAddress.parse(fromEmailAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
                msg.setSubject("Appointment reminder from EasyAppointment!");
                msg.setText(emailBody);
                msg.setHeader("X-Mailer", mailer);
                
                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);
                
                Transport.send(msg);
                
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            
            return false;
        }
    }
}
