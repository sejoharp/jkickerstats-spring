package jkickerstats.services;

import jkickerstats.persistence.MatchLister;
import jkickerstats.persistence.MatchPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Component
public class StatsUpdater {
    private static final Logger LOG = Logger.getLogger(StatsUpdater.class.getName());

    private final MatchLister matchLister;
    private final MatchPersister persister;
    private final ParsedMatchesRetriever parsedMatchesRetriever;

    @Autowired
    StatsUpdater(MatchLister matchLister, MatchPersister persister, ParsedMatchesRetriever parsedMatchesRetriever) {
        this.matchLister = matchLister;
        this.persister = persister;
        this.parsedMatchesRetriever = parsedMatchesRetriever;
    }

    public void updateStats(Integer seasonId) {
        LOG.info("updater batch started.");
        long matchesCountBefore = matchLister.countMatches();
        if (matchLister.noMatchesAvailable()) {
            getAllData();
        } else if (seasonId != null) {
            loadSeason(seasonId);
        } else {
            updateCurrentSeason();
        }
        long matchesCountAfter = matchLister.countMatches();
        LOG.info(String.format("updater batch finished and found %s new matches.",
                matchesCountAfter - matchesCountBefore));
    }

    void getAllData() {
        parsedMatchesRetriever.getSeasonIDs()
                .map(parsedMatchesRetriever::getViaDocs)
                .flatMap(Function.identity())
                .map(parsedMatchesRetriever::downloadGamesFromMatch)
                .forEach(persister::save);
    }

    void updateCurrentSeason() {
        int seasonId = getCurrentSeasonId(parsedMatchesRetriever.getSeasonIDs());
        loadSeason(seasonId);
    }

    private void loadSeason(int seasonId) {
        parsedMatchesRetriever.getViaDocs(seasonId)
                .filter(matchLister::isNewMatch)
                .map(parsedMatchesRetriever::downloadGamesFromMatch)
                .forEach(persister::save);
    }

    static int getCurrentSeasonId(Stream<Integer> seasons) {
        return seasons
                .max(Integer::compare)
                .orElseThrow(() -> new IllegalStateException("no seasons found."));
    }


}
