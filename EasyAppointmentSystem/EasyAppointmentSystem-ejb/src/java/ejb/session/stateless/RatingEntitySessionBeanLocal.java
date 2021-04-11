package ejb.session.stateless;

import entity.CustomerEntity;
import entity.RatingEntity;
import entity.ServiceProviderEntity;
import javax.ejb.Local;
import util.exception.EntityAttributeNullException;
import util.exception.RatingNotFoundException;


public interface RatingEntitySessionBeanLocal {

    public Long addRating(RatingEntity ratingEntity) throws EntityAttributeNullException;

    public RatingEntity retrieveRatingEntityById(Long ratingId) throws RatingNotFoundException;

    public void updateRatingEntity(RatingEntity ratingEntity) throws EntityAttributeNullException;

    public void deleteRatingEntity(Long ratingId) throws RatingNotFoundException;

    public boolean isAlreadyRated(ServiceProviderEntity serviceProvider, CustomerEntity customer);
    
}
