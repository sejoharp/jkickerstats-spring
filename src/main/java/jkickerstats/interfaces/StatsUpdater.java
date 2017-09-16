package jkickerstats.interfaces;

import jkickerstats.domain.MatchLister;
import jkickerstats.domain.MatchPersister;
import jkickerstats.types.Game;
import jkickerstats.types.Match;
import jkickerstats.types.Match.MatchBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

@Component
class StatsUpdater {
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

    void updateStats() {
        LOG.info("updater batch started.");
        long matchesCountBefore = matchLister.countMatches();
        if (matchLister.noMatchesAvailable()) {
            getAllData();
        } else {
            updateData();
        }
        long matchesCountAfter = matchLister.countMatches();
        LOG.info(String.format("updater batch finished and found %s new matches.",
                matchesCountAfter - matchesCountBefore));
    }

    void getAllData() {
        parsedMatchesRetriever.getSeasonIDs().stream()
                .map(parsedMatchesRetriever::get)
                .flatMap(Function.identity())
                .map(parsedMatchesRetriever::downloadGamesFromMatch)
                .forEach(persister::save);
    }

    void updateData() {
        Integer seasonId = getCurrentSeasonId(parsedMatchesRetriever.getSeasonIDs());
        parsedMatchesRetriever.get(seasonId)
                .filter(matchLister::isNewMatch)
                .map(parsedMatchesRetriever::downloadGamesFromMatch)
                .forEach(persister::save);
    }

    static int getCurrentSeasonId(List<Integer> seasons) {
        return seasons.stream()
                .max(Integer::compare)
                .orElseThrow(() -> new IllegalStateException("no seasons found."));
    }

    List<Match> downloadAllMatches() {
        List<Match> allMatches = new ArrayList<>();
        for (Integer seasonId : parsedMatchesRetriever.getSeasonIDs()) {
            System.out.println("processing seasonId:" + seasonId);
            for (String ligaLink : ParsedMatchesRetrieverImpl.getLigaLinks(seasonId)) {
                System.out.println("processing ligalink:" + ligaLink);
                for (Match match : ParsedMatchesRetrieverImpl.getMatches(ligaLink)) {
                    System.out.println("processing match:" + match.getMatchDate());
                    allMatches.add(new MatchBuilder(match)//
                            .withGames(ParsedMatchesRetrieverImpl.getGames(match.getMatchLink()))//
                            .build());
                }
            }
        }
        return allMatches;
    }

    List<Game> downloadAllGames() {
        List<Game> games = new ArrayList<>();
        parsedMatchesRetriever.getSeasonIDs()
                .forEach(seasonId -> ParsedMatchesRetrieverImpl.getLigaLinks(seasonId)//
                        .forEach(ligaLink -> ParsedMatchesRetrieverImpl.getMatchLinks(ligaLink)//
                                .forEach(matchLink -> games.addAll(ParsedMatchesRetrieverImpl.getGames(matchLink)))));
        return games;
    }

}
