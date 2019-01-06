package jkickerstats.services;

import jkickerstats.domain.Match;

import java.util.stream.Stream;

public interface ParsedMatchesRetriever {
    Stream<Match> getViaDocs(Integer seasonId);

    Match downloadGamesFromMatch(Match match);

    Stream<Integer> getSeasonIDs();
}
