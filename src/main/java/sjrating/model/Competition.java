package sjrating.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Competition {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    private final Date date;
    private final int raceId;
    private final String name;
    private final String place;
    private List<CompetitionResult> results;

    public Competition(int raceId, Date date, String name, String place) {
        this.raceId = raceId;
        this.date = date;
        this.name = name;
        this.place = place;
        results = new ArrayList<>();
    }

    public void addResult(CompetitionResult result) {
        results.add(result);
    }

    public CompetitionResult getResult(int id) {
        return results.get(id);
    }

    public Date getDate() {
        return date;
    }

    public int getRaceId() {
        return raceId;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public List<CompetitionResult> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return DATE_FORMAT.format(date) + " " + name + ", " + place + " " + results.toString();
    }
}
