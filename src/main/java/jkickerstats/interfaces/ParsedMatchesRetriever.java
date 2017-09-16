package jkickerstats.interfaces;

import jkickerstats.types.Match;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface ParsedMatchesRetriever {
    Stream<Match> get(Integer seasonId);

    Match downloadGamesFromMatch(Match match);

    List<Integer> getSeasonIDs();
}
