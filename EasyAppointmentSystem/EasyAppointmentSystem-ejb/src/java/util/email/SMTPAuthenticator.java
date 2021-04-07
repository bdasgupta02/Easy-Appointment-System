package util.email;

import javax.mail.*;



public class SMTPAuthenticator extends javax.mail.Authenticator
{
    private String smtpAuthUser;
    private String smtpAuthPassword;
    
    
        
    public SMTPAuthenticator() 
    {
    }

    
    
    public SMTPAuthenticator(String smtpAuthUser, String smtpAuthPassword)
    {
        this.smtpAuthUser = smtpAuthUser;
        this.smtpAuthPassword = smtpAuthPassword;
    }
    
    
    
    @Override
    public PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(smtpAuthUser, smtpAuthPassword);
    }
}