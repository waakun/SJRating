package sjrating.retriever;

import sjrating.model.Competition;
import sjrating.model.SkiJumper;
import sjrating.retriever.exception.RetrieverException;

import java.util.Map;

public interface CompetitionRetriever {
    Competition fetchCompetition(int raceID, Map<Integer, SkiJumper> skiJumpers) throws RetrieverException;
}
