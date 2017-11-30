package sjrating.service;

import sjrating.database.exception.DatabaseException;
import sjrating.database.impl.CompetitionDaoImpl;
import sjrating.database.impl.RatingDaoImpl;
import sjrating.model.Competition;
import sjrating.model.SkiJumper;
import sjrating.rating.RatingCalculator;
import sjrating.retriever.CompetitionRetriever;
import sjrating.retriever.exception.RetrieverException;
import sjrating.service.exception.ServiceException;

import java.util.List;
import java.util.Map;

public class SJRatingServiceImpl implements SJRatingService {

    private final CompetitionDaoImpl competitionDao;
    private final RatingDaoImpl ratingDao;

    private final CompetitionRetriever retriever;

    public SJRatingServiceImpl(CompetitionDaoImpl competitionDao, RatingDaoImpl ratingDao, CompetitionRetriever retriever) {
        this.competitionDao = competitionDao;
        this.ratingDao = ratingDao;
        this.retriever = retriever;
    }

    @Override
    public void insertCompetitions(int[] ids) throws ServiceException {

        Map<Integer, SkiJumper> skiJumpers;
        try {
            skiJumpers = competitionDao.selectSkiJumpers();
        } catch (DatabaseException e) {
            throw new ServiceException("Cannot fetch ski jumpers from database.", e);
        }

        for (int id : ids)
            insertCompetition(id, skiJumpers);
    }

    @Override
    public void recalculate() throws ServiceException {
        try {
            // select all ski jumpers from database
            Map<Integer, SkiJumper> skiJumpers = competitionDao.selectSkiJumpers();
            // select all competitions with results
            List<Competition> competitions = ratingDao.selectAllCompetitions(skiJumpers);
            // calculate rating
            RatingCalculator.calculateRating(competitions);
            // update ski jumpers
            ratingDao.updateSkiJumpers(skiJumpers);
            // update results
            ratingDao.updateResults(competitions);
        } catch (DatabaseException e) {
            throw new ServiceException("Cannot recalculate rating.", e);
        }
    }

    private void insertCompetition(int id, Map<Integer, SkiJumper> skiJumpers) throws ServiceException {
        try {
            // parse website with results
            Competition competition = retriever.fetchCompetition(id, skiJumpers);
            // save competition
            competitionDao.saveCompetition(competition, skiJumpers);
            // wait 300 miliseconds (to not spam external resource with requests)
            Thread.sleep(300);
        } catch (RetrieverException | DatabaseException e) {
            throw new ServiceException("Cannot insert competition " + id + ".", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
