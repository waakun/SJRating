package sjrating;

import sjrating.database.impl.CompetitionDaoImpl;
import sjrating.database.impl.RatingDaoImpl;
import sjrating.database.DatabaseConnectionManager;
import sjrating.properties.PropertiesLoader;
import sjrating.properties.exception.PropertiesLoaderException;
import sjrating.retriever.CompetitionRetriever;
import sjrating.retriever.FisWebsiteCompetitionRetriever;
import sjrating.service.SJRatingService;
import sjrating.service.SJRatingServiceImpl;
import sjrating.service.exception.ServiceException;

public class Application {

    public static void main(String[] args) {
        if (args.length == 0) {
            displayHelp();
            return;
        }

        DatabaseConnectionManager manager = null;
        try {
            manager = new DatabaseConnectionManager(PropertiesLoader.getDatabaseCredentials());
        } catch (PropertiesLoaderException e) {
        }
        CompetitionDaoImpl competitionDao = new CompetitionDaoImpl(manager);
        RatingDaoImpl ratingDao = new RatingDaoImpl(manager);
        CompetitionRetriever retriever = new FisWebsiteCompetitionRetriever();
        SJRatingService service = new SJRatingServiceImpl(competitionDao, ratingDao, retriever);

        switch (args[0]) {
            case "calculate":
                try {
                    service.recalculate();
                } catch (ServiceException e) {
                    System.err.println("Error during calculating rating.");
                    System.err.println(e.getMessage());
                }
                break;
            case "add":
                int[] ids = new int[args.length - 1];
                for (int i = 1; i < args.length; i++) {
                    try {
                        ids[i - 1] = Integer.parseInt(args[i]);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid competition ID: " + args[i]);
                        return;
                    }
                }
                try {
                    service.insertCompetitions(ids);
                } catch (ServiceException e) {
                    System.err.println("Error during adding competitions.");
                    System.err.println(e.getMessage());
                }
                break;
            default:
                System.err.println("Invalid command.");
                displayHelp();
                break;
        }
    }

    private static void displayHelp() {
        System.err.println("SJRating Version 1.0, \u00a9 2017 W. Adamczewski");
        System.err.println("Usage:");
        System.err.println(" * sjrating calculate - recalculates ratings for all competitions.");
        System.err.println(" * sjrating add [ids] - inserts new competitions into database.");
        System.err.println("This application uses competition IDs from fis-ski.com website.");
    }
}
