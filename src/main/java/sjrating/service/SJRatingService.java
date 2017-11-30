package sjrating.service;

import sjrating.service.exception.ServiceException;

/**
 * An interface containing methods used by user interface.
 */
public interface SJRatingService {
    void insertCompetitions(int[] ids) throws ServiceException;
    void recalculate() throws ServiceException;
}
