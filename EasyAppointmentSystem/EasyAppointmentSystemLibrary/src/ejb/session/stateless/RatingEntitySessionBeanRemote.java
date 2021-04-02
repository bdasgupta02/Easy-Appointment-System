package ejb.session.stateless;

import entity.RatingEntity;
import javax.ejb.Remote;
import util.exception.EntityAttributeNullException;
import util.exception.RatingNotFoundException;


public interface RatingEntitySessionBeanRemote {
    
    public Long addRating(RatingEntity ratingEntity) throws EntityAttributeNullException;

    public RatingEntity retrieveRatingEntityById(Long ratingId) throws RatingNotFoundException;

    public void updateRatingEntity(RatingEntity ratingEntity) throws EntityAttributeNullException;

    public void deleteRatingEntity(Long ratingId) throws RatingNotFoundException;
    
}
