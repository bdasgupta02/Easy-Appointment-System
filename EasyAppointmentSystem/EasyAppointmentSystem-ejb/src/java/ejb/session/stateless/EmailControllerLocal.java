package ejb.session.stateless;

import entity.AppointmentEntity;
import java.util.concurrent.Future;
import javax.ejb.Local;


public interface EmailControllerLocal {
    public Future<Boolean> emailCheckoutNotificationAsync(AppointmentEntity appointmentEntity, String fromEmailAddress, String toEmailAddress) throws InterruptedException;
}
