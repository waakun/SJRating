package sjrating.model;

public class CompetitionResult {

    private SkiJumper skiJumper;
    private int place;
    private double ratingBefore;
    private double ratingAfter;
    private int id;

    public CompetitionResult(SkiJumper skiJumper, int place) {
        this(skiJumper, place, -1);
    }

    public CompetitionResult(SkiJumper skiJumper, int place, int id) {
        this.skiJumper = skiJumper;
        this.place = place;
        this.id = id;
    }

    public SkiJumper getSkiJumper() {
        return skiJumper;
    }

    public int getPlace() {
        return place;
    }

    public double getRatingBefore() {
        return ratingBefore;
    }

    public void setRatingBefore(double ratingBefore) {
        this.ratingBefore = ratingBefore;
    }

    public double getRatingAfter() {
        return ratingAfter;
    }

    public void setRatingAfter(double ratingAfter) {
        this.ratingAfter = ratingAfter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return place + " " + skiJumper;
    }
}
