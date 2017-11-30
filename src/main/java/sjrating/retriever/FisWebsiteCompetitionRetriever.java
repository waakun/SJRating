package sjrating.retriever;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;
import sjrating.model.Competition;
import sjrating.model.CompetitionResult;
import sjrating.model.SkiJumper;
import sjrating.retriever.exception.RetrieverException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class FisWebsiteCompetitionRetriever implements CompetitionRetriever {

    private static final String URL = "https://data.fis-ski.com/dynamic/results.html?sector=JP&raceid=%d";

    public Competition fetchCompetition(int raceID, Map<Integer, SkiJumper> skiJumpers) throws RetrieverException {
        Document document = connect(raceID);
        return parse(raceID, skiJumpers, document);
    }

    private Document connect(int raceID) throws RetrieverException {
        String url = String.format(URL, raceID);
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RetrieverException("Cannot connect to website.", e);
        }
    }

    private Competition parse(int raceID, Map<Integer, SkiJumper> skiJumpers, Document document) throws RetrieverException {
        Competition competition;
        try {
            String dateText = document.select(".padding-content h3 span").get(0).text();
            Date date = Competition.DATE_FORMAT.parse(dateText);

            String placeName = document.select(".padding-content h3 a").get(0).text();
            String raceName = document.select(".padding-content div div h4").get(0).text().trim().replace("\u00a0", "");

            competition = new Competition(raceID, date, raceName, placeName);

            Elements rows = document.select("table.table-datas").get(1).getElementsByTag("tr");
            for (int i = 1; i < rows.size() - 1; i++) {
                Element row = rows.get(i);

                if (row.children().size() != 11) {
                    continue;
                }

                // Parse place
                String placeText = row.child(0).text().replace("\u00a0", "");
                Integer place;
                try {
                    place = Integer.parseInt(placeText);
                } catch (NumberFormatException e) {
                    place = 999;
                }

                Integer id = Integer.parseInt(row.child(2).text().replace("\u00a0", ""));
                String name = row.child(3).child(0).text().replace("\u00a0", "");
                String country = row.child(5).text().replace("\u00a0", "");

                // Find or add new ski jumper to the map
                SkiJumper skiJumper;
                if (!skiJumpers.containsKey(id)) {
                    skiJumper = new SkiJumper(id, name, country);
                    skiJumpers.put(id, skiJumper);
                } else {
                    skiJumper = skiJumpers.get(id);
                }

                // Add result to competition
                CompetitionResult result = new CompetitionResult(skiJumper, place);
                competition.addResult(result);
            }
        } catch (ParseException | Selector.SelectorParseException e) {
            throw new RetrieverException("Cannot parse website", e);
        }
        return competition;
    }

}
