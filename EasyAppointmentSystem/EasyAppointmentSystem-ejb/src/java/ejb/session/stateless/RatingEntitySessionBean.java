package ejb.session.stateless;

import entity.RatingEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.EntityAttributeNullException;
import util.exception.RatingNotFoundException;


@Stateless
@Local(RatingEntitySessionBeanLocal.class)
@Remote(RatingEntitySessionBeanRemote.class)
public class RatingEntitySessionBean implements RatingEntitySessionBeanRemote, RatingEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long addRating(RatingEntity ratingEntity) throws EntityAttributeNullException {
        if (ratingEntity.getRating() == null) {
            throw new EntityAttributeNullException("Error: Some values are null! Creation of Rating aborted.\n");
        } else {
            em.persist(ratingEntity);
            em.flush();
            return ratingEntity.getRatingId();
        }
    }
    
    @Override
    public RatingEntity retrieveRatingEntityById(Long ratingId) throws RatingNotFoundException {
        RatingEntity ratingEntity = em.find(RatingEntity.class, ratingId);
        if (ratingEntity != null) {
            return ratingEntity;
        } else {
            throw new RatingNotFoundException("Error: Rating with id " + ratingId + " does not exist!\n");
        }
    }
    
    @Override
    public void updateRatingEntity(RatingEntity ratingEntity) throws EntityAttributeNullException {
        if (ratingEntity.getRating() == null) {
            throw new EntityAttributeNullException("Error: Some values are null! Update of Rating aborted.\n");
        } else {
            em.merge(ratingEntity);
            em.flush();
        }
    }
    
    @Override
    public void deleteRatingEntity(Long ratingId) throws RatingNotFoundException {
        RatingEntity ratingEntity = em.find(RatingEntity.class, ratingId);
        if (ratingEntity != null) {
            em.remove(ratingEntity);
        } else {
            throw new RatingNotFoundException("Error: Rating with id " + ratingId + " does not exist!\n");
        }
    }
}
