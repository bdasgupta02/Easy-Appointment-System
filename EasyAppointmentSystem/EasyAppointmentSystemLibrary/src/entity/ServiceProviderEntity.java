package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ServiceProviderEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String bizRegNum;
    private int bizCategory;
    private String name;
    private String bizAddress;
    private String city;
    private String emailAddress;
    private String password;
    private String phoneNum;
    private String status;
    private float avgRating;
    
    @OneToMany
    private List<AppointmentEntity> appointments;

    public ServiceProviderEntity() {
    }
    
    public ServiceProviderEntity(String bizRegNum, int bizCategory, String name, String bizAddress, String city, String emailAddress, String password, String phoneNum) {
        this.bizRegNum = bizRegNum;
        this.bizCategory = bizCategory;
        this.name = name;
        this.bizAddress = bizAddress;
        this.city = city;
        this.emailAddress = emailAddress;
        this.password = password;
        this.phoneNum = phoneNum;
        this.avgRating = 0;
        this.status = "PENDING";
        this.appointments = new ArrayList<AppointmentEntity>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiceProviderEntity)) {
            return false;
        }
        ServiceProviderEntity other = (ServiceProviderEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ServiceProviderEntity[ id=" + id + " ]";
    }
    
    public String getBizRegNum() {
        return bizRegNum;
    }

    public void setBizRegNum(String bizRegNum) {
        this.bizRegNum = bizRegNum;
    }

   
    public int getBizCategory() {
        return bizCategory;
    }

    public void setBizCategory(int bizCategory) {
        this.bizCategory = bizCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBizAddress() {
        return bizAddress;
    }

    public void setBizAddress(String bizAddress) {
        this.bizAddress = bizAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }  

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float aAvgRating) {
        avgRating = aAvgRating;
    }

    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }

    public void addAppointment(AppointmentEntity newAppointment) {
        this.appointments.add(newAppointment);
    }
    
}
