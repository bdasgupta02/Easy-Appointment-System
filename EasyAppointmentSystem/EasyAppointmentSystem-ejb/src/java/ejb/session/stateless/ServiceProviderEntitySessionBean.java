package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.RatingEntity;
import entity.ServiceProviderEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Remote;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.enumeration.ServiceProviderStatusEnum;
import util.exception.CategoryNotFoundException;
import util.exception.DateProcessingException;
import util.exception.EntityAttributeNullException;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderAlreadyBlockedException;
import util.exception.ServiceProviderAlreadyExistsException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UniqueFieldExistsException;
import util.security.CryptographicHelper;

@Stateless
@Local(ServiceProviderEntitySessionBeanLocal.class)
@Remote(ServiceProviderEntitySessionBeanRemote.class)

public class ServiceProviderEntitySessionBean implements ServiceProviderEntitySessionBeanLocal, ServiceProviderEntitySessionBeanRemote {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;

    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;

    public ServiceProviderEntitySessionBean() {
    }

    @Override
    public Long addNewServiceProvider(ServiceProviderEntity newServiceProviderEntity) throws EntityAttributeNullException, ServiceProviderAlreadyExistsException {
        if (newServiceProviderEntity.getName().isEmpty() || newServiceProviderEntity.getBizCategory() == null
                || newServiceProviderEntity.getBizRegNum().isEmpty() || newServiceProviderEntity.getCity().isEmpty()
                || newServiceProviderEntity.getBizAddress().isEmpty() || newServiceProviderEntity.getEmail().isEmpty()
                || newServiceProviderEntity.getPhoneNum().isEmpty() || newServiceProviderEntity.getPassword().isEmpty()) {
            throw new EntityAttributeNullException("Error: Some values are null! Creation of Service Provider aborted.\n");
        } else {
            try {
                String hashedPassword = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(newServiceProviderEntity.getPassword()));
                newServiceProviderEntity.setPassword(hashedPassword);
                em.persist(newServiceProviderEntity);
                em.flush();
            } catch (PersistenceException ex) {
                if ((ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                        && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new ServiceProviderAlreadyExistsException("Error: Service provider already exists!");
                }
            }

            return newServiceProviderEntity.getServiceProviderId();
        }
    }

    @Override
    public ServiceProviderEntity retrieveServiceProviderEntityById(Long serviceProviderId) throws ServiceProviderNotFoundException {
        ServiceProviderEntity serviceProvider = em.find(ServiceProviderEntity.class, serviceProviderId);
        if (serviceProvider != null) {
            em.refresh(serviceProvider);
            serviceProvider.getAppointments().size();
            serviceProvider.getRatings().size();
            return serviceProvider;
        } else {
            throw new ServiceProviderNotFoundException("Error: Service provider with id " + serviceProviderId + " does not exist!");
        }
    }

    @Override
    public void updateServiceProviderEntity(ServiceProviderEntity serviceProviderEntity) throws EntityAttributeNullException, UniqueFieldExistsException {
        if (serviceProviderEntity.getName().isEmpty() || serviceProviderEntity.getBizCategory() == null
                || serviceProviderEntity.getBizRegNum().isEmpty() || serviceProviderEntity.getCity().isEmpty()
                || serviceProviderEntity.getBizAddress().isEmpty() || serviceProviderEntity.getEmail().isEmpty()
                || serviceProviderEntity.getPhoneNum().isEmpty() || serviceProviderEntity.getPassword().isEmpty()) {
            throw new EntityAttributeNullException("Error: Some values are null! Update of Service Provider aborted.\n");
        } else {
            try {
                ServiceProviderEntity outdatedServiceProvider = retrieveServiceProviderEntityById(serviceProviderEntity.getServiceProviderId());
                String hashedPassword = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(serviceProviderEntity.getPassword()));
                if (!outdatedServiceProvider.getPassword().equals(hashedPassword)) {
                    serviceProviderEntity.setPassword(hashedPassword);
                }
                em.merge(serviceProviderEntity);
                em.flush();
            } catch (PersistenceException ex) {
                if ((ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                        && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new UniqueFieldExistsException("Error: Service provider with email " + serviceProviderEntity.getEmail() + " already exists!");
                }
            } catch (ServiceProviderNotFoundException ex) {
            }     
        }
    }

