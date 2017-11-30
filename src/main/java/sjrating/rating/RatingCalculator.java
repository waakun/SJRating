package sjrating.rating;

import sjrating.model.Competition;
import sjrating.model.CompetitionResult;

import java.util.List;

public class RatingCalculator {

    /**
     * Calculates rating from all given competitions.
     * @param competitions
     */
    public static void calculateRating(List<Competition> competitions) {
        for (Competition competition : competitions) {
            List<CompetitionResult> results = competition.getResults();

            for (CompetitionResult result : results) {
                result.setRatingBefore(result.getSkiJumper().getRating());
            }

            for (CompetitionResult player : results) {
                double ratingBefore = player.getRatingBefore();
                double ratingAfter = ratingBefore;
                for (CompetitionResult opponent : results) {
                    // no change if same ski jumper
                    if (player.getSkiJumper().equals(opponent.getSkiJumper())) {
                        continue;
                    }

                    boolean win;
                    // no change if tie
                    if (player.getPlace() == opponent.getPlace())
                        continue;
                    else
                        win = player.getPlace() < opponent.getPlace();

                    double ratingBeforeDifference = win
                            ? ratingBefore - opponent.getRatingBefore()
                            : opponent.getRatingBefore() - ratingBefore;

                    double ratingDelta = 1 / (1 + Math.pow(10, ratingBeforeDifference / 400));
                    ratingAfter += ratingDelta * (win ? 1 : -1);
                }
                player.setRatingAfter(ratingAfter);
                player.getSkiJumper().setRating(ratingAfter);
            }
        }
    }

}
