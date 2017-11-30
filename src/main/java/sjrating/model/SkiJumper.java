package sjrating.model;

public class SkiJumper {
    private int fisId;
    private String name;
    private String country;
    private boolean added;
    private double rating;

    public SkiJumper(int fisId, String name, String country) {
        this(fisId, name, country, false);
    }

    public SkiJumper(int fisId, String name, String country, boolean added) {
        this.fisId = fisId;
        this.name = name;
        this.country = country;
        this.added = added;
        this.rating = 1000.0;
    }

    public int getFisId() {
        return fisId;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return name + " " + country;
    }
}
