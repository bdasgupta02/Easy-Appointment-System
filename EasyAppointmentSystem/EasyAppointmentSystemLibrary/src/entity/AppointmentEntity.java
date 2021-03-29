package entity;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class AppointmentEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CustomerEntity customerEntity;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ServiceProviderEntity serviceProviderEntity;
    
    @Column(nullable=false)
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(nullable=false)
    @Temporal(TemporalType.TIME)
    private Date startTime;
    @Column(nullable=false)
    @Temporal(TemporalType.TIME)
    private Date endTime;
    @Column(nullable=false)
    private Boolean cancelled;
    
    @Column(nullable=false, unique=true)
    private String appointmentNum;

    public AppointmentEntity() {
    }

    public AppointmentEntity(CustomerEntity customerEntity, ServiceProviderEntity serviceProviderEntity, Date date, Date startTime, Date endTime, String appointmentNum) {
        this();
        this.customerEntity = customerEntity;
        this.serviceProviderEntity = serviceProviderEntity;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointmentNum = appointmentNum;
        this.cancelled = false;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }



    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (appointmentId != null ? appointmentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the appointmentId fields are not set
        if (!(object instanceof AppointmentEntity)) {
            return false;
        }
        AppointmentEntity other = (AppointmentEntity) object;
        if ((this.appointmentId == null && other.appointmentId != null) || (this.appointmentId != null && !this.appointmentId.equals(other.appointmentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AppointmentEntity[ id=" + appointmentId + " ]";
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public ServiceProviderEntity getServiceProviderEntity() {
        return serviceProviderEntity;
    }

    public void setServiceProviderEntity(ServiceProviderEntity serviceProviderEntity) {
        this.serviceProviderEntity = serviceProviderEntity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAppointmentNum() {
        return appointmentNum;
    }

    public void setAppointmentNum(String appointmentNum) {
        this.appointmentNum = appointmentNum;
    }
}