    @Override
    public void deleteServiceProviderEntity(Long serviceProviderId) throws ServiceProviderNotFoundException {
        ServiceProviderEntity serviceProvider = em.find(ServiceProviderEntity.class, serviceProviderId);
        if (serviceProvider != null) {
            em.remove(serviceProvider);
        } else {
            throw new ServiceProviderNotFoundException("Error: Service provider with id " + serviceProviderId + " does not exist!\n");
        }
    }

    @Override
    public ServiceProviderEntity retrieveServiceProviderByEmail(String email) throws ServiceProviderNotFoundException {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.email = :inEmail");
        query.setParameter("inEmail", email);

        try {
            ServiceProviderEntity serviceProvider = (ServiceProviderEntity) query.getSingleResult();
            serviceProvider.getAppointments().size();
            serviceProvider.getRatings().size();
            return serviceProvider;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ServiceProviderNotFoundException();
        }
    }

    @Override
    public ServiceProviderEntity login(String emailAdd, String password) throws InvalidLoginException {
        try {
            ServiceProviderEntity serviceProviderEntity = retrieveServiceProviderByEmail(emailAdd);
            String hashedPassword = serviceProviderEntity.getPassword();
            String hashedInputPassword = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password));
            
            if (hashedInputPassword.equals(hashedPassword)) {
                serviceProviderEntity.getAppointments().size();
                serviceProviderEntity.getRatings().size();
                return serviceProviderEntity;
            } else {
                throw new InvalidLoginException("Error:Invalid password!");
            }
        } catch (ServiceProviderNotFoundException ex) {
            throw new InvalidLoginException("Error: Username does not exist!");
        }
    }

    @Override
    public List<AppointmentEntity> retrieveAppointmentsByServiceProviderId(Long serviceProviderId) throws ServiceProviderNotFoundException {
        ServiceProviderEntity serviceProviderEntity = retrieveServiceProviderEntityById(serviceProviderId);
        serviceProviderEntity.getAppointments().size();
        return serviceProviderEntity.getAppointments();
    }

    @Override
    public List<ServiceProviderEntity> retrieveAllServiceProviders() {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s ORDER BY s.serviceProviderId ASC");
        List<ServiceProviderEntity> allServiceProviders = query.getResultList();
        for (ServiceProviderEntity s : allServiceProviders) {
            s.getAppointments().size();
            s.getRatings().size();
        }
        return allServiceProviders;
    }

    @Override
    public List<ServiceProviderEntity> retrieveAllPendingServiceProviders() {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.status = :inPending ORDER BY s.serviceProviderId ASC");
        query.setParameter("inPending", ServiceProviderStatusEnum.PENDING);
        List<ServiceProviderEntity> pendingServiceProviders = query.getResultList();
        for (ServiceProviderEntity s : pendingServiceProviders) {
            s.getAppointments().size();
            s.getRatings().size();
        }
        return pendingServiceProviders;
    }

    @Override
    public void approveServiceProviderStatus(ServiceProviderEntity serviceProviderEntity) throws EntityAttributeNullException, UniqueFieldExistsException {
        serviceProviderEntity.setStatus(ServiceProviderStatusEnum.APPROVED);
        updateServiceProviderEntity(serviceProviderEntity);
    }

    @Override
    public void blockServiceProviderStatus(ServiceProviderEntity serviceProviderEntity) throws ServiceProviderAlreadyBlockedException, EntityAttributeNullException, UniqueFieldExistsException {

        if (serviceProviderEntity.getStatus() == ServiceProviderStatusEnum.BLOCKED) {
            throw new ServiceProviderAlreadyBlockedException("Error: Service provider already blocked!");
        }

        serviceProviderEntity.setStatus(ServiceProviderStatusEnum.BLOCKED);
        updateServiceProviderEntity(serviceProviderEntity);
    }

    @Override
    public List<ServiceProviderEntity> retrieveAllApprovedServiceProviders() {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.status = :inApproved ORDER BY s.serviceProviderId ASC");
        query.setParameter("inApproved", ServiceProviderStatusEnum.APPROVED);
        List<ServiceProviderEntity> serviceProviders = query.getResultList();
        for (ServiceProviderEntity s : serviceProviders) {
            s.getAppointments().size();
            s.getRatings().size();
        }
        return query.getResultList();
    }

    @Override
    public List<ServiceProviderEntity> retrieveSearchResult(Long categoryId, String city, Date date) {
        categoryId++;
        try {
            CategoryEntity category = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);
            Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.bizCategory = :inCategory AND s.city = :inCity AND s.status = :inApproved ORDER BY s.serviceProviderId ASC");
            query.setParameter("inCategory", category);
            query.setParameter("inCity", city);
            query.setParameter("inApproved", ServiceProviderStatusEnum.APPROVED);
            List<ServiceProviderEntity> queryResults = query.getResultList();
            for (ServiceProviderEntity s : queryResults) {
                s.getAppointments().size();
                s.getRatings().size();
            }
            List<ServiceProviderEntity> dateResults = sortDate(date, queryResults);
            return dateResults;
        } catch (CategoryNotFoundException ex) {
            System.out.println("Error while searching for Service Providers: Category does not exist!");
            return new ArrayList<>();
        }
    }

    private List<ServiceProviderEntity> sortDate(Date date, List<ServiceProviderEntity> serviceProviders) {
        List<ServiceProviderEntity> newServiceProviders = new ArrayList<>();
        for (ServiceProviderEntity s : serviceProviders) {
            try {
                String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
                if (nextSlotFreePerDate(s.getServiceProviderId(), dateString).size() > 0) {
                    newServiceProviders.add(s);
                }
            } catch (DateProcessingException | ServiceProviderNotFoundException ex) {
                System.out.println("Error: Date parsing error!");
            }
        }
        return newServiceProviders;
    }

    // revised slot going to be added if chosen by customer
    @Override
    public List<Date> nextSlotFreePerDate(Long serviceProviderId, String date) throws DateProcessingException, ServiceProviderNotFoundException {
        ServiceProviderEntity serviceProviderEntity = retrieveServiceProviderEntityById(serviceProviderId);
        serviceProviderEntity.getAppointments().size();
        List<AppointmentEntity> appointments = serviceProviderEntity.getAppointments();
        List<Date> emptySlots = initEmptySlots(date);
        List<Date> revisedSlots = new ArrayList<>();
        
        for (int i = 0; i < emptySlots.size(); i++) {
            boolean isEmpty = true;
            for (AppointmentEntity a : appointments) {

                // if existing appointments end and if the slot time is more than 2 hours away from now
                if (emptySlots.get(i).equals(a.getStartTimestamp()) && !a.getCancelled()) {
                    isEmpty = false;
                } else if (emptySlots.get(i).before(new Date(System.currentTimeMillis() + (3600 * 1000 * 2)))) {
                    isEmpty = false;
                }
            }
            if (isEmpty) {
                revisedSlots.add(emptySlots.get(i));
            }
        }
        return revisedSlots;
    }

    // duration is set to 1 hour
    private List<Date> initEmptySlots(String date) throws DateProcessingException {
        List<Date> emptySlots = new ArrayList<>();
        for (int i = 8; i < 18; i++) {
            String newHour = i + "";
            String newMinute = "30";
            try {
                emptySlots.add(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date + " " + newHour + ":" + newMinute));
            } catch (ParseException ex) {
                throw new DateProcessingException("Error processing date: parse issues while creating new slots!");
            }
        }
        return emptySlots;
    }

    @Override
    public Double getAverageRating(ServiceProviderEntity serviceProviderEntity) {
        List<RatingEntity> ratingEntities = serviceProviderEntity.getRatings();
        double size = (double) ratingEntities.size();
        double totalRating = 0.0;
        totalRating = ratingEntities.stream().map(r -> (double) r.getRating()).reduce(totalRating, (accumulator, _item) -> accumulator + _item);
        return size == 0 ? -1.0 : totalRating / size;
    }
}
