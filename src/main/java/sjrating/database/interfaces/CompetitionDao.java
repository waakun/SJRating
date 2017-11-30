package sjrating.database.interfaces;

import sjrating.database.exception.DatabaseException;
import sjrating.model.Competition;
import sjrating.model.SkiJumper;

import java.util.Map;

public interface CompetitionDao {
    /**
     * Saves single competition to database.
     *
     * @param competition
     * @throws DatabaseException
     */
    void saveCompetition(Competition competition, Map<Integer, SkiJumper> skiJumpers) throws DatabaseException;
    /**
     * Selects all ski jumpers from database.
     *
     * @return
     * @throws DatabaseException
     */
    Map<Integer, SkiJumper> selectSkiJumpers() throws DatabaseException;
}
