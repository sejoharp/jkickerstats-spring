package jkickerstats.interfaces;

import jkickerstats.types.Match;

import java.util.stream.Stream;

public interface ParsedMatchesRetriever {
    Stream<Match> get(Integer seasonId);

    Match downloadGamesFromMatch(Match match);

    Stream<Integer> getSeasonIDs();
}
