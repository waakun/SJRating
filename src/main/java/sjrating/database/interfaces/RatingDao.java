package sjrating.database.interfaces;

import sjrating.database.exception.DatabaseException;
import sjrating.model.Competition;
import sjrating.model.SkiJumper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface RatingDao {
    /**
     * Selects all results from competitions.
     *
     * @return
     * @throws SQLException
     */
    List<Competition> selectAllCompetitions(Map<Integer, SkiJumper> skiJumpers) throws DatabaseException;

    /**
     * Updates all ski jumpers' stats.
     *
     * @param skiJumpers
     * @throws SQLException
     */
    void updateSkiJumpers(Map<Integer, SkiJumper> skiJumpers) throws DatabaseException;

    /**
     * Updates results to database.
     *
     * @param competitions
     * @throws SQLException
     */
    void updateResults(List<Competition> competitions) throws DatabaseException;
}
