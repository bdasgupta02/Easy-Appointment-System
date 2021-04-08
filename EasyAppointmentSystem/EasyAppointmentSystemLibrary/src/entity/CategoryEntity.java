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
import javax.persistence.OneToMany;

@Entity
public class CategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    @Column(nullable=false, unique=true)
    private String category;
    
    @OneToMany(mappedBy = "bizCategory")
    private List<ServiceProviderEntity> serviceProviders;

    public CategoryEntity() {
        this.serviceProviders = new ArrayList<>();
    }

    public CategoryEntity(String category) {
        this();
        this.category = category;
    }
    
    public CategoryEntity(String category, List<ServiceProviderEntity> serviceProviders) {
        this();
        this.category = category;
        this.serviceProviders = serviceProviders;
    }

    public List<ServiceProviderEntity> getServiceProviders() {
        return serviceProviders;
    }

    public void addServiceProvider(ServiceProviderEntity serviceProvider) {
        this.serviceProviders.add(serviceProvider);
    }
    
    public void setServiceProvider(List<ServiceProviderEntity> serviceProviders) {
        this.serviceProviders = serviceProviders;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryId != null ? categoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the categoryId fields are not set
        if (!(object instanceof CategoryEntity)) {
            return false;
        }
        CategoryEntity other = (CategoryEntity) object;
        if ((this.categoryId == null && other.categoryId != null) || (this.categoryId != null && !this.categoryId.equals(other.categoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.category;
    }
    
}
