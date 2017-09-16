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
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static jkickerstats.interfaces.ParsedMatchesRetrieverImpl.*;

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
        parsedMatchesRetriever.getSeasonIDs()
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

    static int getCurrentSeasonId(Stream<Integer> seasons) {
        return seasons
                .max(Integer::compare)
                .orElseThrow(() -> new IllegalStateException("no seasons found."));
    }

    List<Match> downloadAllMatches() {
        List<Match> allMatches = new ArrayList<>();
        parsedMatchesRetriever.getSeasonIDs().forEach(seasonId -> {
            System.out.println("processing seasonId:" + seasonId);
            getLigaLinks(seasonId).forEach(ligaLink -> {
                System.out.println("processing ligalink:" + ligaLink);
                getMatches(ligaLink).forEach(match -> {
                    System.out.println("processing match:" + match.getMatchDate());
                    allMatches.add(new MatchBuilder(match)//
                            .withGames(getGames(match.getMatchLink()).collect(toList()))//
                            .build());
                });
            });
        });
        return allMatches;
    }

    List<Game> downloadAllGames() {
        List<Game> games = new ArrayList<>();
        parsedMatchesRetriever.getSeasonIDs()
                .forEach(seasonId -> getLigaLinks(seasonId)//
                        .forEach(ligaLink -> getMatchLinks(ligaLink)//
                                .forEach(matchLink -> games.addAll(getGames(matchLink).collect(toList())))));
        return games;
    }

}
