package sjrating.database.impl;

import sjrating.database.DatabaseConnectionManager;
import sjrating.database.interfaces.RatingDao;
import sjrating.database.exception.DatabaseException;
import sjrating.model.Competition;
import sjrating.model.CompetitionResult;
import sjrating.model.SkiJumper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RatingDaoImpl implements RatingDao {

    private final DatabaseConnectionManager connectionManager;

    private static final String SELECT_RESULTS_SQL =
            "SELECT cr.competition_id AS cpid, " +
                    "cr.skijumper_id AS sjid, " +
                    "cr.id AS crid, " +
                    "cr.place AS place " +
                    "FROM `competition_results` cr " +
                    "JOIN competitions c ON cr.competition_id = c.race_id " +
                    "ORDER BY c.date, c.race_id, cr.place";

    private static final String UPDATE_SKIJUMPERS_RATINGS_SQL =
            "UPDATE skijumpers SET rating = ? WHERE jumper_id = ?";

    private static final String UPDATE_RESULTS_RATINGS_SQL =
            "UPDATE competition_results SET rating_before = ?, rating_after = ? WHERE id = ?";

    public RatingDaoImpl(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public List<Competition> selectAllCompetitions(Map<Integer, SkiJumper> skiJumpers) throws DatabaseException {
        connectionManager.connect();
        List<Competition> competitions;
        try {
            competitions = selectCompetitionResults(skiJumpers);
            connectionManager.getConnection().close();
        } catch (SQLException e) {
            throw new DatabaseException("Cannot select competitions from database.", e);
        }
        return competitions;
    }

    public void updateSkiJumpers(Map<Integer, SkiJumper> skiJumpers) throws DatabaseException {
        connectionManager.connect();
        try {
            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(UPDATE_SKIJUMPERS_RATINGS_SQL);
            for (SkiJumper skiJumper : skiJumpers.values()) {
                preparedStatement.setDouble(1, skiJumper.getRating());
                preparedStatement.setInt(2, skiJumper.getFisId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            preparedStatement.close();
            connectionManager.getConnection().close();
        } catch (SQLException e) {
            throw new DatabaseException("Cannot update ski jumpers.", e);
        }
    }

    /**
     * Updates results to database.
     *
     * @param competitions
     * @throws SQLException
     */
    public void updateResults(List<Competition> competitions) throws DatabaseException {
        connectionManager.connect();
        try {
            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(UPDATE_RESULTS_RATINGS_SQL);
            for (Competition competition : competitions) {
                for (CompetitionResult result : competition.getResults()) {
                    preparedStatement.setDouble(1, result.getRatingBefore());
                    preparedStatement.setDouble(2, result.getRatingAfter());
                    preparedStatement.setDouble(3, result.getId());
                    preparedStatement.addBatch();
                }
            }
            preparedStatement.executeBatch();
            preparedStatement.close();
            connectionManager.getConnection().close();
        } catch (SQLException e) {
            throw new DatabaseException("Cannot update results.", e);
        }
    }

    private List<Competition> selectCompetitionResults(Map<Integer, SkiJumper> skiJumpers) throws SQLException {
        List<Competition> competitions = new ArrayList<>();

        Statement statement = connectionManager.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_RESULTS_SQL);
        int lastRaceId = 0;
        int i = 0;
        Competition currentCompetition = null;
        while (resultSet.next()) {
            int cpId = resultSet.getInt("cpid");
            if (cpId != lastRaceId) {
                currentCompetition = new Competition(cpId, null, "", "");
                competitions.add(currentCompetition);
                lastRaceId = cpId;
            }

            SkiJumper skiJumper = skiJumpers.get(resultSet.getInt("sjid"));
            int place = resultSet.getInt("place");
            int id = resultSet.getInt("crid");
            if (currentCompetition != null) {
                currentCompetition.addResult(new CompetitionResult(skiJumper, place, id));
            }
        }
        return competitions;
    }
}
