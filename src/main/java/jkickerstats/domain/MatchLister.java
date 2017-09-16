package jkickerstats.domain;

import jkickerstats.types.Match;

import java.util.List;

public interface MatchLister {
    List<Match> getAllMatches();

    default boolean isNewMatch(Match match){
        return !getAllMatches().contains(match);
    }

    default boolean noMatchesAvailable() {
        return getAllMatches().size() == 0;
    }

    default long countMatches() {
        return getAllMatches().size();
    }
}
