package ejb.session.stateless;

import entity.AppointmentEntity;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import util.email.EmailManager;



@Stateless
@Local(EmailControllerLocal.class)
@Remote(EmailControllerRemote.class)

public class EmailController implements EmailControllerLocal, EmailControllerRemote
{

    @Asynchronous
    @Override
    public Future<Boolean> emailCheckoutNotificationAsync(AppointmentEntity appointmentEntity, String fromEmailAddress, String toEmailAddress) throws InterruptedException
    {
        EmailManager emailManager = new EmailManager("is2103group10@gmail.com", "Pa55word!");
        Boolean result = emailManager.emailCheckoutNotification(appointmentEntity, fromEmailAddress, toEmailAddress);
        
        return new AsyncResult<>(result);
    } 
}
