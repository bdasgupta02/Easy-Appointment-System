package entity;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ServiceProviderEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceProviderId;
    
    @Column(nullable=false, unique=true)
    private String bizRegNum;
    @Column(nullable=false)
    private int bizCategory;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private String bizAddress;
    @Column(nullable=false)
    private String city;
    @Column(nullable=false, unique=true)
    private String email;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false)
    private String phoneNum;
    @Column(nullable=false)
    private String status;
    @Column(nullable=false)
    private float avgRating;
    
    @OneToMany(mappedBy = "serviceProviderEntity")
    private List<AppointmentEntity> appointments;

    public ServiceProviderEntity() {
    }

    public ServiceProviderEntity(String bizRegNum, int bizCategory, String name, String bizAddress, String city, String email, String password, String phoneNum, String status, float avgRating) {
        this();
        this.bizRegNum = bizRegNum;
        this.bizCategory = bizCategory;
        this.name = name;
        this.bizAddress = bizAddress;
        this.city = city;
        this.email = email;
        this.password = password;
        this.phoneNum = phoneNum;
        this.status = status;
        this.avgRating = avgRating;
    }


    public Long getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(Long serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (serviceProviderId != null ? serviceProviderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the serviceProviderId fields are not set
        if (!(object instanceof ServiceProviderEntity)) {
            return false;
        }
        ServiceProviderEntity other = (ServiceProviderEntity) object;
        if ((this.serviceProviderId == null && other.serviceProviderId != null) || (this.serviceProviderId != null && !this.serviceProviderId.equals(other.serviceProviderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ServiceProviderEntity[ id=" + serviceProviderId + " ]";
    }

    public String getBizRegNum() {
        return bizRegNum;
    }

    public void setBizRegNum(String bizRegNum) {
        this.bizRegNum = bizRegNum;
    }

   
    public Integer getBizCategory() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
