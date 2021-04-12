package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;
import util.enumeration.ServiceProviderStatusEnum;

@Entity
public class ServiceProviderEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceProviderId;
    
    @Column(nullable=false, unique=true)
    private String bizRegNum;
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
    private ServiceProviderStatusEnum status;
    
    @OneToMany(mappedBy = "serviceProviderEntity")
    @JoinColumn(nullable = false)
    private List<RatingEntity> ratings;
    
    @OneToMany(mappedBy = "serviceProviderEntity")
    @JoinColumn(nullable = false)
    private List<AppointmentEntity> appointments;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CategoryEntity bizCategory;

    public ServiceProviderEntity() {
        this.appointments = new ArrayList<>();
        this.ratings = new ArrayList<>();
    }

    public ServiceProviderEntity(String bizRegNum, CategoryEntity bizCategory, String name, String bizAddress, 
            String city, String email, String password, String phoneNum, ServiceProviderStatusEnum status, List<RatingEntity> ratings, List<AppointmentEntity> appointments) {
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
        this.ratings = ratings;
        this.appointments = appointments;
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

   
    public CategoryEntity getBizCategory() {
        return bizCategory;
    }

    public void setBizCategory(CategoryEntity bizCategory) {
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

    public ServiceProviderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ServiceProviderStatusEnum status) {
        this.status = status;
    }  

    @XmlTransient
    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }

    public void addAppointment(AppointmentEntity newAppointment) {
        this.getAppointments().add(newAppointment);
    }

    @XmlTransient
    public List<RatingEntity> getRatings() {
        return ratings;
    }

    public void addRating(RatingEntity rating) {
        this.ratings.add(rating);
    }
    
    public void setRating(List<RatingEntity> ratings) {
        this.ratings = ratings;
    }

    public void setAppointments(List<AppointmentEntity> appointments) {
        this.appointments = appointments;
    }
}
