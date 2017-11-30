package sjrating.database.impl;

import sjrating.database.exception.DatabaseException;
import sjrating.database.interfaces.CompetitionDao;
import sjrating.database.DatabaseConnectionManager;
import sjrating.model.Competition;
import sjrating.model.CompetitionResult;
import sjrating.model.SkiJumper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CompetitionDaoImpl implements CompetitionDao {

    private final DatabaseConnectionManager connectionManager;

    private static final String INSERT_COMPETITION_SQL =
            "INSERT INTO competitions (`race_id`, `name`, `place`, `date`) VALUES (?, ?, ?, ?) ";
    private static final String INSERT_SKIJUMPERS_SQL =
            "INSERT INTO skijumpers (`jumper_id`, `name`, `country`) VALUES (?, ?, ?) ";
    private static final String INSERT_COMPETITION_RESULT_SQL =
            "INSERT INTO competition_results (`competition_id`, `skijumper_id`, `place`) VALUES (?, ?, ?)";
    private static final String SELECT_SKIJUMPERS_SQL =
            "SELECT `jumper_id`, `name`, `country` FROM skijumpers";

    public CompetitionDaoImpl(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void saveCompetition(Competition competition, Map<Integer, SkiJumper> skiJumpers) throws DatabaseException {
        try {
            connectionManager.connect();
            insertCompetition(competition);
            insertNewSkiJumpers(skiJumpers);
            insertCompetitionResults(competition);
            connectionManager.close();
        } catch (SQLException e) {
            throw new DatabaseException("Cannot save competition into database.", e);
        }
    }

    public Map<Integer, SkiJumper> selectSkiJumpers() throws DatabaseException {
        Map<Integer, SkiJumper> result = new HashMap<>();

        connectionManager.connect();
        try {
            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(SELECT_SKIJUMPERS_SQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int fisId = resultSet.getInt("jumper_id");
                String name = resultSet.getString("name");
                String country = resultSet.getString("country");
                SkiJumper skiJumper = new SkiJumper(fisId, name, country, true);
                result.put(fisId, skiJumper);
            }
            resultSet.close();
            preparedStatement.close();
            connectionManager.close();
        } catch (SQLException e) {
            throw new DatabaseException("Cannot select existing ski jumpers from database.", e);
        }

        return result;
    }

    private void insertCompetition(Competition competition) throws SQLException {
        PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(INSERT_COMPETITION_SQL);
        preparedStatement.setInt(1, competition.getRaceId());
        preparedStatement.setString(2, competition.getName());
        preparedStatement.setString(3, competition.getPlace());
        preparedStatement.setDate(4, new java.sql.Date(competition.getDate().getTime()));
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    private void insertNewSkiJumpers(Map<Integer, SkiJumper> skiJumpers) throws SQLException {
        PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(INSERT_SKIJUMPERS_SQL);
        for (SkiJumper skiJumper : skiJumpers.values()) {
            if (!skiJumper.isAdded()) {
                preparedStatement.setInt(1, skiJumper.getFisId());
                preparedStatement.setString(2, skiJumper.getName());
                preparedStatement.setString(3, skiJumper.getCountry());
                preparedStatement.addBatch();
                skiJumper.setAdded(true);
            }
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
    }

    private void insertCompetitionResults(Competition competition) throws SQLException {
        PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(INSERT_COMPETITION_RESULT_SQL);
        for (CompetitionResult result : competition.getResults()) {
            preparedStatement.setInt(1, competition.getRaceId());
            preparedStatement.setInt(2, result.getSkiJumper().getFisId());
            preparedStatement.setInt(3, result.getPlace());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
    }
}
